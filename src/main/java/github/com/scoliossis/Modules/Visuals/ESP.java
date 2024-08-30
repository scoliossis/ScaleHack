package github.com.scoliossis.Modules.Visuals;

import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import github.com.scoliossis.Util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ESP {
    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Configs.esp || !Configs.glowESP) return;

        for (Entity ent : C.w().loadedEntityList) {
            if (!shouldESP(ent)) continue;

            float x = (float) (ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * event.partialTicks);
            float y = (float) (ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * event.partialTicks);
            float z = (float) (ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * event.partialTicks);

            float opacity1 = 70;
            float opacity2 = 140;


            // just makes size change slightly when rotating, looks cooler
            float sizeMulti = 2;
            opacity1 /= sizeMulti;
            opacity2 /= sizeMulti;

            // circle on head
            RenderUtil.drawCool3dGlow(x, y+1.2f, z, 0.5f*sizeMulti, 0.5f*sizeMulti, event.partialTicks, new Color(32, 245, 250, (int)opacity1), false);

            // chest
            RenderUtil.drawCool3dGlow(x, y+0.2f, z, sizeMulti, sizeMulti, event.partialTicks, new Color(32, 245, 250, (int)opacity2), false);

            // legs
            RenderUtil.drawCool3dGlow(x, y-0.2f*sizeMulti, z, 0.7f*sizeMulti, 0.7f*sizeMulti, event.partialTicks, new Color(32, 245, 250, (int)opacity1), false);
        }
    }

    boolean chamsEnabled = true;

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (chamsEnabled && !shouldESP(event.entity)) {
            chamsEnabled = false;
            disableChams();
            RenderUtil.glColor(new Color(0xffffffff));
        }

        if (!Configs.chams || !Configs.esp || !shouldESP(event.entity)) return;

        enableChams();
        if (Configs.ghostChams) RenderUtil.glColor(new Color(255, 255, 255, 70));
        //setColor(new Color(32, 245, 250, 70));

        chamsEnabled = true;
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Post<EntityLivingBase> event) {
        if (chamsEnabled) {
            chamsEnabled = false;
            disableChams();
            RenderUtil.glColor(new Color(0xffffffff));
        }
    }

    public static void enableChams() {
        if (Configs.chamsMode == 0) {
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1, -1000000);
        }
        else GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    public static void disableChams() {
        if (Configs.chamsMode == 0) {
            GlStateManager.doPolygonOffset(1, 1000000);
            GlStateManager.disablePolygonOffset();
        }
        else GL11.glEnable(GL11.GL_DEPTH_TEST);
    }


    public static boolean shouldESP(Entity entity) {
        return entity instanceof EntityPlayer && entity != C.p();
    }
}
