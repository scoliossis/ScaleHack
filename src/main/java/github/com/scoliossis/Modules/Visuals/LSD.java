package github.com.scoliossis.Modules.Visuals;

import com.sun.javafx.geom.Vec2d;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.Vec3f;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import github.com.scoliossis.Util.ChatUtil;
import github.com.scoliossis.Util.MathUtil;
import github.com.scoliossis.Util.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;


public class LSD {
    @SubscribeEvent
    public void onConsumeDrugs(TickEvent.RenderTickEvent event) {
        if (!Configs.LSD) return;
        ScaledResolution sr = new ScaledResolution(C.mc);

        if (scaryCircle.size() < 20) scaryCircle.add(new circeProperties((
                sr.getScaledWidth() * Math.random()) - (sr.getScaledWidth() / 2f),
                (sr.getScaledHeight() * Math.random()) - (sr.getScaledHeight() / 2f),
                Math.random(), System.currentTimeMillis()));

        for (int i = 0; i < scaryCircle.size(); i++) {
            circeProperties vec = scaryCircle.get(i);

            float x = (float) (vec.x+((System.currentTimeMillis() - vec.time)/200f*vec.speed));
            float y = (float) (vec.y+((System.currentTimeMillis() - vec.time)/400f*vec.speed));
            float size = (float) (((System.currentTimeMillis() - vec.time)/1000f)*vec.speed)*200;

            RenderUtil.drawGlow(x, y, size, size, size, RenderUtil.getColorsFade(vec.x, RenderUtil.rainbowColors, 2, 100));

            if (x+size/2f > sr.getScaledWidth()) {
                scaryCircle.remove(i);
            }
        }
    }

    static class circeProperties {
        double x;
        double y;
        double speed;
        long time;

        public circeProperties(double x, double y, double speed, long time) {
            this.x = x;
            this.y = y;

            this.speed = speed;
            this.time = time;
        }
    }

    public static ArrayList<circeProperties> scaryCircle = new ArrayList<>();
}
