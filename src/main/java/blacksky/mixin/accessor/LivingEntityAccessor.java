package blacksky.mixin.accessor;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("jumping")
    boolean blacksky$isJumping();

    @Accessor("noJumpDelay")
    int blacksky$getJumpingCooldown();

    @Accessor("noJumpDelay")
    void blacksky$setJumpingCooldown(int value);
}
