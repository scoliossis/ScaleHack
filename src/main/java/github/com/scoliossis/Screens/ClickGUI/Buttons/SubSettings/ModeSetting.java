package github.com.scoliossis.Screens.ClickGUI.Buttons.SubSettings;

import github.com.scoliossis.Screens.ClickGUI.Settings.Config;
import github.com.scoliossis.Util.ChatUtil;
import github.com.scoliossis.Util.FontUtil;
import github.com.scoliossis.Util.RenderUtil;

import java.awt.*;
import java.util.ArrayList;

import static github.com.scoliossis.Screens.ClickGUI.Buttons.Module.*;
import static github.com.scoliossis.Screens.ClickGUI.Buttons.Module.scrollOffset;
import static github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain.*;

public class ModeSetting {
    static ArrayList<Config> shownModes = new ArrayList<>();

    public static void draw() {
        // background
        RenderUtil.drawRect(x, y, width, height, getBackgroundColor(subSetting));
        clickStuff();

        String mode = subSetting.mode[(int) subSetting.get(Integer.class)];

        RenderUtil.drawStringCenteredY("&o" + settingName + "   -   " + mode, x + 5, y + (height / 2f), fontSize, new Color(0xEEEEEE));

        float size = getSize(subSetting, growingSettings2);

        if (shownModes.contains(subSetting) || size != 0) {
            if (size == 0) size = 1;
            drawOutline(0);

            RenderUtil.enableTranslate(0, height - (height*size));
            RenderUtil.enableScale(1, size);
            drawModes();
            RenderUtil.enableScale(1, 1/size);
            RenderUtil.enableTranslate(0, -(height - (height*size)));
        }

    }

    private static void clickStuff() {
        if (isHovered(mX - xOff, mY, x, y, width, height)) {
            if (isLeftButtonPressed() || isRightButtonPressed()) {
                if (shownModes.contains(subSetting)) {
                    startGrowing(subSetting, -1, growingSettings2);
                    shownModes.remove(subSetting);
                }
                else {
                    startGrowing(subSetting, 1, growingSettings2);
                    shownModes.add(subSetting);
                }
            }
        }
    }

    private static void drawModes() {
        //float oldY = y;

        float stringX = 5;
        float stringY = height;

        for (int i = 0; i < subSetting.mode.length; i++) {
            String settingName = subSetting.mode[i];
            if (i != subSetting.mode.length-1) settingName += "  ";

            float stringWidth = FontUtil.getStringWidth(settingName, fontSize);
            if (stringX + stringWidth >= width) {
                RenderUtil.drawRect(x, stringY, width, height, getBackgroundColorByInt(getParentsNumber(subSetting)+1));

                drawOutline(stringY);

                stringX = x+5;
                stringY += height;

                //y += height;
            }

            Color fontColor = new Color(0xEEEEEE);
            if (i == (int) subSetting.get(Integer.class)) fontColor = new Color(color);

            if (isHovered(mX, mY, stringX, stringY, stringWidth, height) && (isLeftButtonPressed() || isRightButtonPressed()))
                subSetting.set(i);

            RenderUtil.drawStringCenteredY("&o" + settingName, stringX, stringY + (height / 2f), fontSize, fontColor);

            stringX += FontUtil.getStringWidth(settingName, fontSize);
        }

        RenderUtil.drawRect(x, stringY, width, height, getBackgroundColorByInt(getParentsNumber(subSetting)+1));

        //yOffset -= (oldY-y);
        //categoryHeights.set(index, yOffset);
        RenderUtil.enableTranslate(0, stringY);
    }
}
