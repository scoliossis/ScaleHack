package github.com.scoliossis.Modules.Movement;

import github.com.scoliossis.Modules.Player.MovementFix;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;

// see EntityMixin, EntityPlayerSPMixin and EntityLivingBaseMixin
// changes isSprinting, setSprinting and jump functions
public class Sprint {
    public static boolean shouldSprint() {
        return Configs.sprint;
    }

    public static boolean shouldSprint2() {
        return (C.p().moveForward != 0 || C.p().moveStrafing != 0);
    }

    public static boolean sprintOverride() {
        return !MovementFix.overrideSprint;
    }
}
