package me.earth.pingbypass.api.setting.impl.types;

import me.earth.pingbypass.api.command.impl.arguments.EnumArgument;
import me.earth.pingbypass.api.config.impl.Parsers;
import me.earth.pingbypass.api.setting.impl.BuilderWithFactory;

public class EnumBuilder<T extends Enum<T>> extends BuilderWithFactory<T, EnumSetting<T>, EnumBuilder<T>> {
    public EnumBuilder(Class<T> type) {
        this(type.getEnumConstants()[0]);
    }

    public EnumBuilder(T initial) {
        super(EnumSetting::new);
        this.withArgumentType(new EnumArgument<>(initial.getDeclaringClass()))
                .withParser(Parsers.enumParser(initial.getDeclaringClass()))
                .withValue(initial);
    }

}
