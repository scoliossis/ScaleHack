package github.com.scoliossis.Modules.Visuals;

import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.RenderUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class Nametags {
    @SubscribeEvent
    public void onRenderEntity(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (!Configs.nameTags || !(event.entity instanceof EntityPlayer)) return;

        event.setCanceled(true);
        RenderUtil.renderNametag(event.entity, event, new Color(22,22,22,100),true);
    }
}
