package github.com.scoliossis.Screens.ClickGUI;

import github.com.scoliossis.Main;
import github.com.scoliossis.Screens.ClickGUI.Buttons.Category;
import github.com.scoliossis.Screens.ClickGUI.Buttons.Module;
import github.com.scoliossis.Screens.ClickGUI.Buttons.SubSettings.KeybindSetting;
import github.com.scoliossis.Screens.ClickGUI.Settings.Config;
import github.com.scoliossis.Screens.ClickGUI.Settings.Setting;
import github.com.scoliossis.Util.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

// all the drawing code is split across multiple files because i can NOT write clean code.
// 5000+ line clickgui type shi
public class ClickGUIScreenMain extends GuiScreen {
    public ClickGUIScreenMain() {

    }

    public static boolean leftMouseDown = false;
    public static boolean leftMousePressed = false;

    public static boolean rightMouseDown = false;
    public static boolean rightMousePressed = false;

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);

        if (button == 0) {
            leftMouseDown = true;
            leftMousePressed = true;
        }
        else if (button == 1) {
            rightMouseDown = true;
            rightMousePressed = true;
        }
    }

    @Override
    protected void mouseReleased(int x, int y, int button) {
        super.mouseReleased(x, y, button);

        if (button == 0) {
            leftMouseDown = false;
            leftMousePressed = false;
        }
        else if (button == 1) {
            rightMouseDown = false;
            rightMousePressed = false;
        }
    }

    public static Config draggingSetting = null;
    public static Config lastDraggedSetting = null;


    public static float[] categoryScrollAmount = new float[Config.getCategorys(Main.Settings)];
    public static float[] categoryScrollAmount2 = new float[Config.getCategorys(Main.Settings)];

    public static float startTrans;


    // idk what deltatime is i just remember using it to make movement the same speed no matter the fps
    public static long deltaTime = 0;
    public static long lastTime = 0;



    // variables that are set before a draw function is called so the draw function doesnt need any input things
    public static Config setting, subSetting, category;
    public static String settingName;


    // i dont usually use , to define a bunch of variables, but this would take too many lines
    public static float x, y,
            baseX, baseY;

    public static float width = 140;
    public static float height = 18;

    public static float fontSize = 0.1f;

    public static float categoryHeight = 20;
    public static float outlineWidth = 1.5f;
    public static int outlineYoffset = 4;


    public static int mX, mY,
        index,
        scrolledAmount;


    public static int color;

    public static boolean categoryShowing;

    public static ScaledResolution sr = new ScaledResolution(C.mc);


    static long time = 0;
    // DRAWING
    @Override
    public void drawScreen(int mX, int mY, float partialTicks) {
        time = System.currentTimeMillis();

        ClickGUIScreenMain.mX = mX;
        ClickGUIScreenMain.mY = mY;


        // delta time my beloved
        deltaTime = System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();

        // how far scrolled since last tick
        scrolledAmount = Mouse.getDWheel();

        // re-set scaledresolution incase of screen resize
        sr = new ScaledResolution(C.mc);

        // draws background
        RenderUtil.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0,0,0, 120));


        // sort by length! why not!
        Main.Settings.sort(Comparator.comparingDouble(setting -> -FontUtil.getStringWidth(formatSettingName(setting.name), 1)));

        //super.drawScreen(mX, mX, partialTicks);

        // loop through categorys!
        //.sorted(Comparator.comparing(e -> e == lastDraggedSetting)) <- setting being dragged always renders on top
        for (Config category : Main.Settings.stream().filter(settings -> settings.type == Setting.Type.CATEGORY).sorted(Comparator.comparing(e -> e == lastDraggedSetting)).collect(Collectors.toList())) {
            ClickGUIScreenMain.category = category;

            // ready to rumble
            RenderUtil.push();

            // draws the category + does functions such as dragging and clicking
            Category.draw();

            // draws all children of the category.
            height = 15;
            Module.draw();

            finishDrawingCategory();
            RenderUtil.pop();
        }



        // reset mouse pressed state.
        resetPressStates();
    }

    private static void finishDrawingCategory() {
        // draws the bottom line
        RenderUtil.drawRect(x+outlineWidth, y+outlineYoffset-outlineWidth-3, width-outlineWidth*2, 3, new Color(26,26,26));
        RenderUtil.drawRect(x, y+outlineYoffset-outlineWidth, width, outlineWidth, new Color(color));

        // adds scrolled amount if hovered
        if (isHoveredSimple(mX, mY, baseX, baseY, width, RenderUtil.getTranslate()[1] - baseY)) {
            categoryScrollAmount2[index] -= scrolledAmount * (deltaTime/50f);
            scrolledAmount = 0;
        }

        // makes sure u dont scroll too far down / up
        categoryScrollAmount[index] = Math.min(0, categoryScrollAmount[index]);
        if (categoryScrollAmount[index] < -(RenderUtil.getTranslate()[1]-startTrans-height)) {
            categoryScrollAmount[index] = Math.max(-(RenderUtil.getTranslate()[1]-startTrans), categoryScrollAmount[index]);
            categoryScrollAmount[index] += deltaTime/10f;
        }


        // draw all text with transformations
        FontUtil.renderStrings();
    }


    // functions to compact code!
    private void resetPressStates() {
        leftMousePressed = false;
        rightMousePressed = false;

        if (!leftMouseDown)
            draggingSetting = null;

        KeybindSetting.lastKey = -1;
        KeybindSetting.lastChar = '\u0000';
    }


    // sets left button pressed to false so you cant press multiple buttons at once
    public static boolean isLeftButtonPressed() {
        boolean h = leftMousePressed;
        leftMousePressed = false;
        return h;
    }

    // sets right button pressed to false so you cant press multiple buttons at once
    public static boolean isRightButtonPressed() {
        boolean h = rightMousePressed;
        rightMousePressed = false;
        return h;
    }

    @Override
    public boolean doesGuiPauseGame() {return false;}

    @Override
    public void keyTyped(char aChar, int key) {
        // close when escape pressed.
        if (key == 1 && KeybindSetting.currentKeybind == null) {
            Config.save(Config.getLastLoadedConfig());
            mc.thePlayer.closeScreen();
        }

        // cast character to string + make character upper case
        KeybindSetting.lastKey = key;
        KeybindSetting.lastChar = Character.toUpperCase(aChar);
    }


    public static String formatSettingName(String settingName) {
        return settingName;
        //return settingName.toLowerCase().replaceAll(" ", "");
    }

    public static Color getBackgroundColor(Config setting) {
        return getBackgroundColorByInt(getParentsNumber(setting));
    }

    public static Color getBackgroundColorByInt(int parents) {
        switch (parents) {
            case 0:
            case 2:
                return new Color(26,26,26);
            case 1:
                return new Color(37, 37, 37);
            case 3:
                return new Color(20, 20, 20);
            default:
                return new Color(10, 10, 10);
        }
    }

    public static int getParentsNumber(Config setting) {
        int parents = 0;

        while (setting.parent != null) {
            setting = setting.parent;
            parents++;
        }

        return parents;
    }

    public static boolean isHovered(float mX, float mY, double x, double y, double w, double h) {
        float[] pointsXY = RenderUtil.scalePoints(x, y);

        // checks if mouseX/Y is within rectangle bounds
        return mX >= pointsXY[0] && mX <= pointsXY[0] + w && mY >= pointsXY[1] && mY <= pointsXY[1] + h;
    }


    public static boolean isHoveredSimple(float mX, float mY, double x, double y, double w, double h) {
        // checks if mouseX/Y is within rectangle bounds
        return mX >= x && mX <= x + w && mY >= y && mY <= y + h;
    }

    // cant use records, no java 16 :(
    public static class GrowingSetting {
        float speed;
        long time;
        float startSize;

        public GrowingSetting(float speed, long time, float startSize) {
            this.speed = speed;
            this.time = time;
            this.startSize = startSize;
        }
    }

    public static HashMap<Config, GrowingSetting> growingSettings = new HashMap<>();
    // the sequel or smth
    public static HashMap<Config, GrowingSetting> growingSettings2 = new HashMap<>();

    public static void startGrowing(Config setting, float speed) {
        startGrowing(setting,speed,growingSettings);
    }

    public static void startGrowing(Config setting, float speed, HashMap<Config, GrowingSetting> map) {
        float startSize = 0.01f;

        GrowingSetting currentSize = map.get(setting);
        if (currentSize != null) {
            float size = getSize(setting);
            map.remove(setting);

            map.put(setting, new GrowingSetting(speed, System.currentTimeMillis(), size));
        }
        else {
            if (speed < 0) startSize = 1;

            map.put(setting, new GrowingSetting(speed, System.currentTimeMillis(), startSize));
        }
    }

    public static float getSize(Config setting) {
        return getSize(setting, growingSettings);
    }

    public static float getSize(Config setting, HashMap<Config, GrowingSetting> map) {
        GrowingSetting currentSize = map.get(setting);

        if (currentSize == null) return 0;

        float timePassed = (float) (time - currentSize.time);
        timePassed*=timePassed;
        timePassed /= 100000f;

        float newSize = currentSize.speed * timePassed;
        newSize += currentSize.startSize;

        if (newSize < 0 || newSize > 1) {
            map.remove(setting);
            return Math.min(1, Math.max(0, newSize));
        }

        return newSize;
    }


}