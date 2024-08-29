package github.com.scoliossis.mixins;

import github.com.scoliossis.Modules.Visuals.ServerSideRotations;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = RendererLivingEntity.class, priority = 1)
public abstract class RenderLivingEntityMixin<T extends EntityLivingBase> extends Render<T> {

    public final EntityLivingBase entity;
    public final RendererLivingEntity<T> renderer;
    public final double x;
    public final double y;
    public final double z;


    @Shadow protected ModelBase mainModel;

    @Shadow protected abstract float getSwingProgress(T p_getSwingProgress_1_, float p_getSwingProgress_2_);

    @Shadow protected abstract float interpolateRotation(float p_interpolateRotation_1_, float p_interpolateRotation_2_, float p_interpolateRotation_3_);

    @Shadow protected abstract void renderLivingAt(T p_renderLivingAt_1_, double p_renderLivingAt_2_, double p_renderLivingAt_4_, double p_renderLivingAt_4_2);

    @Shadow protected abstract float handleRotationFloat(T p_handleRotationFloat_1_, float p_handleRotationFloat_2_);

    @Shadow protected abstract void rotateCorpse(T p_rotateCorpse_1_, float p_rotateCorpse_2_, float p_rotateCorpse_3_, float p_rotateCorpse_4_);

    @Shadow protected abstract void preRenderCallback(T p_preRenderCallback_1_, float p_preRenderCallback_2_);

    @Shadow protected boolean renderOutlines;

    @Shadow protected abstract boolean setScoreTeamColor(T p_setScoreTeamColor_1_);

    @Shadow protected abstract void renderModel(T p_renderModel_1_, float p_renderModel_2_, float p_renderModel_3_, float p_renderModel_4_, float p_renderModel_5_, float p_renderModel_6_, float p_renderModel_7_);

    @Shadow protected abstract void unsetScoreTeamColor();

    @Shadow protected abstract void unsetBrightness();

    @Shadow protected abstract boolean setDoRenderBrightness(T p_setDoRenderBrightness_1_, float p_setDoRenderBrightness_2_);

    @Shadow protected abstract void renderLayers(T p_renderLayers_1_, float p_renderLayers_2_, float p_renderLayers_3_, float p_renderLayers_4_, float p_renderLayers_5_, float p_renderLayers_6_, float p_renderLayers_7_, float p_renderLayers_8_);

    @Shadow @Final private static Logger logger;

