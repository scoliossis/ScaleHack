package github.com.scoliossis.Screens.ClickGUI.Buttons;


import github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain;
import github.com.scoliossis.Screens.ClickGUI.Settings.Config;
import github.com.scoliossis.Screens.ClickGUI.Settings.Setting;
import github.com.scoliossis.Util.ChatUtil;
import github.com.scoliossis.Util.FontUtil;
import github.com.scoliossis.Util.RenderUtil;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import static github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain.*;
import static github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain.setting;

// just normal modules, like killaura or whatever, drawn below a category
public class Module {

    public static float startY, scrollOffset, yOffset;

    public static ArrayList<Config> settingsPressed = new ArrayList<>();
    public static ArrayList<float[]> settingsPressedInfo = new ArrayList<>();

    public static ArrayList<Config> settingsPressedRight = new ArrayList<>();

    public static int xOff = 3;
    public static int wOff = xOff * 2;

    public static int stringIndentLeft = 9;

    public static void draw() {
        // check if children should be shown + draws them to resize size if closing category
        float size = getSize(category);
        if (categoryShowing && size == 0) size = 1;

        RenderUtil.enableScale(1, size);
        if (categoryShowing || size != 0) {
            RenderUtil.enableScale(1, size);

            // store the category y, im dumb sowwy
            startY = y;

            // sort children into length order
            category.sons.sort(Comparator.comparingDouble(setting -> -FontUtil.getStringWidth(formatSettingName(setting.name), 1)));

            // adds last scrolled amount + smooths scrolling
            doScrollSmoothing();

            // loop through children
            for (Config setting : category.sons) {
                ClickGUIScreenMain.setting = setting;

                //yOffset = categoryHeights.get(index);
                //y = startY + yOffset + scrollOffset;
                settingName = formatSettingName(setting.name);

                doMouseStuff();

                drawButton();

                // draws cool outline
                RenderUtil.drawRect(x, 0, outlineWidth, height+outlineYoffset, new Color(color));
                RenderUtil.drawRect(x+width-outlineWidth, 0, outlineWidth, height+outlineYoffset, new Color(color));

                // draw children
                if (Children.shouldShow(setting) || getSize(setting) != 0) {
                    // lol im sorry about this, idk why its fucked without translating the height before and after.
                    RenderUtil.enableTranslate(0, height);
                    Children.draw(setting);
                    RenderUtil.enableTranslate(0, -height);
                }
                if (setting.type == Setting.Type.CONFIG) ConfigSettings.draw();

                RenderUtil.enableTranslate(0, height);
            }

            RenderUtil.disableScale();
        }
    }

    private static void doScrollSmoothing() {
        float scrollDif = ((categoryScrollAmount[index] - categoryScrollAmount2[index]) / 10f) * deltaTime/10f;
        categoryScrollAmount[index] += scrollDif;
        categoryScrollAmount2[index] += scrollDif * 1.5f;

        categoryScrollAmount[index] = Math.min(0, categoryScrollAmount[index]);

        scrollOffset = categoryScrollAmount[index];
    }


    private static void doMouseStuff() {
        if (setting.type == Setting.Type.CONFIG) {
            if ((boolean) setting.get(Boolean.class)) {
                setting.set(false);

                String newFileName = getConfigFileName();

                try {
                    Files.createFile(Paths.get(Config.configsFileBase + "/" + newFileName));
                } catch (IOException e) {
                    ChatUtil.chat("epic fail, i hate everything");
                }
            }
        }


        // if hovering over button
        if (isHovered(mX, mY, 0, y, width, height)) {

            // if left clicked on it.
            if (isLeftButtonPressed()) {

                // checks if already animating
                if (!settingsPressed.contains(setting)) {
                    settingsPressed.add(setting);

                    if ((boolean) setting.get(Boolean.class)) settingsPressedInfo.add(new float[]{mX, mY, width});
                    else settingsPressedInfo.add(new float[]{mX, mY, 1});
                }

                // toggles on/off state
                setting.set(!(boolean) setting.get(Boolean.class));
            }

            // if left clicked on it.
            if (isRightButtonPressed()) {
                // toggles on/off state
                if (!settingsPressedRight.contains(setting)) {
                    startGrowing(setting, 2);
                    settingsPressedRight.add(setting);
                }
                else {
                    startGrowing(setting, -2);
                    settingsPressedRight.remove(setting);
                }
            }
        }
    }

    private static String getConfigFileName() {
        boolean found = true;
        int num = 0;
        String newFileName = "new config (1).json";
        while (found) {
            found = false;
            for (File file : Objects.requireNonNull(Config.configsFileBase.listFiles())) {
                if (file.getName().equals(newFileName)) {
                    num++;
                    newFileName = "new config (" + num + ").json";
                    found = true;
                }
            }
        }
        return newFileName;
    }

    static void drawButton() {
        // draw background
        RenderUtil.drawRect(x, y, width, height, getBackgroundColor(category));

        // draw button
        RenderUtil.drawRect(x + xOff, y, width - wOff, height, getBackgroundColor(setting));


        // draws the box filling
        if (settingsPressed.contains(setting)) {
            float[] clickedInfo = settingsPressedInfo.get(settingsPressed.indexOf(setting));
            float startmX = (clickedInfo[0] - RenderUtil.getTranslate()[0]);
            float startmY = (clickedInfo[1] - RenderUtil.getTranslate()[1]);
            float startWidth = clickedInfo[2];

            if ((boolean) setting.get(Boolean.class)) startWidth += ((startWidth * 1.1f) * deltaTime / 120);
            else startWidth -= ((startWidth * 1.1f) * deltaTime / 120);

            settingsPressedInfo.set(settingsPressed.indexOf(setting), new float[]{clickedInfo[0], clickedInfo[1], startWidth});

            RenderUtil.enableScissor(x + xOff, y, width - wOff, height);
            RenderUtil.drawRoundedRect(startmX - startWidth / 2f, startmY - startWidth / 2f, startWidth, startWidth, startWidth / 2f, new Color(color));
            RenderUtil.disableScissor();

            // finished growing
            if (startWidth >= width*2 || startWidth <= 1) {
                settingsPressedInfo.remove(settingsPressed.indexOf(setting));
                settingsPressed.remove(setting);
            }
        }

        // draws a filled colored box
        else if ((boolean) setting.get(Boolean.class))
            RenderUtil.drawRect(x + xOff, y, width - wOff, height, new Color(color));

        // draw text
        RenderUtil.drawStringCenteredY("&o" + settingName, x + width - stringIndentLeft - FontUtil.getStringWidth(settingName, fontSize), y + (height / 2f), fontSize, new Color(0xEEEEEE));
    }

    public static void drawOutline() {
        drawOutline(0);
    }
    public static void drawOutline(float y) {
        // draws cool outline
        RenderUtil.drawRect(x, y, outlineWidth, height+outlineYoffset, new Color(color));
        RenderUtil.drawRect(x+width-outlineWidth, y, outlineWidth, height+outlineYoffset, new Color(color));
    }
}
