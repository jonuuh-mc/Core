package io.jonuuh.core.local;

import io.jonuuh.core.lib.config.Config;
import io.jonuuh.core.lib.config.command.CommandOpenSettingsGui;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.BoolSetting;
import io.jonuuh.core.lib.config.setting.types.DoubleListSetting;
import io.jonuuh.core.lib.config.setting.types.DoubleSetting;
import io.jonuuh.core.lib.config.setting.types.IntListSetting;
import io.jonuuh.core.lib.config.setting.types.StringSetting;
import io.jonuuh.core.lib.update.UpdateHandler;
import io.jonuuh.core.lib.util.ChatLogger;
import io.jonuuh.core.lib.util.ModLogger;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = Main.modID, version = Main.version, acceptedMinecraftVersions = "[1.8.9]")
public class Main
{
    public static final String modID = "modid";
    public static final String version = "0.0.0";
    public final ModLogger modLogger;

    public Main()
    {
        this.modLogger = ModLogger.createInstance(modID);
    }

    @Mod.EventHandler
    public void FMLPreInit(FMLPreInitializationEvent event)
    {
        Settings masterSettings = initSettings();
        Config.createInstance(event.getSuggestedConfigurationFile(), masterSettings);
    }

    @Mod.EventHandler
    public void FMLInit(FMLInitializationEvent event)
    {
        UpdateHandler.createInstance("barriermodels", version); // TODO: should be in FMLInit probably (has event listeners)

        ChatLogger.createInstance("\u00a7f[\u00a76Core\u00a7f] ");

        KeyBinding keyBinding = new KeyBinding("<description>", Keyboard.KEY_NONE, modID);
        ClientRegistry.registerKeyBinding(keyBinding);
        MinecraftForge.EVENT_BUS.register(new Events(keyBinding));

        ClientCommandHandler.instance.registerCommand(new CommandOpenSettingsGui(modID, new SettingsGuiImpl("master")));
    }

    private Settings initSettings()
    {
        Settings settings = new Settings();
        settings.put("DRAW_BACKGROUND", new BoolSetting(true));
        settings.put("BACKGROUND_OPACITY", new DoubleSetting(66D));
        settings.put("RENDER_RANGE", new IntListSetting(new int[]{1, 75}));
        settings.put("FAT_SLIDER", new DoubleListSetting(new double[]{10D, 15D, 20D, 5D, 30D, 45D, 1000D}));
        settings.put("BACKGROUND_COLOR", new StringSetting("BLUE"));

        return settings;
    }

//    private List<Settings> initSettingsList()
//    {
//        List<Settings> settingsList = new ArrayList<>();
//
//        Settings settings = new Settings("test");
//        settings.put("DRAW_BACKGROUND", new BoolSetting(true));
//        settings.put("BACKGROUND_OPACITY", new DoubleSetting(66D));
//        settings.put("RENDER_RANGE", new IntListSetting(new int[]{1, 75}));
//        settings.put("FAT_SLIDER", new DoubleListSetting(new double[]{10D, 15D, 20D, 5D, 30D, 45D, 1000D}));
//        settings.put("BACKGROUND_COLOR", new StringSetting("BLUE"));
//
//        settingsList.add(settings);
//
//        return settingsList;
//    }
}
