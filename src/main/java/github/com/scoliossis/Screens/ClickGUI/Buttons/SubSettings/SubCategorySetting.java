package github.com.scoliossis.Screens.ClickGUI.Buttons.SubSettings;

import github.com.scoliossis.Util.FontUtil;
import github.com.scoliossis.Util.RenderUtil;

import java.awt.*;

import static github.com.scoliossis.Screens.ClickGUI.Buttons.Module.xOff;
import static github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain.*;

public class SubCategorySetting {
    public static void draw() {
        // background
        RenderUtil.drawRect(x, y, width, height, getBackgroundColor(subSetting));
        clickStuff();

        float textX = x + (width/2f) - (FontUtil.getStringWidth(settingName, fontSize)/2f);

        RenderUtil.drawStringCenteredY("&o" + settingName, textX, y + (height / 2f), fontSize, new Color(0xEEEEEE));
    }

    private static void clickStuff() {
        if (isHovered(mX - xOff, mY, x, y, width, height)) {
            if (isLeftButtonPressed() || isRightButtonPressed()) {
                if ((boolean) subSetting.get(Boolean.class)) startGrowing(subSetting, -2);
                else startGrowing(subSetting, 2);

                subSetting.set(!(boolean) subSetting.get(Boolean.class));
            }
        }
    }
}
