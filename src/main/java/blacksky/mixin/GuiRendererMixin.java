package blacksky.mixin;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.vertex.VertexFormat;
import blacksky.screens.modernui.impl.WorldAnimation;
import blacksky.utils.render.ui.blur.BlurFramebuffer;
import blacksky.utils.render.ui.glass.GlassRenderer;
import blacksky.utils.render.ui.image.ImageRenderer;
import blacksky.utils.render.ui.outline.outline360.Outline360Renderer;
import blacksky.utils.render.ui.outline.outlinedefault.DefaultOutlineRenderer;
import blacksky.utils.render.ui.outline.outlineglass.GlassOutlineRenderer;
import blacksky.utils.render.ui.arc.ArcOutlineRenderer;
import blacksky.utils.render.ui.arc.ArcRenderer;
import blacksky.utils.render.ui.rectangle.rectdefault.DefaultRectangleRenderer;
import blacksky.utils.render.ui.rectangle.recthalficon.HalfIconRectangleRenderer;
import blacksky.utils.render.ui.rectangle.recthalftone.HalftoneRectangleRenderer;
import blacksky.utils.render.ui.ripple.RippleRenderer;
import blacksky.utils.render.ui.zippy.ZippyRenderer;
import blacksky.utils.render.item.RenderItem;
import net.minecraft.client.gui.render.GuiRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiRenderer.class)
public abstract class GuiRendererMixin {
    private RenderPass blacksky$currentRenderPass;
    private boolean blacksky$blurDrawActive;
    private boolean blacksky$glassDrawActive;
    private boolean blacksky$glassOutlineDrawActive;
    private boolean blacksky$rectangleDrawActive;
    private boolean blacksky$halfIconRectangleDrawActive;
    private boolean blacksky$halftoneRectangleDrawActive;
    private boolean blacksky$zippyDrawActive;
    private boolean blacksky$arcDrawActive;
    private boolean blacksky$arcOutlineDrawActive;
    private boolean blacksky$outlineDrawActive;
    private boolean blacksky$outline360DrawActive;
    private boolean blacksky$imageDrawActive;
    private boolean blacksky$itemDrawActive;
    private boolean blacksky$rippleDrawActive;

