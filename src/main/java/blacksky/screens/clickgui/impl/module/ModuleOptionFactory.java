package blacksky.screens.clickgui.impl.module;

import blacksky.api.module.Module;
import blacksky.api.settings.Setting;
import blacksky.api.settings.impl.BindSetting;
import blacksky.api.settings.impl.BooleanSetting;
import blacksky.api.settings.impl.ButtonSetting;
import blacksky.api.settings.impl.ColorSetting;
import blacksky.api.settings.impl.ModeSetting;
import blacksky.api.settings.impl.MultiModeSetting;
import blacksky.api.settings.impl.NumberSetting;
import blacksky.api.settings.impl.StringSetting;
import blacksky.screens.clickgui.impl.options.BindOption;
import blacksky.screens.clickgui.impl.options.BooleanOption;
import blacksky.screens.clickgui.impl.options.ButtonOption;
import blacksky.screens.clickgui.impl.options.ClickGuiOption;
import blacksky.screens.clickgui.impl.options.ColorOption;
import blacksky.screens.clickgui.impl.options.MultiOption;
import blacksky.screens.clickgui.impl.options.SingleOption;
import blacksky.screens.clickgui.impl.options.SliderOption;
import blacksky.screens.clickgui.impl.options.TextOption;

import java.util.ArrayList;
import java.util.List;

public final class ModuleOptionFactory {
    private ModuleOptionFactory() {
    }

    public static ModuleOption create(Module module) {
        return new ModuleOption(module, createSettings(module));
    }

    private static List<ClickGuiOption> createSettings(Module module) {
        List<ClickGuiOption> options = new ArrayList<>();
        List<ClickGuiOption> buttonOptions = new ArrayList<>();

        for (Setting<?> setting : module.getSettings()) {
            ClickGuiOption option = createSettingOption(setting);
            if (option == null) {
                continue;
            }
            option.setSetting(setting);
            if (setting instanceof ButtonSetting) {
                buttonOptions.add(option);
            } else {
                options.add(option);
            }
        }

        options.addAll(buttonOptions);
        return List.copyOf(options);
    }

    private static ClickGuiOption createSettingOption(Setting<?> setting) {
        if (setting instanceof ModeSetting modeSetting) {
            return new SingleOption(modeSetting);
        }
        if (setting instanceof MultiModeSetting multiModeSetting) {
            return new MultiOption(multiModeSetting);
        }
        if (setting instanceof NumberSetting numberSetting) {
            return new SliderOption(numberSetting, "");
        }
        if (setting instanceof BooleanSetting booleanSetting) {
            return new BooleanOption(booleanSetting);
        }
        if (setting instanceof StringSetting stringSetting) {
            return new TextOption(stringSetting);
        }
        if (setting instanceof ColorSetting colorSetting) {
            return new ColorOption(colorSetting);
        }
        if (setting instanceof BindSetting bindSetting) {
            return new BindOption(bindSetting);
        }
        if (setting instanceof ButtonSetting buttonSetting) {
            return new ButtonOption(buttonSetting, "Apply");
        }
        return null;
    }
}