    protected RenderLivingEntityMixin(RenderManager p_i46179_1_, EntityLivingBase entity, RendererLivingEntity<T> renderer, double x, double y, double z) {
        super(p_i46179_1_);
        this.entity = entity;
        this.renderer = renderer;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void doRender(T p_doRender_1_, double p_doRender_2_, double p_doRender_4_, double p_doRender_6_, float p_doRender_8_, float p_doRender_9_) {
        if (!MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre(p_doRender_1_, renderer, p_doRender_2_, p_doRender_4_, p_doRender_6_))) {


            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            this.mainModel.swingProgress = this.getSwingProgress(p_doRender_1_, p_doRender_9_);
            boolean shouldSit = p_doRender_1_.isRiding() && p_doRender_1_.ridingEntity != null && p_doRender_1_.ridingEntity.shouldRiderSit();
            this.mainModel.isRiding = shouldSit;
            this.mainModel.isChild = p_doRender_1_.isChild();

            float bodyYaw = this.interpolateRotation(p_doRender_1_.prevRenderYawOffset, p_doRender_1_.renderYawOffset, p_doRender_9_);;
            float headYaw = this.interpolateRotation(p_doRender_1_.prevRotationYawHead, p_doRender_1_.rotationYawHead, p_doRender_9_);
            float pitch = p_doRender_1_.prevRotationPitch + (p_doRender_1_.rotationPitch - p_doRender_1_.prevRotationPitch) * p_doRender_9_;

            if (p_doRender_1_ == C.p() && Configs.showRots) {
                if (ServerSideRotations.yaw != C.p().rotationYawHead) {
                    bodyYaw = ServerSideRotations.yaw;
                    headYaw = ServerSideRotations.yaw;
                }

                // dumb if statement or smth
                if (ServerSideRotations.pitch != C.p().rotationPitch) pitch = ServerSideRotations.pitch;
            }


            try {
                float f = bodyYaw;
                float f1 = headYaw;
                float f2 = f1 - f;
                float f8;
                if (shouldSit && p_doRender_1_.ridingEntity instanceof EntityLivingBase) {
                    EntityLivingBase entitylivingbase = (EntityLivingBase)p_doRender_1_.ridingEntity;
                    f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, p_doRender_9_);
                    f2 = f1 - f;
                    f8 = MathHelper.wrapAngleTo180_float(f2);
                    if (f8 < -85.0F) {
                        f8 = -85.0F;
                    }

                    if (f8 >= 85.0F) {
                        f8 = 85.0F;
                    }

                    f = f1 - f8;
                    if (f8 * f8 > 2500.0F) {
                        f += f8 * 0.2F;
                    }
                }

                float f7 = pitch;
                this.renderLivingAt(p_doRender_1_, p_doRender_2_, p_doRender_4_, p_doRender_6_);
                f8 = this.handleRotationFloat(p_doRender_1_, p_doRender_9_);
                this.rotateCorpse(p_doRender_1_, f8, f, p_doRender_9_);
                GlStateManager.enableRescaleNormal();
                GlStateManager.scale(-1.0F, -1.0F, 1.0F);
                this.preRenderCallback(p_doRender_1_, p_doRender_9_);
                float f4 = 0.0625F;
                GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
                float f5 = p_doRender_1_.prevLimbSwingAmount + (p_doRender_1_.limbSwingAmount - p_doRender_1_.prevLimbSwingAmount) * p_doRender_9_;
                float f6 = p_doRender_1_.limbSwing - p_doRender_1_.limbSwingAmount * (1.0F - p_doRender_9_);
                if (p_doRender_1_.isChild()) {
                    f6 *= 3.0F;
                }

                if (f5 > 1.0F) {
                    f5 = 1.0F;
                }

                GlStateManager.enableAlpha();
                this.mainModel.setLivingAnimations(p_doRender_1_, f6, f5, p_doRender_9_);
                this.mainModel.setRotationAngles(f6, f5, f8, f2, f7, 0.0625F, p_doRender_1_);
                boolean flag;
                if (this.renderOutlines) {
                    flag = this.setScoreTeamColor(p_doRender_1_);
                    this.renderModel(p_doRender_1_, f6, f5, f8, f2, f7, 0.0625F);
                    if (flag) {
                        this.unsetScoreTeamColor();
                    }
                } else {
                    flag = this.setDoRenderBrightness(p_doRender_1_, p_doRender_9_);
                    this.renderModel(p_doRender_1_, f6, f5, f8, f2, f7, 0.0625F);
                    if (flag) {
                        this.unsetBrightness();
                    }

                    GlStateManager.depthMask(true);
                    if (!(p_doRender_1_ instanceof EntityPlayer) || !((EntityPlayer)p_doRender_1_).isSpectator()) {
                        this.renderLayers(p_doRender_1_, f6, f5, p_doRender_9_, f8, f2, f7, 0.0625F);
                    }
                }

                GlStateManager.disableRescaleNormal();
            } catch (Exception var20) {
                Exception exception = var20;
                logger.error("Couldn't render entity", exception);
            }

            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            if (!this.renderOutlines) {
                super.doRender(p_doRender_1_, p_doRender_2_, p_doRender_4_, p_doRender_6_, p_doRender_8_, p_doRender_9_);
            }

            MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post(p_doRender_1_, renderer, p_doRender_2_, p_doRender_4_, p_doRender_6_));
        }
    }
}
