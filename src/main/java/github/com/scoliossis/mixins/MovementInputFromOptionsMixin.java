package github.com.scoliossis.mixins;

import github.com.scoliossis.Events.MovementInputEvent;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovementInputFromOptions.class)
public class MovementInputFromOptionsMixin extends MovementInput {
    @Shadow @Final private GameSettings gameSettings;

    @Inject(method = "updatePlayerMoveState", at = @At("HEAD"), cancellable = true)
    public void updatePlayerMoveState(CallbackInfo ci) {
        ci.cancel();

        float strafeNumTemp = 0;
        float forwardNumTemp = 0;
        if (this.gameSettings.keyBindForward.isKeyDown()) {
            ++forwardNumTemp;
        }

        if (this.gameSettings.keyBindBack.isKeyDown()) {
            --forwardNumTemp;
        }

        if (this.gameSettings.keyBindLeft.isKeyDown()) {
            ++strafeNumTemp;
        }

        if (this.gameSettings.keyBindRight.isKeyDown()) {
            --strafeNumTemp;
        }

        boolean tempJump = this.gameSettings.keyBindJump.isKeyDown();
        boolean tempSneak = this.gameSettings.keyBindSneak.isKeyDown();

        MovementInputEvent event = new MovementInputEvent(strafeNumTemp, forwardNumTemp, tempJump, tempSneak, 0.3f);

        if (MinecraftForge.EVENT_BUS.post(event)) return;

        this.moveStrafe = event.strafe;
        this.moveForward = event.forward;

        this.jump = event.jump;
        this.sneak = event.sneak;

        if (this.sneak) {
            this.moveStrafe = (float)((double)event.strafe * event.sneakSpeed);
            this.moveForward = (float)((double)event.forward * event.sneakSpeed);
        }
    }
}
