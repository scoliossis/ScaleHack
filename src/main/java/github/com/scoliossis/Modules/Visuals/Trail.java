package github.com.scoliossis.Modules.Visuals;

import github.com.scoliossis.Events.MotionEvent;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import github.com.scoliossis.Util.RenderUtil;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;

public class Trail {

    ArrayList<Vec3> poses = new ArrayList<>();

    @SubscribeEvent
    public void onMotionPost(MotionEvent.Post event) {
        //if (C.p().posX != C.p().lastTickPosX || C.p().posZ != C.p().lastTickPosZ || C.p().posY != C.p().lastTickPosY)
            poses.add(C.p().getPositionVector());

        while (poses.size() > Configs.trailLength) poses.remove(0);
    }

    @SubscribeEvent
    public void onRender3d(RenderWorldLastEvent event) {
        if (!Configs.trail) return;

        for (Vec3 pos : poses)
            RenderUtil.drawCool3dGlow(pos.xCoord, pos.yCoord, pos.zCoord, 0.2f, 0.2f, event.partialTicks, RenderUtil.getColorsFade((pos.xCoord+pos.zCoord)*200, RenderUtil.rainbowColors, 5), true);
    }
}
