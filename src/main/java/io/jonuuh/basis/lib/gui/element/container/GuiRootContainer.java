package io.jonuuh.basis.lib.gui.element.container;

import io.jonuuh.basis.lib.gui.BaseGuiScreen;
import io.jonuuh.basis.lib.gui.event.lifecycle.InitGuiEvent;

public class GuiRootContainer extends GuiContainer
{
    /** The BaseGuiScreen containing this GuiRootContainer, should be a 1:1 relationship */
    public final BaseGuiScreen guiScreen;

    public GuiRootContainer(Builder builder)
    {
        super(builder);
        this.guiScreen = builder.guiScreen;
    }

    @Override
    public void setParent(GuiContainer parent)
    {
        throw new UnsupportedOperationException("Cannot set the parent of a GuiRootContainer.");
    }

    @Override
    protected boolean shouldScissor()
    {
        return false;
    }

    @Override
    public void onInitGui(InitGuiEvent event)
    {
        setWidth(event.sr.getScaledWidth());
        setHeight(event.sr.getScaledHeight());
        super.onInitGui(event);
    }

//    @Override
//    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
//    {
//        if (!isVisible())
//        {
//            return;
//        }
//        super.onScreenDraw(mouseX, mouseY, partialTicks);
////        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, worldXPos(), worldYPos(), getWidth(), getHeight(), 3, new Color("#FFFFFF", 0.2F));
//    }

    public static class Builder extends GuiContainer.AbstractBuilder<Builder, GuiRootContainer>
    {
        protected final BaseGuiScreen guiScreen;

        public Builder(BaseGuiScreen guiScreen)
        {
            super("ROOT");
            this.guiScreen = guiScreen;
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiRootContainer build()
        {
            return new GuiRootContainer(this);
        }
    }
}
