package github.com.scoliossis.mixins;

import github.com.scoliossis.Events.MoveFlyingEvent;
import github.com.scoliossis.Events.RotationEvent;
import github.com.scoliossis.Modules.Player.MovementFix;
import github.com.scoliossis.Modules.Movement.NoClip;
import github.com.scoliossis.Modules.Movement.Sprint;
import github.com.scoliossis.Util.C;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public boolean noClip;

    @Shadow public float rotationYaw;

    @Shadow public double motionX;

    @Shadow public double motionZ;

    @Shadow private int entityId;

    @Shadow protected abstract boolean getFlag(int p_getFlag_1_);

    @Shadow public float rotationPitch;

    @Shadow protected abstract void setFlag(int p_setFlag_1_, boolean p_setFlag_2_);

    @Shadow public abstract int getEntityId();

    @Inject(method = "moveEntity", at = @At("HEAD"))
    private void hookNoClip(CallbackInfo ci) {
        if (!this.noClip && NoClip.shouldNoClip()) {
            this.noClip = true;
        }
    }

    @Inject(method = "moveFlying", at = @At("HEAD"), cancellable = true)
    public void moveFlying(float p_moveFlying_1_, float p_moveFlying_2_, float p_moveFlying_3_, CallbackInfo ci) {
        if (C.p() == null || this.entityId != C.p().getEntityId()) return;

        if (!MovementFix.postedEvent) {
            RotationEvent rotationEvent = new RotationEvent(this.rotationYaw, this.rotationPitch, false);
            MinecraftForge.EVENT_BUS.post(rotationEvent);
        }

        MoveFlyingEvent moveFlyingEvent = new MoveFlyingEvent(p_moveFlying_1_, p_moveFlying_2_, p_moveFlying_3_, MovementFix.shouldFixMovement() ? MovementFix.playerYaw : this.rotationYaw);
        if (MinecraftForge.EVENT_BUS.post(moveFlyingEvent)) return;

        float f = moveFlyingEvent.strafe * moveFlyingEvent.strafe + moveFlyingEvent.forward * moveFlyingEvent.forward;
        if (f >= 1.0E-4F) {
            f = MathHelper.sqrt_float(f);
            if (f < 1.0F) {
                f = 1.0F;
            }

            f = moveFlyingEvent.friction / f;
            moveFlyingEvent.strafe *= f;
            moveFlyingEvent.forward *= f;
            float f1 = MathHelper.sin(moveFlyingEvent.yaw * 3.1415927F / 180.0F);
            float f2 = MathHelper.cos(moveFlyingEvent.yaw * 3.1415927F / 180.0F);
            this.motionX += (double)(moveFlyingEvent.strafe * f2 - moveFlyingEvent.forward * f1);
            this.motionZ += (double)(moveFlyingEvent.forward * f2 + moveFlyingEvent.strafe * f1);
        }

        ci.cancel();
    }
}
