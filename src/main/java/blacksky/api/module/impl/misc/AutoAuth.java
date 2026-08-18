package blacksky.api.module.impl.misc;

import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import blacksky.api.events.annotation.SubscribeEvent;
import blacksky.api.events.impl.PacketEvent;
import blacksky.api.module.Module;
import blacksky.api.module.ModuleCategory;
import blacksky.api.settings.impl.StringSetting;

import java.util.Locale;

public final class AutoAuth extends Module {
    private final StringSetting password = register(new StringSetting("Password", "Server auth password.", "BlackSkyPass123", 64));

    public AutoAuth() {
        super("Auto Auth", "Automatically sends /login and /register commands.", ModuleCategory.MISC);
    }

    @SubscribeEvent
    private void onPacket(PacketEvent event) {
        if (!event.isReceive() || mc.player == null || mc.level == null || mc.player.connection == null) {
            return;
        }
        if (!(event.getPacket() instanceof ClientboundSystemChatPacket packet)) {
            return;
        }

        String pass = password.getValue() == null ? "" : password.getValue().trim();
        if (pass.length() < 4) {
            return;
        }

        String message = packet.content().getString().toLowerCase(Locale.ROOT);
        if (message.contains("войдите") || message.contains("/login")) {
            mc.player.connection.sendCommand("login " + pass);
            return;
        }
        if (message.contains("зарегистрируйтесь") || message.contains("регистрация") || message.contains("/reg")) {
            mc.player.connection.sendCommand("reg " + pass + " " + pass);
        }
    }
}
