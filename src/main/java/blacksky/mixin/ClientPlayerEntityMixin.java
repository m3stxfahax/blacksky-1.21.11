package blacksky.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import blacksky.api.events.impl.CloseScreenEvent;
import blacksky.api.events.impl.PlayerTravelEvent;
import blacksky.api.events.impl.PushEvent;
import blacksky.api.events.impl.UsingItemEvent;
import blacksky.api.module.impl.combat.AuraModule;
import blacksky.api.module.impl.combat.aura.AngleConnection;
import blacksky.manager.Manager;
import blacksky.utils.move.MoveUtil;

@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin {
    @Shadow
    private float yRotLast;

    @Shadow
    private float xRotLast;

    @Shadow
    @Final
    public ClientPacketListener connection;

    @Shadow
    @Final
    protected Minecraft minecraft;

    @Shadow
    public abstract boolean isUsingItem();

    @Unique
    private double blacksky$prevX;

    @Unique
    private double blacksky$prevZ;

    @Unique
    private float blacksky$prevBodyYaw;

    @Inject(method = "moveTowardsClosestSpace", at = @At("HEAD"), cancellable = true, require = 0)
    private void blacksky$pushOutOfBlocks(double x, double z, CallbackInfo ci) {
        PushEvent event = Manager.postEvent(new PushEvent(PushEvent.Type.BLOCK));
        if (event.isCancelled()) {
            ci.cancel();
        }
    }


    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/ClientInput;tick()V", shift = At.Shift.AFTER), require = 0)
    private void blacksky$onInputTick(CallbackInfo ci) {
        if (minecraft.player != null) {
            Manager.postEvent(new PlayerTravelEvent(Vec3.ZERO, false));
        }
    }

    @Redirect(method = "modifyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec2;scale(F)Lnet/minecraft/world/phys/Vec2;", ordinal = 1), require = 0)
    private Vec2 blacksky$cancelItemSlowdown(Vec2 vec, float multiplier) {
        UsingItemEvent event = Manager.postEvent(new UsingItemEvent(UsingItemEvent.ON));
        LocalPlayer player = (LocalPlayer) (Object) this;
        if (event.isCancelled() && isUsingItem() && !player.isPassenger()) {
            return vec.scale(1.0F);
        }
        return vec.scale(multiplier);
    }

    @Inject(method = "closeContainer", at = @At("HEAD"), cancellable = true, require = 0)
    private void blacksky$closeHandledScreenHook(CallbackInfo ci) {
        Screen screen = minecraft.screen;
        CloseScreenEvent event = Manager.postEvent(new CloseScreenEvent(screen));
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method = {"sendPosition", "tick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"), require = 0)
    private float blacksky$packetYaw(float original) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        AngleConnection controller = AngleConnection.INSTANCE;
        if (!controller.shouldApplyPacketRotation()) {
            blacksky$syncBodyYawCache(player, original);
            return original;
        }

        float yaw = controller.getPacketYaw();
        float bodyYaw = MoveUtil.calculateBodyYaw(
                yaw,
                blacksky$prevBodyYaw,
                blacksky$prevX,
                blacksky$prevZ,
                player.getX(),
                player.getZ(),
                player.getAttackAnim(1.0F)
        );

        blacksky$prevBodyYaw = bodyYaw;
        blacksky$prevX = player.getX();
        blacksky$prevZ = player.getZ();
        player.setYBodyRot(bodyYaw);
        return yaw;
    }

    @Unique
    private void blacksky$syncBodyYawCache(LocalPlayer player, float yaw) {
        blacksky$prevBodyYaw = yaw;
        blacksky$prevX = player.getX();
        blacksky$prevZ = player.getZ();
    }

    @ModifyExpressionValue(method = {"sendPosition", "tick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot()F"), require = 0)
    private float blacksky$packetPitch(float original) {
        AngleConnection controller = AngleConnection.INSTANCE;
        return controller.shouldApplyPacketRotation() ? controller.getPacketPitch() : original;
    }

    @Inject(method = "sendPosition", at = @At("TAIL"))
    private void blacksky$ensureSilentRotationPacket(CallbackInfo ci) {
        AngleConnection controller = AngleConnection.INSTANCE;
        AuraModule aura = AuraModule.getInstance();
        boolean queuedAuraAttack = aura != null && aura.hasQueuedAttack();
        if (!controller.shouldApplyPacketRotation()) {
            if (queuedAuraAttack) {
                aura.flushQueuedAttack();
            }
            return;
        }

        LocalPlayer player = (LocalPlayer) (Object) this;
        float yaw = controller.getPacketYaw();
        float pitch = controller.getPacketPitch();
        boolean rotationChanged = Math.abs(yaw - yRotLast) > 1.0E-3F || Math.abs(pitch - xRotLast) > 1.0E-3F;
        if (rotationChanged) {
            connection.send(new ServerboundMovePlayerPacket.Rot(
                    yaw,
                    pitch,
                    player.onGround(),
                    player.horizontalCollision
            ));
            yRotLast = yaw;
            xRotLast = pitch;
        }

        if (queuedAuraAttack) {
            aura.flushQueuedAttack();
        }
    }
}
