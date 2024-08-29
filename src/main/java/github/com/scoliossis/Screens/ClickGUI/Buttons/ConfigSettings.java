package github.com.scoliossis.Screens.ClickGUI.Buttons;

import github.com.scoliossis.Main;
import github.com.scoliossis.Screens.ClickGUI.Settings.Config;
import github.com.scoliossis.Util.FontUtil;
import github.com.scoliossis.Util.RenderUtil;

import java.awt.*;
import java.io.File;
import java.util.Objects;

import static github.com.scoliossis.Screens.ClickGUI.Buttons.Module.*;
import static github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain.*;

public class ConfigSettings {
    public static void draw() {
        for (File file : Objects.requireNonNull(Config.configsFileBase.listFiles())) {
            RenderUtil.enableTranslate(0, height);
            //yOffset = categoryHeights.get(index);
            //y = startY + yOffset + scrollOffset;

            // if hovering over button
            if (isHovered(mX, mY, baseX, baseY + y, width, height)) {

                // if left clicked on it.
                if (isLeftButtonPressed()) {
                    Config.save(Main.selectedConfig);
                    Config.load(file.getName());
                }
            }

            // background
            RenderUtil.drawRect(x, y, width, height, getBackgroundColor(category));

            RenderUtil.drawRect(x + xOff, y, width - wOff, height, getBackgroundColor(setting));
            if (file.getName().equals(Main.selectedConfig)) RenderUtil.drawRect(x + xOff, y, width - wOff, height, new Color(color));


            RenderUtil.drawStringCenteredY("&o" + file.getName(), x + width - stringIndentLeft - FontUtil.getStringWidth(file.getName(), fontSize), y + (height / 2f), fontSize, new Color(0xEEEEEE));
            drawOutline(0);
        }
    }
}
