package github.com.scoliossis.Util;

import net.minecraft.util.EnumFacing;

public class TeleportUtil {
    public static void hClip(double distance) {
        EnumFacing moveDirection = EnumFacing.fromAngle(MovementUtil.getMovementYaw());

        double xCoord = -Math.sin(moveDirection.getHorizontalIndex() * 90f * Math.PI / 180) * distance;
        double zCoord = Math.cos(moveDirection.getHorizontalIndex() * 90f * Math.PI / 180) * distance;

        // slightly easier to type
        offsetTP(xCoord, 0, zCoord);
    }

    public static void offsetTP(double x, double y, double z) {
        C.p().setPosition(C.p().posX+x, C.p().posY+y, C.p().posZ+z);
    }

    public static void tp(double x, double y, double z) {
        C.p().setPosition(x, y, z);
    }
}
