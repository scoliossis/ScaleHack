package github.com.scoliossis.Screens.ClickGUI.Buttons;

import github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain;
import github.com.scoliossis.Screens.ClickGUI.Settings.Config;
import github.com.scoliossis.Util.ChatUtil;
import github.com.scoliossis.Util.RenderUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain.*;

// categorys like combat!
public class Category {

    public static void draw() {
        // rotating + scaling!
        doCategoryDragging();

        // set positions n shi
        setPosNshi();

        // clicking shi
        doCategoryClicking();

        // drawing category!
        drawCategory();


        RenderUtil.enableScissor(0, categoryHeight-2, width, sr.getScaledHeight());

        RenderUtil.enableTranslate(0, categoryHeight-2 + categoryScrollAmount[index]);
        startTrans = RenderUtil.getTranslate()[1];

    }



    // used for dragging categorys
    static float mouseXoffset = 0;
    static float mouseYoffset = 0;
    static float mouseMovedAmountX = 0;
    static float mouseMovedAmountY = 0;

    static void setPosNshi() {
        // get info about category pos n set some variables
        Integer[] basePos = (Integer[]) Config.highestParent(category).get(Integer[].class);

        index = basePos[2];

        color = basePos[3];
        categoryShowing = basePos[4] == 1;

        baseX = basePos[0];
        baseY = basePos[1];

        RenderUtil.enableTranslate(basePos[0], basePos[1]);

        settingName = ClickGUIScreenMain.formatSettingName(category.name);
    }


    static void doCategoryClicking() {
        // if hovering over the category title
        if (isHovered(mX, mY, 0, 0, width, height)) {
            // if not already dragging, hovering over setting and left mouse down, start dragging.
            if (draggingSetting == null && leftMouseDown) {
                // set setting
                draggingSetting = category;
                lastDraggedSetting = draggingSetting;

                // set mouse offsets
                mouseXoffset = mX - baseX;
                mouseYoffset = mY - baseY;
            }

            // ATTACK! (closes category)
            if (isRightButtonPressed()) category.toggleCategory();
        }

        // if dragging, sets the pos to your mouse coord.
        if (draggingSetting == category) {
            // increase rotation amount
            mouseMovedAmountX += (baseX - (mX - mouseXoffset)) / 10f;

            // increase compression amount
            mouseMovedAmountY -= (baseY - (mY - mouseYoffset)) / 500f;

            // set x and y
            //x = mX - mouseXoffset;
            //y = mY - mouseYoffset;

            category.set(new Integer[]{(int) (mX - mouseXoffset), (int) (mY - mouseYoffset), index, color, categoryShowing ? 1 : 0});
        }
    }

    static void doCategoryDragging() {
        if (category == lastDraggedSetting) {
            mouseMovedAmountX = Math.min(80, Math.max(-80, mouseMovedAmountX));
            mouseMovedAmountY = Math.min(0.5f, Math.max(-0.5f, mouseMovedAmountY));

            // fucked smth up, lazy
            //GL11.glRotatef(-mouseMovedAmountX, 0, 0, 1f);

            RenderUtil.enableScale(1, 1-mouseMovedAmountY);

            // slowly decrease rotation amount, decreases slower and slower until almost 0
            mouseMovedAmountX-=((mouseMovedAmountX*1.1f)*deltaTime/70);
            mouseMovedAmountY-=((mouseMovedAmountY*1.1f)*deltaTime/70);
        }
    }

    static void drawCategory() {
        // background
        RenderUtil.drawRect(x, y, width, categoryHeight, getBackgroundColor(category));

        // actual box + title
        RenderUtil.drawRect(x, y, width, categoryHeight, new Color(26,26,26));
        RenderUtil.drawStringCenteredY("&l&o"+settingName, x + 5, y + (categoryHeight/2f), fontSize, new Color(0xffffff));

        // outline at top
        RenderUtil.drawRect(x, y, width, outlineWidth, new Color(color));

        // draws cool outline on sides
        RenderUtil.drawRect(x, y, outlineWidth, categoryHeight+outlineYoffset, new Color(color));
        RenderUtil.drawRect(x+width-outlineWidth, y, outlineWidth, categoryHeight+outlineYoffset, new Color(color));

        //categoryHeights.set(index, height);


        // draws bigger bottom line bar
        //float yOf = categoryHeights.get(index);
        RenderUtil.drawRect(x, y + categoryHeight, width, outlineWidth + 3, new Color(color));
    }

}
