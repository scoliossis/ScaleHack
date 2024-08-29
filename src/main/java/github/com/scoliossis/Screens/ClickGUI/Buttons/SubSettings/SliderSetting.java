package github.com.scoliossis.Screens.ClickGUI.Buttons.SubSettings;

import github.com.scoliossis.Screens.ClickGUI.Settings.Config;
import github.com.scoliossis.Util.ChatUtil;
import github.com.scoliossis.Util.FontUtil;
import github.com.scoliossis.Util.MathUtil;
import github.com.scoliossis.Util.RenderUtil;

import java.awt.*;
import java.util.ArrayList;

import static github.com.scoliossis.Screens.ClickGUI.Buttons.Module.xOff;
import static github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain.*;

public class SliderSetting {

    static ArrayList<Config> slidersSliding = new ArrayList<>();
    static ArrayList<Float> sliderStuff = new ArrayList<>();


    static float settingGap, sliderWidth,
            settingNumber, settingFraction, fractionWidth,
            currentSliderNumber;

    public static void draw() {
        // background
        RenderUtil.drawRect(x, y, width, height, getBackgroundColor(subSetting));

        settingGap = subSetting.max - subSetting.min;
        sliderWidth = width - (xOff * 2) - 2;

        // get fraction
        settingNumber = (float) subSetting.get(Float.class);
        settingFraction = (settingNumber-subSetting.min) / settingGap;
        fractionWidth = settingFraction * sliderWidth;

        currentSliderNumber = fractionWidth;

        // slider setting n shi
        slowlySetNumber();

        // if hovering over button
        clickStuff();

        RenderUtil.drawRect(x + xOff, y + 2, currentSliderNumber, height - 4, new Color(color));


        RenderUtil.drawStringCenteredY("&o" + settingName, x + 5, y + (height / 2f), fontSize, new Color(0xEEEEEE));
        RenderUtil.drawStringCenteredY("&o" + settingNumber, x + width - FontUtil.getStringWidth("&o" + settingNumber, fontSize), y + (height / 2f), fontSize, new Color(0xEEEEEE));
    }

    static void slowlySetNumber() {
        if (slidersSliding.contains(subSetting)) {
            float currentSlider = sliderStuff.get(slidersSliding.indexOf(subSetting));
            currentSliderNumber = currentSlider + (((fractionWidth - currentSlider) / 100f) * deltaTime);

            sliderStuff.set(slidersSliding.indexOf(subSetting), currentSliderNumber);

            if (currentSliderNumber - currentSlider < 0.1 && currentSliderNumber - currentSlider > -0.1) {
                sliderStuff.remove(slidersSliding.indexOf(subSetting));
                slidersSliding.remove(subSetting);
            }
        }
    }

    static void clickStuff() {
        if (isHovered(mX - xOff, mY, x, y, sliderWidth, height)) {
            // attack.
            if (leftMouseDown && draggingSetting == null) {
                float mouseXonSlider = mX - baseX - xOff;
                float sliderSelectedFraction = mouseXonSlider / sliderWidth;
                float numberSetting = (sliderSelectedFraction * settingGap) + subSetting.min;

                subSetting.set(MathUtil.roundTo(numberSetting, subSetting.decimals));

                if (!slidersSliding.contains(subSetting)) {
                    slidersSliding.add(subSetting);
                    sliderStuff.add(fractionWidth);
                }
            }
        }
    }
}
