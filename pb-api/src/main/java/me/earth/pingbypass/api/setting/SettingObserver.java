package me.earth.pingbypass.api.setting;

public interface SettingObserver<T> {
    void onValue(SettingEvent<T> event);

}
