package blacksky.api.module.impl.visual;

import blacksky.api.drag.core.ElementManager;
import blacksky.api.drag.core.ElementScreen;
import blacksky.api.drag.core.HudElement;
import blacksky.api.drag.impl.AnimationShowcase;
import blacksky.api.drag.impl.Armor;
import blacksky.api.drag.impl.Binds;
import blacksky.api.drag.impl.Cooldowns;
import blacksky.api.drag.impl.Hotbar;
import blacksky.api.drag.impl.HotKeys;
import blacksky.api.drag.impl.HudPanel;
import blacksky.api.drag.impl.Inventory;
import blacksky.api.drag.impl.MediaPlayer;
import blacksky.api.drag.impl.Notifications;
import blacksky.api.drag.impl.Potions;
import blacksky.api.drag.impl.Staff;
import blacksky.api.drag.impl.TargetHud;
import blacksky.api.drag.impl.Watermark;
import blacksky.api.drag.impl.WatermarkIcons;
import blacksky.api.events.impl.DrawEvent;
import blacksky.api.module.Module;
import blacksky.api.module.ModuleCategory;
import blacksky.api.settings.impl.BooleanSetting;
import blacksky.api.settings.impl.MultiModeSetting;
import blacksky.utils.render.LoadingVisualGuard;
import blacksky.utils.render.item.RenderItem;
import blacksky.utils.render.ui.Render2D;
import blacksky.utils.render.ui.blur.BuiltBlur;
import net.minecraft.client.input.MouseButtonEvent;

import java.util.List;

public class Hud extends Module {
    private static final float NO_BLUR_RECT_ALPHA_SCALE = 200.0F / 255.0F;

    private static final String[] ELEMENTS = {
            "Watermark",
            "Hotbar",
            "Armor",
            "HotKeys",
            "Binds",
            "Notifications",
            "Potions",
            "Cooldowns",
            "TargetHud",
            "Staff",
            "Inventory",
//            "MediaPlayer",
            "Watermark icons"
    };
    private static Hud instance;

    private final MultiModeSetting elements = register(new MultiModeSetting("Elements", "HUD elements to render.",
            ELEMENTS, ELEMENTS));
    private final BooleanSetting blur = register(new BooleanSetting("Blur", "Use blurred HUD backgrounds.", true));
    private final BooleanSetting glow = register(new BooleanSetting("Glow", "Use glow effects on HUD elements.", true));

    private final List<HudElement> elementsList = List.of(
            new Watermark(),
            new Hotbar(),
            new Armor(),
            new HotKeys(),
            new Binds(),
            new Notifications(),
            new Potions(),
            new Cooldowns(),
            new TargetHud(),
            new Staff(),
            new Inventory(),
            new MediaPlayer(),
            // new WatermarkIcons(), // Debug panel - disabled
            new AnimationShowcase()
    );

    public Hud() {
        super("HUD", "Renders draggable HUD elements.", ModuleCategory.VISUAL);
        instance = this;
        setEnabled(true);
    }

    public static Hud getInstance() {
        return instance;
    }

    public static boolean shouldRenderCustomHotbar() {
        Hud hud = instance;
        return hud != null && hud.isEnabled() && hud.elements.isSelected("Hotbar");
    }

    public static boolean isBlurEnabled() {
        Hud hud = instance;
        return hud == null || hud.blur.getValue();
    }

    public static boolean isGlowEnabled() {
        Hud hud = instance;
        return hud == null || hud.glow.getValue();
    }

    public static void renderHudBackground(float x, float y, float width, float height, float radius, float blurRadius, float smoothness, int color) {
        if (isBlurEnabled()) {
            Render2D.blur(x, y, width, height, radius, blurRadius, smoothness, color);
            return;
        }
        Render2D.rect(x, y, width, height, radius, noBlurRectColor(color));
    }

    public static void renderHudBackground(BuiltBlur blur) {
        if (blur == null) {
            return;
        }
        if (isBlurEnabled()) {
            Render2D.blur(blur);
            return;
        }
        Render2D.rect(
                blur.x(),
                blur.y(),
                blur.width(),
                blur.height(),
                blur.radiusTopLeft(),
                blur.radiusTopRight(),
                blur.radiusBottomRight(),
                blur.radiusBottomLeft(),
                noBlurRectColor(blur.color())
        );
    }

    public static void renderHudGlow(String texture, float x, float y, float width, float height, float radius, int color) {
        if (isGlowEnabled()) {
            Render2D.image(texture, x, y, width, height, radius, color);
        }
    }

    private static int noBlurRectColor(int color) {
        int alpha = (color >>> 24) & 0xFF;
        int scaledAlpha = Math.round(alpha * NO_BLUR_RECT_ALPHA_SCALE);
        return (color & 0x00FFFFFF) | (scaledAlpha << 24);
    }

    public void renderHudLayer(DrawEvent event) {
        if (mc.player == null || mc.level == null || mc.getWindow() == null || LoadingVisualGuard.shouldSuppressHud(mc)) {
            return;
        }

        ElementScreen screen = ElementScreen.current();
        ElementManager elementManager = ElementManager.getInstance();
        elementManager.frame(screen);
        if (event.getLayer() == DrawEvent.Layer.CHAT_OVERLAY) {
            elementManager.updateActiveElementFromMouse();
        }
        RenderItem.beginFrame(event.getGraphics());
        try {
            for (HudElement element : elementsList) {
                boolean selected = elements.isSelected(elementName(element));
                setElementVisible(element, selected);
                if (selected) {
                    element.render();
                }
            }
        } finally {
            RenderItem.flush();
        }

        boolean editLayer = event.getLayer() == DrawEvent.Layer.CHAT_OVERLAY && elementManager.canEditCurrentScreen();
        if (editLayer) {
            elementManager.renderEditorOverlay(event.getGraphics(), screen);
        }
    }

    public boolean handleMouseClicked(MouseButtonEvent event, boolean doubled) {
        if (!isEnabled() || event == null || event.button() != 0) {
            return false;
        }
        for (int i = elementsList.size() - 1; i >= 0; i--) {
            HudElement element = elementsList.get(i);
            if (elements.isSelected(elementName(element)) && element.mouseClicked(event, doubled)) {
                return true;
            }
        }
        return false;
    }

    private String elementName(HudElement element) {
        if (element instanceof HudPanel panel) {
            return panel.elementName();
        }
        if (element instanceof Watermark watermark) {
            return watermark.elementName();
        }
        return element.getClass().getSimpleName();
    }

    private void setElementVisible(HudElement element, boolean visible) {
        if (element instanceof HudPanel panel) {
            panel.setHudVisible(visible);
            return;
        }
        if (element instanceof Watermark watermark) {
            watermark.setHudVisible(visible);
        }
    }
}
