package github.com.scoliossis.Screens.MainMenu;

import github.com.scoliossis.Main;
import github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain;
import github.com.scoliossis.Util.C;
import github.com.scoliossis.Util.FontUtil;
import github.com.scoliossis.Util.RenderUtil;
import net.minecraft.client.gui.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class MainMenuMain extends GuiScreen {
    public MainMenuMain() {

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


    String[] strings = {"singleplayer", "multiplayer", "settings", "alts"};

    @Override
    public void drawScreen(int mX, int mY, float partialTicks) {
        drawBackground();
        drawButtons(mX, mY);


        FontUtil.renderStrings();

        leftMousePressed = false;
        rightMousePressed = false;
    }

    public void drawButtons(int mX, int mY) {
        ScaledResolution sr = new ScaledResolution(C.mc);

        int screenWidth = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();


        float w = 300;
        float h = 220;
        float x = screenWidth/2f - w/2;
        float y = screenHeight/2f - h/2;


        String clientNameText = Main.clientName + " " + Main.VERSION;
        float textSize = 0.3f;
        float textW = FontUtil.getStringWidth(clientNameText, textSize);
        float textH = textSize*100 * 1.3f;
        float textX = screenWidth/2f - textW/2;
        float textY = y+10;

        //RenderUtil.drawGlow(textX-10, textY, textW+20, textH, 10, new Color(150,150,150));
        RenderUtil.drawString("&l&o"+clientNameText, textX, textY, textSize, new Color(0xffffffff));

        RenderUtil.drawRoundedRect(x, y, w, h, 5, new Color(10,10,10, 150));

        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];

            float newW = w / 1.3f;
            float newH = h/8;
            float newX = (x + w/2f) - newW/2f;
            float newY = y+newH*(i+1) + i * 10 + 30;

            float newTextSize = 0.15f;
            float newTextW = FontUtil.getStringWidth(string, newTextSize);
            float newTextX = (newX + newW/2f) - newTextW/2f;
            float newTextY = newY+(newH/2)-10f;


            if (ClickGUIScreenMain.isHovered(mX, mY, newX, newY, newW, newH)) {
                RenderUtil.drawString("&s" + string, newTextX, newTextY, newTextSize, new Color(0xffffffff));
                RenderUtil.drawRoundedRect(newX, newY, newW, newH, 1, new Color(22, 22, 22, 200));

                if (leftMousePressed) {
                    switch (i) {
                        case 0:
                            mc.displayGuiScreen(new GuiSelectWorld(this));
                            break;
                        case 1:
                            mc.displayGuiScreen(new GuiMultiplayer(this));
                            break;
                        case 2:
                            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                            break;
                        //case 3:
                        // mc.displayGuiScreen(new AltManagerGUI());
                        // break;
                    }
                }
            }
            else {
                RenderUtil.drawString("&s" + string, newTextX, newTextY, newTextSize, new Color(255,255,255,190));
                RenderUtil.drawRoundedRect(newX, newY, newW, newH, 1, new Color(22, 22, 22, 100));
            }
        }
    }


    public static void drawBackground() {
        ScaledResolution sr = new ScaledResolution(C.mc);
        int screenWidth = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();

        if ((scaryCircle.size() < 60 && System.currentTimeMillis() % 10000 > 5000) || scaryCircle.size() < 5) {
            double xPos = screenWidth * Math.random();
            double yPos = screenHeight * Math.random();

            double xSpeed = (-(screenWidth/2f - xPos) / screenWidth) / 3;
            double ySpeed = (-(screenHeight/2f - yPos) / screenHeight) / 3;

            double sizeMultiplier = Math.random()*3;
            // cool effect
            //double sizeMultiplier = Math.random()/5;

            //xSpeed = Math.max(0.1f, xSpeed);

            scaryCircle.add(new circeProperties(xPos, yPos, xSpeed, ySpeed, sizeMultiplier, System.currentTimeMillis()));
        }


        RenderUtil.drawRect(0,0,screenWidth,screenHeight, new Color(22,22,22));

        for (int i = 0; i < scaryCircle.size(); i++) {
            circeProperties vec = scaryCircle.get(i);


            //float x = (float) vec.x;
            //float y = (float) vec.y;

            float x = (float) (vec.x+((System.currentTimeMillis() - vec.time)*vec.speedX));
            float y = (float) (vec.y+((System.currentTimeMillis() - vec.time)*vec.speedY));
            float size = (float) (((System.currentTimeMillis() - vec.time)*vec.speedSize))/10;
            size = Math.min(700,size);

            x-=size/2f;
            y-=size/2f;


            RenderUtil.drawGlow(x, y, size, size, size, RenderUtil.getColorsFade(x, RenderUtil.rainbowColors, 2, 100));

            if (x > screenWidth || x+size < 0 || y+size < 0 || y > screenHeight) {
                scaryCircle.remove(i);
                i--;
            }
        }
    }

    public static class circeProperties {
        public double x;
        public double y;
        public double speedX;
        public double speedY;
        public double speedSize;
        public long time;

        public circeProperties(double x, double y, double speedX, double speedY, double speedSize, long time) {
            this.x = x;
            this.y = y;

            this.speedX = speedX;
            this.speedY = speedY;
            this.speedSize = speedSize;
            this.time = time;
        }
    }

    public static ArrayList<circeProperties> scaryCircle = new ArrayList<>();
}
