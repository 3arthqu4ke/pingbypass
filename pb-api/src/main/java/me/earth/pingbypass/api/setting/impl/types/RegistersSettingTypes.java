package me.earth.pingbypass.api.setting.impl.types;

import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.SettingRegistry;

public interface RegistersSettingTypes extends SettingRegistry {
    default BoolBuilder boolBuilder(String name, boolean value) {
        return new BoolBuilder().withName(name).withValue(value);
    }

    default Setting<Boolean> bool(String name, boolean value, String description) {
        return boolBuilder(name, value).registerOnBuild(this).withDescription(description).build();
    }

    default StringSettingBuilder stringBuilder(String name, String value) {
        return new StringSettingBuilder().withName(name).withValue(value);
    }

    default Setting<String> string(String name, String value, String description) {
        return stringBuilder(name, value).registerOnBuild(this).withDescription(description).build();
    }

    default NumberSetting<Integer> number(String name, int value, int min, int max, String description) {
        return intBuilder(name, value, min, max).registerOnBuild(this).withDescription(description).build();
    }

    default IntBuilder intBuilder(String name, int value, int min, int max) {
        return new IntBuilder().withName(name).withMin(min).withMax(max).withValue(value);
    }

    default NumberSetting<Float> floating(String name, float value, float min, float max, String description) {
        return floatBuilder(name, value, min, max).registerOnBuild(this).withDescription(description).build();
    }

    default FloatBuilder floatBuilder(String name, float value, float min, float max) {
        return new FloatBuilder().withName(name).withMin(min).withMax(max).withValue(value);
    }

    default NumberSetting<Double> precise(String name, double value, double min, double max, String description) {
        return doubleBuilder(name, value, min, max).registerOnBuild(this).withDescription(description).build();
    }

    default DoubleBuilder doubleBuilder(String name, double value, double min, double max) {
        return new DoubleBuilder().withName(name).withMin(min).withMax(max).withValue(value);
    }

    default <T extends Enum<T>> Setting<T> constant(String name, T value, String description) {
        return enumBuilder(name, value).withDescription(description).registerOnBuild(this).build();
    }

    default <T extends Enum<T>> EnumBuilder<T> enumBuilder(String name, T value) {
        return new EnumBuilder<T>(value).withName(name);
    }

}
