package github.com.scoliossis.Util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.*;

import java.util.List;

public class RotationUtil {
    public static MovingObjectPosition rayTrace(Vec3 from, double distance, float yaw, float pitch) {
        final float n4 = -yaw * 0.017453292f;
        final float n5 = -pitch * 0.017453292f;
        final float cos = MathHelper.cos(n4 - 3.1415927f);
        final float sin = MathHelper.sin(n4 - 3.1415927f);
        final float n6 = -MathHelper.cos(n5);
        final Vec3 vec3 = new Vec3(sin * n6, MathHelper.sin(n5), cos * n6);

        return C.w().rayTraceBlocks(from, from.addVector(vec3.xCoord * distance, vec3.yCoord * distance, vec3.zCoord * distance), false, false, false);
    }

    public static MovingObjectPosition rayTrace(double distance, float yaw, float pitch) {
        return rayTrace(getPlayerEyes(C.p()), distance, yaw, pitch);
    }

    public static MovingObjectPosition rayTrace(float pitch, float yaw) {
        final Vec3 vec3 = C.p().getPositionEyes(1);
        final Vec3 vec4 = getVectorForRotation(pitch, yaw);
        final Vec3 vec5 = vec3.addVector(vec4.xCoord * C.mc.playerController.getBlockReachDistance(), vec4.yCoord * C.mc.playerController.getBlockReachDistance(), vec4.zCoord * C.mc.playerController.getBlockReachDistance());
        return C.w().rayTraceBlocks(vec3, vec5);
    }


    public static Entity getMouseOver(float reach, float pitch, float yaw) {
        float partialTicks = 1;

        Entity pointedEntity;

        MovingObjectPosition objectMouseOver;
        Entity entity = C.p();

        if (entity != null && C.w() != null) {
            //C.mc.mcProfiler.startSection("pick");
            double d0 = (double)reach;
            //objectMouseOver = rayTrace(d0, yaw, pitch);
            // if the first number is > 0 it wont hit through walls
            objectMouseOver = C.p().rayTrace(0, 1);

            double d1 = d0;
            Vec3 vec3 = entity.getPositionEyes(partialTicks);
            boolean flag = false;

            // thanks intellij
            //int i = true;

            if (C.mc.playerController.extendedReach()) {
                d0 = 6.0;
                d1 = 6.0;
            } else if (d0 > reach) {
                flag = true;
            }

            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }

            //Vec3 vec31 = entity.getLook(partialTicks);
            Vec3 vec31 = getVectorForRotation(pitch, yaw);


            Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            pointedEntity = null;
            Vec3 vec33 = null;
            float f = 1.0F;
            List<Entity> list = C.w().getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double)f, (double)f, (double)f), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
                public boolean apply(Entity p_apply_1_) {
                    return p_apply_1_.canBeCollidedWith();
                }
            }));
            double d2 = d1;

            for(int j = 0; j < list.size(); ++j) {
                Entity entity1 = (Entity)list.get(j);
                float f1 = entity1.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)f1, (double)f1, (double)f1);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (d2 >= 0.0) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0;
                    }
                } else if (movingobjectposition != null) {
                    double d3 = vec3.distanceTo(movingobjectposition.hitVec);
                    if (d3 < d2 || d2 == 0.0) {
                        if (entity1 == entity.ridingEntity && !entity.canRiderInteract()) {
                            if (d2 == 0.0) {
                                pointedEntity = entity1;
                                vec33 = movingobjectposition.hitVec;
                            }
                        } else {
                            pointedEntity = entity1;
                            vec33 = movingobjectposition.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }

            if (pointedEntity != null && flag && vec3.distanceTo(vec33) > 3.0) {
                pointedEntity = null;
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, (EnumFacing)null, new BlockPos(vec33));
            }

            //C.mc.mcProfiler.endSection();

            return pointedEntity;
        }

        return null;
    }


    // stolen from minecraft (and i dont even use it anywhere)
    public static Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }


    public static float wrapAngleTo180f(double angle) {
        return (float) (angle - Math.floor(angle / 360 + 0.5) * 360);
    }

    public static double wrapAngleTo180(double angle) {
        return angle - Math.floor(angle / 360 + 0.5) * 360;
    }

    public static Rotation getRotation(Vec3 from, Vec3 to) {
        double diffX = to.xCoord - from.xCoord;
        double diffY = to.yCoord - from.yCoord;
        double diffZ = to.zCoord - from.zCoord;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float pitch = (float) -Math.atan2(dist, diffY);
        float yaw = (float) Math.atan2(diffZ, diffX);
        pitch = (float) wrapAngleTo180(((pitch * 180F) / Math.PI + 90) * -1);
        yaw = (float) wrapAngleTo180(((yaw * 180) / Math.PI) - 90);

        return new Rotation(pitch, yaw);
    }

    public static Rotation getRotation(Vec3 vec3) {
        return getRotation(getPlayerEyes(C.p()), vec3);
    }

    public static Vec3 getPlayerEyes(EntityLivingBase player) {
        return player.getPositionEyes(1);
    }

    public static float getAngleDifference(float a, float b) {
        return (((((a - b) % 360.0f) + 540.0f) % 360.0f) - 180.0f);
    }

    public static Rotation getSimpleRotation(Rotation currentRotation, Rotation targetRotation, float turnSpeed) {
        float pitchToReturn = currentRotation.pitch + MathHelper.clamp_float(getAngleDifference(targetRotation.pitch, currentRotation.pitch), -turnSpeed, turnSpeed);
        float yawToReturn = currentRotation.yaw + MathHelper.clamp_float(getAngleDifference(targetRotation.yaw, currentRotation.yaw), -turnSpeed, turnSpeed);

        if (Configs.correctGCD) {
            // make int out of number, then multi again, should work idk
            float gcdNum = Configs.correctGCDnumber*Minecraft.getDebugFPS();

            pitchToReturn = ((int) (pitchToReturn * gcdNum)) / gcdNum;
            yawToReturn = ((int) (yawToReturn * gcdNum)) / gcdNum;
        }

        return new Rotation(pitchToReturn, yawToReturn);
    }

    public static Rotation getSmoothRotation(Rotation current, Rotation target, float smooth) {
        float pitchToReturn = current.pitch + (target.pitch - current.pitch) / smooth;
        float yawToReturn = (float) wrapAngleTo180(current.yaw + (target.yaw - current.yaw) / smooth);

        if (Configs.correctGCD) {
            // make int out of number, then multi again, should work idk
            float gcdNum = Configs.correctGCDnumber*Minecraft.getDebugFPS();

            pitchToReturn = ((int) (pitchToReturn * gcdNum)) / gcdNum;
            yawToReturn = ((int) (yawToReturn * gcdNum)) / gcdNum;
        }


        return new Rotation(pitchToReturn, yawToReturn);
    }


    public static class Rotation {
        public float pitch;
        public float yaw;

        public Rotation(float pitch, float yaw) {
            this.pitch = pitch;
            this.yaw = yaw;
        }
    }

}
