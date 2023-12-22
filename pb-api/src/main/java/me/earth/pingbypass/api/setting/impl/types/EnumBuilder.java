package me.earth.pingbypass.api.setting.impl.types;

import me.earth.pingbypass.api.command.impl.arguments.EnumArgument;
import me.earth.pingbypass.api.config.impl.Parsers;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.impl.BuilderWithFactory;
import me.earth.pingbypass.api.setting.impl.SettingImpl;

public class EnumBuilder<T extends Enum<T>> extends BuilderWithFactory<T, Setting<T>, EnumBuilder<T>> {
    public EnumBuilder(Class<T> type) {
        this(type.getEnumConstants()[0]);
    }

    public EnumBuilder(T initial) {
        super(SettingImpl::new);
        this.withArgumentType(new EnumArgument<>(initial.getDeclaringClass()))
                .withParser(Parsers.enumParser(initial.getDeclaringClass()))
                .withValue(initial);
    }

}
