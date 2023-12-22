package me.earth.pingbypass.api.setting;

public interface HoldsValue<T> {
    T getValue();

    boolean setValue(T value);

}
