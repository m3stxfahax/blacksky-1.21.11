package blacksky;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import blacksky.manager.Manager;
import blacksky.utils.render.ui.Render2D;
import blacksky.utils.sounds.SoundManager;

public class Engine implements ModInitializer, ClientModInitializer {

    private final Manager manager = new Manager();

    @Override
    public void onInitialize() {
        SoundManager.init();
        Render2D.init();
    }

    @Override
    public void onInitializeClient() {
        manager.initClient();
    }
}
