package github.com.scoliossis.Screens.ClickGUI.Buttons.SubSettings;

import github.com.scoliossis.Screens.ClickGUI.Settings.Config;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.FontUtil;
import github.com.scoliossis.Util.RenderUtil;

import java.awt.*;

import static github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain.*;

public class KeybindSetting {

    public static Config currentKeybind = null;

    public static int lastKey = -1;
    public static char lastChar = '\u0000';


    // KEYBIND SETTING
    public static void draw() {
        RenderUtil.drawRect(x, y, width, height, getBackgroundColor(subSetting));

        if (isHovered(mX, mY, x, y, width, height) && leftMousePressed) {
            currentKeybind = setting;
        }

        char character = '\u0000';
        int key = -1;

        // using setting.index is NOT a mistake.
        if (Configs.keybinds[setting.index] != null) {
            character = Configs.keybinds[setting.index].aChar;
            key = Configs.keybinds[setting.index].key;
        }

        String characterText = "[" + character + "]";

        if (key == -1)
            characterText = "none";
        else if (character == '\u0000')
            characterText = "[" + RenderUtil.getCharFromInt(key) + "]";

        if (currentKeybind == setting) {
            // if escape is pressed, dont bind.
            if (lastKey == 1) {
                Configs.keybinds[setting.index] = null;
                currentKeybind = null;
            }

            // if a key is pressed gogogo
            else if (lastKey != -1) {
                characterText = "[" + lastChar + "]";
                Configs.keybinds[setting.index] = new Config.keybindStuff(lastKey, lastChar);
                currentKeybind = null;
            }

            // show listening
            else
                characterText = "[LISTENING]";
        }

        RenderUtil.drawStringCenteredY("&o" + settingName, x + 5, y + (height / 2f), fontSize, new Color(0xEEEEEE));
        RenderUtil.drawStringCenteredY("&o" + characterText, x + width - FontUtil.getStringWidth(characterText, fontSize) - 10, y + (height / 2f), fontSize, new Color(0xEEEEEE));
    }
}
