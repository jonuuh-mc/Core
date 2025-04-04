package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.GuiColorType;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public class GuiLabel extends GuiElement
{
    protected final FontRenderer fontRenderer;
    protected String text;
    protected boolean centerStrInWidth;
    protected boolean centerStrInHeight;
    protected boolean fitWidthToStr;
    protected boolean fitHeightToStr;
    protected float textXPos;
    protected float textYPos;

    public GuiLabel(String elementName, float xPos, float yPos, float width, float height, String text)
    {
        super(elementName, xPos, yPos, width, height);
        this.fontRenderer = mc.fontRendererObj;
        this.text = text;
        centerStrInWidth = centerStrInHeight = true;
    }

    public GuiLabel(String elementName, float xPos, float yPos, String text)
    {
        super(elementName, xPos, yPos);
        this.fontRenderer = mc.fontRendererObj;
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    public GuiLabel setText(String text)
    {
        this.text = text;
        return this;
    }

    protected void onInitGui(ScaledResolution scaledResolution)
    {
        textXPos = worldXPos();
        textYPos = worldYPos();

        if (centerStrInWidth)
        {
            textXPos = worldXPos() + (getWidth() / 2) - (fontRenderer.getStringWidth(text) / 2F);
        }
        else if (fitWidthToStr)
        {
            setWidth(fontRenderer.getStringWidth(text));
        }

        if (centerStrInHeight)
        {
            textYPos = worldYPos() + (getHeight() / 2) - ((fontRenderer.FONT_HEIGHT - 1) / 2F);
        }
        else if (fitHeightToStr)
        {
            setHeight(fontRenderer.FONT_HEIGHT - 1);
        }
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        fontRenderer.drawString(text, textXPos, textYPos, getColor(GuiColorType.BASE).toPackedARGB(), true);
    }
}
