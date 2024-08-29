package github.com.scoliossis.Util;

public class MovementUtil {
    public static int airTicks = 0;

    public static float getMovementYaw() {
        return getMovementYaw(C.p().rotationYaw);
    }

    // pasted from mushroom
    public static float getMovementYaw(float playerYaw) {

        float yaw = (C.p().moveStrafing > 0 ? -90 : (C.p().moveStrafing < 0 ? 90 : 0));
        yaw *= (C.p().moveForward < 0 ? -0.5f : (C.p().moveForward > 0 ? 0.5f : 1f));
        yaw += playerYaw + (C.p().moveForward < 0 ? 180 : 0);

        // could be 1 line but its way too cramped. built in obfuscation type shit
        // (C.p().rotationYaw + (C.p().moveForward < 0 ? 180 : 0)) + ((C.p().moveStrafing > 0 ? -90 : (C.p().moveStrafing < 0 ? 90 : 0)) * ((C.p().moveForward < 0 ? -0.5f : (C.p().moveForward > 0 ? 0.5f : 1f))));

        return yaw;
    }

    // pasted from mushroom
    public static void setMotion(double speed) {
        double forward = C.p().moveForward;
        double strafe = C.p().moveStrafing;

        float yaw = C.p().rotationYaw;


        if (forward != 0.0) {
            if (strafe > 0.0) yaw += ((forward > 0.0) ? -45 : 45);
            else if (strafe < 0.0) yaw += ((forward > 0.0) ? 45 : -45);
            strafe = 0.0;

            forward = forward > 0 ? 1 : -1;

        }

        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));

        C.p().motionX = forward * speed * cos + strafe * speed * sin;
        C.p().motionZ = forward * speed * sin - strafe * speed * cos;
    }
}
