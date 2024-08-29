package github.com.scoliossis.Screens.ClickGUI.Buttons.SubSettings;

import github.com.scoliossis.Util.RenderUtil;

import java.awt.*;

import static github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain.*;

public class BooleanSetting {
    static int boxSize = 8;

    public static void draw() {
        // background
        RenderUtil.drawRect(x, y, width, height, getBackgroundColor(subSetting));

        // if hovering over button
        clickStuff();

        // draws a growing square infront of a white one, square grows until behind square is filled
        drawEnabledAnimation();

        RenderUtil.drawStringCenteredY("&o" + settingName, x + 5, y + (height / 2f), fontSize, new Color(0xEEEEEE));
    }

    static void drawEnabledAnimation() {
        float smallSquareX = x + width - 20;
        float smallSquareY = y + height / 2 - boxSize / 2f;

        // draws a white box
        RenderUtil.drawRoundedRect(smallSquareX, smallSquareY, boxSize, boxSize, 1, new Color(255, 255, 255));

        // if setting enabled + animation done
        float size = getSize(subSetting);
        if ((boolean) subSetting.get(Boolean.class) && size == 0) size = 1;
        smallSquareX -= boxSize*size / 2f - boxSize / 2f;
        smallSquareY -= boxSize*size / 2f - boxSize / 2f;

        RenderUtil.drawRoundedRect(smallSquareX, smallSquareY, boxSize*size, boxSize * size, 1, new Color(color));
        //RenderUtil.drawRoundedRect(smallSquareX, smallSquareY, boxW*size, boxH, 1, new Color(color));
    }


    static void clickStuff() {
        if (isHovered(mX, mY, x, y, width, height)) {
            // if left clicked on.
            if (isLeftButtonPressed()) {
                if ((boolean) subSetting.get(Boolean.class)) startGrowing(subSetting, -2);
                else startGrowing(subSetting, 2);

                // toggles on/off state
                subSetting.set(!(boolean) subSetting.get(Boolean.class));
            }
        }
    }
}
