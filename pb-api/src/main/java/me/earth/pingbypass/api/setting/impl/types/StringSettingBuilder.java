package me.earth.pingbypass.api.setting.impl.types;

import me.earth.pingbypass.api.command.impl.arguments.StringArgument;
import me.earth.pingbypass.api.config.impl.Parsers;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.impl.BuilderWithFactory;
import me.earth.pingbypass.api.setting.impl.SettingImpl;

public class StringSettingBuilder extends BuilderWithFactory<String, Setting<String>, StringSettingBuilder> {
    public StringSettingBuilder() {
        super(SettingImpl::new);
        this.withArgumentType(StringArgument.string("string")).withParser(Parsers.STRING).withValue("");
    }

}