    @Redirect(method = "draw", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setProjectionMatrix(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lcom/mojang/blaze3d/ProjectionType;)V", ordinal = 0))
    private void blacksky$useModernWorldProjection(GpuBufferSlice projection, ProjectionType projectionType) {
        GpuBufferSlice override = WorldAnimation.projectionOverride();
        if (override != null) {
            RenderSystem.setProjectionMatrix(override, ProjectionType.PERSPECTIVE);
            return;
        }
        RenderSystem.setProjectionMatrix(projection, projectionType);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void blacksky$beginBlurFrame(GpuBufferSlice fogBuffer, CallbackInfo ci) {
        BlurFramebuffer.getInstance().beginGuiFrame();
        GlassRenderer.getInstance().beginGuiFrame();
        GlassOutlineRenderer.getInstance().beginGuiFrame();
        DefaultRectangleRenderer.getInstance().beginGuiFrame();
        HalfIconRectangleRenderer.getInstance().beginGuiFrame();
        HalftoneRectangleRenderer.getInstance().beginGuiFrame();
        ZippyRenderer.getInstance().beginGuiFrame();
        ArcRenderer.getInstance().beginGuiFrame();
        ArcOutlineRenderer.getInstance().beginGuiFrame();
        DefaultOutlineRenderer.getInstance().beginGuiFrame();
        Outline360Renderer.getInstance().beginGuiFrame();
        ImageRenderer.getInstance().beginGuiFrame();
        RippleRenderer.getInstance().beginGuiFrame();
        RenderItem.beginGuiFrame();
    }

    @Inject(method = "prepare", at = @At("HEAD"))
    private void blacksky$preparePendingBlurResources(CallbackInfo ci) {
        BlurFramebuffer.getInstance().preparePending();
    }

    @Inject(method = "prepare", at = @At("RETURN"))
    private void blacksky$prepareRenderUniforms(CallbackInfo ci) {
        BlurFramebuffer.getInstance().prepareBuffers();
        GlassRenderer.getInstance().prepareBuffers();
        GlassOutlineRenderer.getInstance().prepareBuffers();
        DefaultRectangleRenderer.getInstance().prepareBuffers();
        HalfIconRectangleRenderer.getInstance().prepareBuffers();
        HalftoneRectangleRenderer.getInstance().prepareBuffers();
        ZippyRenderer.getInstance().prepareBuffers();
        ArcRenderer.getInstance().prepareBuffers();
        ArcOutlineRenderer.getInstance().prepareBuffers();
        DefaultOutlineRenderer.getInstance().prepareBuffers();
        Outline360Renderer.getInstance().prepareBuffers();
        ImageRenderer.getInstance().prepareBuffers();
        RippleRenderer.getInstance().prepareBuffers();
        RenderItem.prepareBuffers();
    }

    @Inject(method = "draw", at = @At("HEAD"))
    private void blacksky$prepareBlurCapture(GpuBufferSlice fogBuffer, CallbackInfo ci) {
        BlurFramebuffer.getInstance().prepareGuiDraw();
    }

    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;processBlurEffect()V", shift = At.Shift.BEFORE))
    private void blacksky$prepareBlurCaptureAfterBeforeBlur(GpuBufferSlice fogBuffer, CallbackInfo ci) {
        BlurFramebuffer.getInstance().prepareGuiDraw();
    }

    @Redirect(method = "executeDraw", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setPipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V"))
    private void blacksky$trackPipeline(RenderPass renderPass, RenderPipeline pipeline) {
        blacksky$currentRenderPass = renderPass;
        blacksky$blurDrawActive = BlurFramebuffer.getInstance().isBlurPipeline(pipeline);
        blacksky$glassDrawActive = GlassRenderer.getInstance().isGlassPipeline(pipeline);
        blacksky$glassOutlineDrawActive = GlassOutlineRenderer.getInstance().isGlassOutlinePipeline(pipeline);
        blacksky$rectangleDrawActive = DefaultRectangleRenderer.getInstance().isRectanglePipeline(pipeline);
        blacksky$halfIconRectangleDrawActive = HalfIconRectangleRenderer.getInstance().isHalfIconRectanglePipeline(pipeline);
        blacksky$halftoneRectangleDrawActive = HalftoneRectangleRenderer.getInstance().isHalftoneRectanglePipeline(pipeline);
        blacksky$zippyDrawActive = ZippyRenderer.getInstance().isZippyPipeline(pipeline);
        blacksky$arcDrawActive = ArcRenderer.getInstance().isArcPipeline(pipeline);
        blacksky$arcOutlineDrawActive = ArcOutlineRenderer.getInstance().isArcOutlinePipeline(pipeline);
        blacksky$outlineDrawActive = DefaultOutlineRenderer.getInstance().isOutlinePipeline(pipeline);
        blacksky$outline360DrawActive = Outline360Renderer.getInstance().isOutline360Pipeline(pipeline);
        blacksky$imageDrawActive = ImageRenderer.getInstance().isImagePipeline(pipeline);
        blacksky$rippleDrawActive = RippleRenderer.getInstance().isRipplePipeline(pipeline);
        blacksky$itemDrawActive = RenderItem.isItemPipeline(pipeline);
        renderPass.setPipeline(pipeline);
    }

    @Inject(method = "executeDraw", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;drawIndexed(IIII)V", shift = At.Shift.BEFORE))
    private void blacksky$bindBlurParams(@Coerce Object draw, RenderPass renderPass, GpuBuffer gpuBuffer, VertexFormat.IndexType indexType, CallbackInfo ci) {
        if (blacksky$blurDrawActive && blacksky$currentRenderPass != null) {
            BlurFramebuffer.getInstance().bindBlurParams(blacksky$currentRenderPass);
        }
        if (blacksky$glassDrawActive && blacksky$currentRenderPass != null) {
            GlassRenderer.getInstance().bindParams(blacksky$currentRenderPass);
        }
        if (blacksky$glassOutlineDrawActive && blacksky$currentRenderPass != null) {
            GlassOutlineRenderer.getInstance().bindParams(blacksky$currentRenderPass);
        }
        if (blacksky$rectangleDrawActive && blacksky$currentRenderPass != null) {
            DefaultRectangleRenderer.getInstance().bindParams(blacksky$currentRenderPass);
        }
        if (blacksky$halfIconRectangleDrawActive && blacksky$currentRenderPass != null) {
            HalfIconRectangleRenderer.getInstance().bindParams(blacksky$currentRenderPass);
        }
        if (blacksky$halftoneRectangleDrawActive && blacksky$currentRenderPass != null) {
            HalftoneRectangleRenderer.getInstance().bindParams(blacksky$currentRenderPass);
        }
        if (blacksky$zippyDrawActive && blacksky$currentRenderPass != null) {
            ZippyRenderer.getInstance().bindParams(blacksky$currentRenderPass);
        }
        if (blacksky$arcDrawActive && blacksky$currentRenderPass != null) {
            ArcRenderer.getInstance().bindParams(blacksky$currentRenderPass);
        }
        if (blacksky$arcOutlineDrawActive && blacksky$currentRenderPass != null) {
            ArcOutlineRenderer.getInstance().bindParams(blacksky$currentRenderPass);
        }
        if (blacksky$outlineDrawActive && blacksky$currentRenderPass != null) {
            DefaultOutlineRenderer.getInstance().bindParams(blacksky$currentRenderPass);
        }
        if (blacksky$outline360DrawActive && blacksky$currentRenderPass != null) {
            Outline360Renderer.getInstance().bindParams(blacksky$currentRenderPass);
        }
        if (blacksky$imageDrawActive && blacksky$currentRenderPass != null) {
            ImageRenderer.getInstance().bindParams(blacksky$currentRenderPass);
        }
        if (blacksky$rippleDrawActive && blacksky$currentRenderPass != null) {
            RippleRenderer.getInstance().bindParams(blacksky$currentRenderPass);
        }
        if (blacksky$itemDrawActive && blacksky$currentRenderPass != null) {
            RenderItem.bindParams(blacksky$currentRenderPass);
        }
    }

    @Inject(method = "executeDraw", at = @At("RETURN"))
    private void blacksky$clearTrackedPipeline(@Coerce Object draw, RenderPass renderPass, GpuBuffer gpuBuffer, VertexFormat.IndexType indexType, CallbackInfo ci) {
        blacksky$currentRenderPass = null;
        blacksky$blurDrawActive = false;
        blacksky$glassDrawActive = false;
        blacksky$glassOutlineDrawActive = false;
        blacksky$rectangleDrawActive = false;
        blacksky$halfIconRectangleDrawActive = false;
        blacksky$halftoneRectangleDrawActive = false;
        blacksky$zippyDrawActive = false;
        blacksky$arcDrawActive = false;
        blacksky$arcOutlineDrawActive = false;
        blacksky$outlineDrawActive = false;
        blacksky$outline360DrawActive = false;
        blacksky$imageDrawActive = false;
        blacksky$itemDrawActive = false;
        blacksky$rippleDrawActive = false;
    }
}
