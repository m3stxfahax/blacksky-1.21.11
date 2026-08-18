package blacksky.screens.modernui.settings;

import blacksky.api.settings.Setting;
import blacksky.api.settings.impl.BindSetting;
import blacksky.api.settings.impl.BooleanSetting;
import blacksky.api.settings.impl.ButtonSetting;
import blacksky.api.settings.impl.ColorSetting;
import blacksky.api.settings.impl.ModeSetting;
import blacksky.api.settings.impl.MultiModeSetting;
import blacksky.api.settings.impl.NumberSetting;
import blacksky.api.settings.impl.StringSetting;

final class SettingCardFactory {
    private SettingCardFactory() {
    }

    static SettingCardComponent<?> create(Setting<?> setting) {
        if (setting instanceof ModeSetting modeSetting) {
            return new ModeSettingCard(modeSetting);
        }
        if (setting instanceof MultiModeSetting multiModeSetting) {
            return new MultiSettingCard(multiModeSetting);
        }
        if (setting instanceof NumberSetting numberSetting) {
            return new SliderSettingCard(numberSetting);
        }
        if (setting instanceof BooleanSetting booleanSetting) {
            return new BooleanSettingCard(booleanSetting);
        }
        if (setting instanceof ColorSetting colorSetting) {
            return new ColorSettingCard(colorSetting);
        }
        if (setting instanceof BindSetting bindSetting) {
            return new BindSettingCard(bindSetting);
        }
        if (setting instanceof StringSetting stringSetting) {
            return new TextSettingCard(stringSetting);
        }
        if (setting instanceof ButtonSetting buttonSetting) {
            return new ButtonSettingCard(buttonSetting);
        }
        return null;
    }
}
