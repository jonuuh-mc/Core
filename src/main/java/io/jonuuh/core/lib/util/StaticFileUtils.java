package io.jonuuh.core.lib.util;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Scanner;

public abstract class StaticFileUtils
{
    public static String getStaticHostedAsset(String assetPath)
    {
        return getStaticHostedAsset(assetPath, "refs/heads/master/" + assetPath);
    }

    public static String getStaticHostedAsset(String assetPath, String fallbackAssetPath)
    {
        String asset = getGithubRepoAssetViaRawApi("jonuuh-mc", "StaticFileStorage", assetPath);

        if (asset == null)
        {
            asset = getGithubRepoAssetViaRawUserContent("jonuuh-mc", "StaticFileStorage", fallbackAssetPath);

            if (asset == null)
            {
                Log4JLogger.INSTANCE.error("Failed to access static asset via raw api ({}) or via raw usercontent ({})", assetPath, fallbackAssetPath);
            }
        }

        return asset;
    }

    public static String getGithubRepoAssetViaRawApi(String user, String repo, String assetPath)
    {
        String url = String.format("https://api.github.com/repos/%s/%s/contents/%s", user, repo, assetPath);

        try
        {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(url).openConnection();
            httpsURLConnection.setRequestProperty("Accept", "application/vnd.github.raw+json");
            return parseHttpsURLConnectionContent(httpsURLConnection);
        }
        catch (IOException exception)
        {
            Log4JLogger.INSTANCE.error("Failed to connect to {}", url, exception);
            return null;
        }
    }

    public static String getGithubRepoAssetViaRawUserContent(String user, String repo, String urlAssetPath)
    {
        String url = String.format("https://raw.githubusercontent.com/%s/%s/%s", user, repo, urlAssetPath);

        try
        {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(url).openConnection();
            // All of this is only needed because github.io domain certificate is not in java trust store by default? (https://stackoverflow.com/a/34533740)
            httpsURLConnection.setSSLSocketFactory(createSSLContext(getGithubIOCertificateString()).getSocketFactory());
            return parseHttpsURLConnectionContent(httpsURLConnection);
        }
        catch (IOException | GeneralSecurityException exception)
        {
            Log4JLogger.INSTANCE.error("Failed to connect to {}", url, exception);
            return null;
        }
    }

    private static String parseHttpsURLConnectionContent(HttpsURLConnection httpsURLConnection)
    {
        try
        {
            InputStream inputStream = httpsURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            String urlContent = scanner.next();

            scanner.close();
            inputStream.close();
            httpsURLConnection.disconnect();

            return urlContent;
        }
        catch (IOException exception)
        {
            Log4JLogger.INSTANCE.error("Failed to parse content for {}", httpsURLConnection.getURL().toString(), exception);
            return null;
        }
    }

    private static SSLContext createSSLContext(String derCertificateString) throws GeneralSecurityException, IOException
    {
        ByteArrayInputStream derInputStream = new ByteArrayInputStream(derCertificateString.getBytes());

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) cf.generateCertificate(derInputStream);
        String alias = certificate.getSubjectX500Principal().getName();

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null);
        trustStore.setCertificateEntry(alias, certificate);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(trustStore, null);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return sslContext;
    }

    private static String getGithubIOCertificateString() throws IOException
    {
        InputStream inputStream = StaticFileUtils.class.getClassLoader().getResourceAsStream("assets/_.github.io.crt");
        String certificateStr = "";

        if (inputStream != null) // Resource could not be found
        {
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            certificateStr = scanner.next();
            scanner.close();
            inputStream.close();
        }

        return certificateStr;
    }
}
