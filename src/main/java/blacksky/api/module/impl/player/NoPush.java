package blacksky.api.module.impl.player;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import blacksky.api.events.annotation.SubscribeEvent;
import blacksky.api.events.impl.PlayerCollisionEvent;
import blacksky.api.events.impl.PushEvent;
import blacksky.api.module.Module;
import blacksky.api.module.ModuleCategory;
import blacksky.api.settings.impl.MultiModeSetting;

public final class NoPush extends Module {
    private final MultiModeSetting ignoreSetting = register(new MultiModeSetting("Ignore", "Push sources to ignore.",
            new String[]{"Water", "Blocks", "Entity Collision", "Powder Snow", "Berries"},
            "Water", "Blocks", "Entity Collision"));

    public NoPush() {
        super("No Push", "Disables selected push interactions.", ModuleCategory.PLAYER);
    }

    @SubscribeEvent
    private void onPush(PushEvent event) {
        switch (event.getType()) {
            case COLLISION -> event.setCancelled(ignoreSetting.isSelected("Entity Collision"));
            case WATER -> event.setCancelled(ignoreSetting.isSelected("Water"));
            case BLOCK -> event.setCancelled(ignoreSetting.isSelected("Blocks"));
        }
    }

    @SubscribeEvent
    private void onPlayerCollision(PlayerCollisionEvent event) {
        Block block = event.getBlock();
        if (block == Blocks.POWDER_SNOW) {
            event.setCancelled(ignoreSetting.isSelected("Powder Snow"));
        } else if (block == Blocks.SWEET_BERRY_BUSH) {
            event.setCancelled(ignoreSetting.isSelected("Berries"));
        }
    }
}
