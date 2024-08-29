package github.com.scoliossis.Modules.Combat;

import github.com.scoliossis.Events.MotionEvent;
import github.com.scoliossis.Events.RotationEvent;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Killaura {
    boolean isBlocking;

    int switchTarget = 0;


    EntityLivingBase lastTarget;

    RotationUtil.Rotation lastRots = new RotationUtil.Rotation(0,0);

    private EntityLivingBase getTarget() {
        if (C.mc.currentScreen instanceof GuiContainer || !C.isInGame()) return null;

        List<Entity> validTargets = C.w().getLoadedEntityList().stream().filter(entity -> entity instanceof EntityLivingBase).filter(entity -> isValid((EntityLivingBase) entity)).collect(Collectors.toList());
        switch (Configs.killAuraSorting) {
            case 0:
                validTargets.sort(Comparator.comparingDouble(e -> e.getDistanceToEntity(C.p())));
                break;
            case 1:
                validTargets.sort(Comparator.comparing(e -> e.hurtResistantTime));
                break;
            case 2:
                validTargets.sort(Comparator.comparing(e -> ((EntityLivingBase)e).getHealth()));
                break;
        }
        if (!validTargets.isEmpty()) {
            if (switchTarget >= validTargets.size()) {
                switchTarget = 0;
            }
            switch (Configs.killAuraTarget) {
                // switch, best, single
                case 0:
                    return (EntityLivingBase)validTargets.get(switchTarget);
                case 1:
                    return (EntityLivingBase)validTargets.get(0);
                case 2:
                    if (lastTarget != null && validTargets.contains(lastTarget)) return lastTarget;
                    return (EntityLivingBase)validTargets.get(0);
            }
        }
        return null;
    }

    private boolean isValid(EntityLivingBase entity) {
        if (C.p() == null || entity == null) return false;

        return entity != C.p() && !(entity instanceof EntityArmorStand) &&
                entity.getDistanceToEntity(C.p()) < Configs.killAuraRotationRange;
    }

    public Entity isLookingAtEntity(RotationUtil.Rotation rotation, float reach) {
        return RotationUtil.getMouseOver(reach, rotation.pitch, rotation.yaw);
    }

    private boolean shouldHit(EntityLivingBase entity, RotationUtil.Rotation rotation) {
        if (!isValid(entity)) return false;

        Entity entityHit = isLookingAtEntity(rotation, C.p().canEntityBeSeen(entity) ? Configs.killAuraAttackRange : Configs.killAuraWallsRange);
        boolean holdingSword = (C.p().getHeldItem() != null && C.p().getHeldItem().getItem() instanceof ItemSword) || !Configs.swordOnlyAura;


        return entityHit != null && holdingSword;
    }
    private boolean canAttack(EntityLivingBase entity, RotationUtil.Rotation rotation) {
        return attacks > 0 && shouldHit(entity, rotation);
    }

    @SubscribeEvent
    public void onRotationEvent(RotationEvent event) {
        EntityLivingBase target = getTarget();
        lastTarget = target;

        if (lastTarget == null || !Configs.killAura) {
            lastRots = null;
            attacks = 0;
            lastAttack = System.currentTimeMillis();
            return;
        }

        RotationUtil.Rotation neededRots = RotationUtil.getRotation(RotationUtil.getPlayerEyes(target));
        RotationUtil.Rotation newRots = neededRots;

        switch (Configs.rotations) {
            case 0:
                newRots = RotationUtil.getSimpleRotation(lastRots, neededRots, MathUtil.getRandomInRange(Configs.minRotation, Configs.maxRotation));

                event.yaw = newRots.yaw;
                event.pitch = newRots.pitch;
                break;

            case 1:
                newRots = RotationUtil.getSmoothRotation(lastRots, neededRots, Configs.smoothRotationSpeed);

                event.yaw = newRots.yaw;
                event.pitch = newRots.pitch;
                break;
        }


        RotationUtil.Rotation rot = new RotationUtil.Rotation(newRots.pitch, newRots.yaw);
        if (canAttack(target, rot)) {
            Entity entityHit = isLookingAtEntity(rot, C.p().canEntityBeSeen(target) ? Configs.killAuraAttackRange : Configs.killAuraWallsRange);

            if (Configs.rotations == 2) {
                event.yaw = newRots.yaw;
                event.pitch = newRots.pitch;
            }

            C.p().swingItem();
            if (Configs.auraKeepSprint)
                PacketUtil.sendPacketNoEvent(new C02PacketUseEntity(entityHit, C02PacketUseEntity.Action.ATTACK));
            else C.mc.playerController.attackEntity(C.p(), entityHit);

            switchTarget++;
            attacks--;
        }
    }

    private long lastAttack;
    private int nextCps = 1;
    int attacks = 0;

    // the fastest event
    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        if (!shouldHit(lastTarget, lastRots) && Configs.rotations < 2) {
            attacks = 0;
            lastAttack = System.currentTimeMillis();
        }

        if (Configs.killAura && System.currentTimeMillis() - lastAttack > (1000 / nextCps) && isValid(lastTarget)) {
            nextCps = (int) MathUtil.getRandomInRange(Configs.killAuraAttackSpeedMin, Configs.killAuraAttackSpeedMax);
            lastAttack = System.currentTimeMillis();
            attacks++;
        }
    }



    @SubscribeEvent
    public void onRotationEvent(MotionEvent.Post event) {
        lastRots = new RotationUtil.Rotation(event.pitch, event.yaw);
    }


    private void startBlocking() {
        PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(C.p().getHeldItem()));
        isBlocking = true;
    }

    private void stopBlocking() {
        if (isBlocking) {
            PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            isBlocking = false;
        }
    }
}
