package github.com.scoliossis.mixins;

import com.mojang.authlib.GameProfile;
import github.com.scoliossis.Events.MotionEvent;
import github.com.scoliossis.Modules.Player.MovementFix;
import github.com.scoliossis.Modules.Movement.Sprint;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin extends AbstractClientPlayer  {
    @Shadow private boolean serverSprintState;

    @Shadow @Final public NetHandlerPlayClient sendQueue;

    @Shadow private boolean serverSneakState;

    public EntityPlayerSPMixin(World p_i45074_1_, GameProfile p_i45074_2_) {
        super(p_i45074_1_, p_i45074_2_);
    }

    @Shadow protected abstract boolean isCurrentViewEntity();

    @Shadow private float lastReportedPitch;

    @Shadow private double lastReportedPosX;

    @Shadow private double lastReportedPosY;

    @Shadow private double lastReportedPosZ;

    @Shadow private float lastReportedYaw;

    @Shadow private int positionUpdateTicks;


    @Shadow public int sprintingTicksLeft;

    /**
     * @author scale
     * @reason omnisprint
     */
    @Overwrite
    public void setSprinting(boolean p_setSprinting_1_) {
        if (this.getEntityId() == C.p().getEntityId() && Sprint.shouldSprint()) p_setSprinting_1_ = Sprint.sprintOverride();

        super.setSprinting(p_setSprinting_1_);
        this.sprintingTicksLeft = p_setSprinting_1_ ? 600 : 0;
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void onUpdateWalkingPlayer(CallbackInfo ci) {
        ci.cancel();

        MotionEvent.Pre event =  new MotionEvent.Pre(this.isSprinting(), this.isSneaking(), this.posX, this.getEntityBoundingBox().minY, this.posZ, MovementFix.playerYaw, MovementFix.playerPitch, this.onGround);
        if (MinecraftForge.EVENT_BUS.post(event)) return;

        boolean flag = event.sprinting;
        if (flag != this.serverSprintState) {
            if (flag) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
            } else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }

            this.serverSprintState = flag;
        }

        boolean flag1 = event.sneaking;
        if (flag1 != this.serverSneakState) {
            if (flag1) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
            } else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }

            this.serverSneakState = flag1;
        }

        if (this.isCurrentViewEntity()) {
            double d0 = event.x - this.lastReportedPosX;
            double d1 = event.y - this.lastReportedPosY;
            double d2 = event.z - this.lastReportedPosZ;
            double d3 = (event.yaw - this.lastReportedYaw);
            double d4 = (event.pitch - this.lastReportedPitch);
            boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4 || this.positionUpdateTicks >= 20;
            boolean flag3 = d3 != 0.0 || d4 != 0.0;
            if (this.ridingEntity == null) {
                if (flag2 && flag3) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(event.x, event.y, event.z, event.yaw, event.pitch, event.onGround));
                } else if (flag2) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(event.x, event.y, event.z, event.onGround));
                } else if (flag3) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(event.yaw, event.pitch, event.onGround));
                } else {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer(event.onGround));
                }
            } else {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0, this.motionZ, event.yaw, event.pitch, event.onGround));
                flag2 = false;
            }

            ++this.positionUpdateTicks;
            if (flag2) {
                this.lastReportedPosX = event.x;
                this.lastReportedPosY = event.y;
                this.lastReportedPosZ = event.z;
                this.positionUpdateTicks = 0;
            }

            if (flag3) {
                this.lastReportedYaw = event.yaw;
                this.lastReportedPitch = event.pitch;
            }
        }

        MinecraftForge.EVENT_BUS.post(new MotionEvent.Post(event.sprinting, event.sneaking, event.x, event.y, event.z, event.yaw, event.pitch, event.onGround));
    }
}
