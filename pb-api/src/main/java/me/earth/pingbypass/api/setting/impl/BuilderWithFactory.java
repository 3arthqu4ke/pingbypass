package me.earth.pingbypass.api.setting.impl;

import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.setting.Setting;

@RequiredArgsConstructor
public class BuilderWithFactory<T, S extends Setting<T>, B extends BuilderWithFactory<T, S, B>>
        extends AbstractSettingBuilder<T, S, B> {
    private final SettingFactoryFunction<T, S> factory;

    @Override
    protected S create() {
        return factory.create(getComponentFactory(), getVisibility(), getArgumentType(), getComplexity(),
                getParser(), getConfigType(), getDescription(), getDefaultValue(), getName());
    }

}
