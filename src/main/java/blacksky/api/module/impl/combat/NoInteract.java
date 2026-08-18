package blacksky.api.module.impl.combat;

import blacksky.api.module.Module;
import blacksky.api.module.ModuleCategory;

public class NoInteract extends Module {
    public NoInteract() {
        super("No Interact", "Blocks unwanted interactions.", ModuleCategory.COMBAT);
    }
}
