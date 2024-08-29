package github.com.scoliossis.Screens.ClickGUI.Buttons.SubSettings;

import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

import static github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain.*;

public class ExtraSetting {

    public static ArrayList<String> settingsExtraPressed = new ArrayList<>();
    public static ArrayList<Float> settingsExtraPressedInfo = new ArrayList<>();


    public static void draw() {
        String settingName2 = setting.name + subSetting.name;

        boolean[] array = Configs.onlyWhenHeld;

        if (Objects.equals(subSetting.name, "Hide")) array = Configs.hideFromArray;

        float boxW = 8;

        // background
        RenderUtil.drawRect(x, y, width, height, getBackgroundColor(subSetting));

        // if hovering over button
        if (isHovered(mX, mY, x, y, width, height)) {

            // if left clicked on.
            if (isLeftButtonPressed()) {
                // checks if already animating
                if (array[setting.index]) startGrowing(subSetting, -2);
                else startGrowing(subSetting, 2);

                // toggles on/off state
                array[setting.index] = !array[setting.index];
            }
        }


        float smallSquareX = x + width - 20;
        float smallSquareY = y + height / 2 - boxW / 2f;

        RenderUtil.drawRoundedRect(smallSquareX, smallSquareY, boxW, boxW, 1, new Color(255, 255, 255));

        float size = getSize(subSetting);
        if (array[setting.index] && size == 0) size = 1;
        smallSquareX -= boxW*size / 2f - boxW / 2f;
        smallSquareY -= boxW*size / 2f - boxW / 2f;

        RenderUtil.drawRoundedRect(smallSquareX, smallSquareY, boxW*size, boxW * size, 1, new Color(color));
        //RenderUtil.drawRoundedRect(smallSquareX, smallSquareY, boxW*size, boxH, 1, new Color(color));\

        RenderUtil.drawStringCenteredY("&o" + settingName, x + 5, y + (height / 2f), fontSize, new Color(0xEEEEEE));
    }
}
