package me.earth.pingbypass.api.setting.impl;

import lombok.experimental.UtilityClass;
import me.earth.pingbypass.api.setting.Setting;

@UtilityClass
public class SettingUtil {
    @SuppressWarnings("unchecked")
    public static <T> void setValueUnchecked(Setting<T> setting, Object value) {
        setting.setValue((T) value);
    }

}
