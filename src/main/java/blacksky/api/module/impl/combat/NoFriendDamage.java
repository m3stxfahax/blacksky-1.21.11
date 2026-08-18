package blacksky.api.module.impl.combat;

import blacksky.api.events.annotation.SubscribeEvent;
import blacksky.api.events.impl.InteractEntityEvent;
import blacksky.api.module.Module;
import blacksky.api.module.ModuleCategory;
import blacksky.utils.repository.friend.FriendUtils;

public class NoFriendDamage extends Module {
    public NoFriendDamage() {
        super("No Friend Damage", "Blocks attacks on friends.", ModuleCategory.COMBAT);
    }

    @SubscribeEvent
    private void onInteract(InteractEntityEvent event) {
        if (FriendUtils.isFriend(event.getEntity())) {
            event.cancel();
        }
    }
}
