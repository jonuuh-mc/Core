package io.jonuuh.configlib.gui.elements.interactable.sliders;

import io.jonuuh.configlib.gui.ISettingsGui;

import java.util.Arrays;

public class GuiDualSlider extends GuiSliderBase
{
    public GuiDualSlider(ISettingsGui parent, int xPos, int yPos, int width, int height, double min, double max, double startValue1, double startValue2)
    {
        super(parent, xPos, yPos, width, height, min, max, Arrays.asList(startValue1, startValue2));
    }

    public GuiDualSlider(ISettingsGui parent, int xPos, int yPos, double min, double max, double startValue1, double startValue2)
    {
        super(parent, xPos, yPos, min, max, Arrays.asList(startValue1, startValue2));
    }
}