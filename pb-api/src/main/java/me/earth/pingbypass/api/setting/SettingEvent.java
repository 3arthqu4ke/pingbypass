package me.earth.pingbypass.api.setting;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.earth.pingbypass.api.event.event.CancellableEvent;

@Data
@EqualsAndHashCode(callSuper = false)
public class SettingEvent<T> extends CancellableEvent {
    private final Setting<T> setting;
    private T value;

    public SettingEvent(Setting<T> setting, T value) {
        this.setting = setting;
        this.value = value;
    }

}
