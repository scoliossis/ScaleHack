package github.com.scoliossis.Screens.ClickGUI.Buttons;

import github.com.scoliossis.Screens.ClickGUI.Buttons.SubSettings.*;
import github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain;
import github.com.scoliossis.Screens.ClickGUI.Settings.Config;
import github.com.scoliossis.Util.ChatUtil;
import github.com.scoliossis.Util.RenderUtil;

import java.awt.*;
import java.util.Arrays;

import static github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain.*;
import static github.com.scoliossis.Screens.ClickGUI.Buttons.Module.*;

public class Children {

    static void draw(Config child) {
        float yNow = RenderUtil.getTranslate()[1];

        RenderUtil.push();

        float size = getSize(child);

        if (getSize(child) != 0 || shouldShow(child)) {
            if (shouldShow(child) && size == 0) size = 1;

            // loop through sons
            for (Config subSetting : child.sons) {
                if (shouldShowChild(subSetting)) {
                    RenderUtil.enableScale(1, size);

                    ClickGUIScreenMain.subSetting = subSetting;


                    float subHeight = 15;

                    settingName = formatSettingName(subSetting.name);

                    height = subHeight;

                    switch (subSetting.type) {
                        // i miss java 14 :sob: i wanna do case E -> {} but i cant :(
                        case BOOLEAN:
                            BooleanSetting.draw();
                            break;
                        case SLIDER:
                            SliderSetting.draw();
                            break;
                        case SUBCATEGORY:
                            SubCategorySetting.draw();
                            break;
                        case MODE:
                            ModeSetting.draw();
                            break;

                        case EXTRA:
                            ExtraSetting.draw();
                            break;
                        case KEYBIND:
                            KeybindSetting.draw();
                            break;
                    }

                    drawOutline();

                    RenderUtil.enableTranslate(0, height);

                    // having a function to draw children is very handy!
                    // who tf says handy dawg u better not push these autistic comments
                    if (!subSetting.sons.isEmpty()) draw(subSetting);
                }

                //categoryHeights.set(index, yOffset + height*size);
            }
        }

        float yOff = RenderUtil.getTranslate()[1]-yNow;
        if (size == 1) yOff /= RenderUtil.getScale()[1];

        RenderUtil.pop();

        RenderUtil.enableTranslate(0, yOff);
    }

    static boolean shouldShowChild(Config setting) {
        // yes this would be a single if statement. this is more readable
        if (setting.parent != null && setting.parent.parent != null && setting.parent.parent.parent != null) {
            switch (setting.parent.type) {
                case MODE:
                    for (int settingInt : setting.modeRequirement) {
                        if (settingInt == (int) setting.parent.get(Integer.class)) {
                            return true;
                        }
                    }
                    return false;
            }

            return true;
        }

        return true;
    }

    static boolean shouldShow(Config setting) {
        // yes this would be a single if statement. this is more readable
        if (setting.parent != null) {
            if (setting.parent.parent == null) return settingsPressedRight.contains(setting);
            else {
                switch (setting.type) {
                    // why does "case BOOLEAN, SUBCATEGORY:" require jdk 14+. java 8 is HORRID!
                    case BOOLEAN:
                    case SUBCATEGORY:
                        return (boolean) setting.get(Boolean.class);
                }

                return true;
            }
        }

        return true;
    }
}
