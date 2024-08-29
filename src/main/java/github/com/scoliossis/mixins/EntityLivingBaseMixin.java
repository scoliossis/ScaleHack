package github.com.scoliossis.mixins;

import github.com.scoliossis.Events.JumpEvent;
import github.com.scoliossis.Events.RotationEvent;
import github.com.scoliossis.Modules.Player.MovementFix;
import github.com.scoliossis.Modules.Movement.Sprint;
import github.com.scoliossis.Util.C;
import github.com.scoliossis.Util.ChatUtil;
import github.com.scoliossis.Util.MovementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity  {

    public EntityLivingBaseMixin(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    @Shadow protected abstract float getJumpUpwardsMotion();

    @Shadow public abstract boolean isPotionActive(Potion p_isPotionActive_1_);

    @Shadow public abstract PotionEffect getActivePotionEffect(Potion p_getActivePotionEffect_1_);



    // custom jump event that i can set yaw of for move fix + set jump height of it for anti jump boost
    // + omnisprint jumping
    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    protected void jump(CallbackInfo ci) {
        if (this.getEntityId() != C.p().getEntityId()) return;

        RotationEvent rotationEvent = new RotationEvent(this.rotationYaw, this.rotationPitch, false);
        MinecraftForge.EVENT_BUS.post(rotationEvent);

        double jumpHeight = this.getJumpUpwardsMotion() + (this.isPotionActive(Potion.jump) ? (double)((float)(this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F) : 0);

        JumpEvent event = new JumpEvent(MovementFix.shouldFixMovement() ? MovementFix.playerYaw : this.rotationYaw, jumpHeight);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            ci.cancel();
            return;
        }

        this.motionY = event.height;

        if (this.isSprinting() && Sprint.shouldSprint2()) {
            float f = event.yaw * 0.017453292F;
            if (Sprint.shouldSprint()) f = MovementUtil.getMovementYaw(event.yaw) * 0.017453292F;

            this.motionX -= (double)(MathHelper.sin(f) * 0.2F);
            this.motionZ += (double)(MathHelper.cos(f) * 0.2F);
        }

        this.isAirBorne = true;

        ci.cancel();
    }
}
