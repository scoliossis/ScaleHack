package github.com.scoliossis.Modules.Visuals;

import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Zoom {

    public boolean zoomed;
    public float fov = 0;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Configs.zoom && !zoomed) {
            fov = C.mc.gameSettings.fovSetting;

            zoomed = true;

            C.mc.gameSettings.fovSetting = 10;
            C.mc.gameSettings.smoothCamera = true;
        }
        if (!Configs.zoom && zoomed) {
            zoomed = false;

            C.mc.gameSettings.fovSetting = fov;
            C.mc.gameSettings.smoothCamera = false;
        }
    }
}
