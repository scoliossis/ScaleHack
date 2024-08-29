package github.com.scoliossis.Modules.Visuals;

import github.com.scoliossis.Main;
import github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import github.com.scoliossis.Util.FontUtil;
import github.com.scoliossis.Util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

public class HUD {
    @SubscribeEvent
    public void onRender2D(TickEvent.RenderTickEvent event) {
        if (!C.isInGame() || !Configs.HUD || C.mc.currentScreen instanceof ClickGUIScreenMain) return;

        String clientGamesenseName = "ScaleSense";
        String hudText = clientGamesenseName + " " + Main.VERSION + " | " + Minecraft.getDebugFPS() + " fps | " + (C.mc.getCurrentServerData() != null ? C.mc.getCurrentServerData().serverIP : "singleplayer");
        float width = FontUtil.getStringWidth(hudText, 0.1f);
        float height = 16;

        float baseX = 5;
        float baseY = 5;

        RenderUtil.drawString("&o"+hudText, 12, 13, 0.1f, new Color(255,255,255));
        RenderUtil.drawRect(baseX, baseY, width+10, height+10, new Color(60, 60, 60));
        RenderUtil.drawRect(baseX+1, baseY+1, width+8, height+8, new Color(40, 40, 40));
        RenderUtil.drawRect(baseX+3, baseY+3, width+4, height+4, new Color(60, 60, 60));
        RenderUtil.drawRect(baseX+4, baseY+4, width+2, height+2, new Color(22, 22, 22));

        RenderUtil.drawFade(9, 10, width+2, 1, RenderUtil.rainbowColors, 2, 3f);

        FontUtil.renderStrings();
    }
}
