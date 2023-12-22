package me.earth.pingbypass.api.setting.impl.types;

import com.mojang.brigadier.arguments.BoolArgumentType;
import me.earth.pingbypass.api.config.impl.Parsers;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.impl.BuilderWithFactory;
import me.earth.pingbypass.api.setting.impl.SettingImpl;

public class BoolBuilder extends BuilderWithFactory<Boolean, Setting<Boolean>, BoolBuilder> {
    public BoolBuilder() {
        super(SettingImpl::new);
        this.withArgumentType(BoolArgumentType.bool()).withParser(Parsers.BOOL).withValue(false);
    }

}
