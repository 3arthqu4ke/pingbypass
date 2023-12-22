package me.earth.pingbypass.api.setting.impl.types;

import me.earth.pingbypass.api.command.impl.arguments.BindArgument;
import me.earth.pingbypass.api.config.impl.ConfigTypes;
import me.earth.pingbypass.api.config.impl.Parsers;
import me.earth.pingbypass.api.input.Bind;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.impl.BuilderWithFactory;
import me.earth.pingbypass.api.setting.impl.SettingImpl;

public class BindBuilder extends BuilderWithFactory<Bind, Setting<Bind>, BindBuilder> {
    public BindBuilder() {
        super(SettingImpl::new);
        this.withArgumentType(BindArgument.empty())
                .withComponentFactory(Bind::getComponent)
                .withConfigType(ConfigTypes.BINDS)
                .withParser(Parsers.BINDS)
                .withValue(Bind.none());
    }

}
