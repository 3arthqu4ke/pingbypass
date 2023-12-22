package me.earth.pingbypass.api.setting.impl.types;

import me.earth.pingbypass.api.command.impl.arguments.BindArgument;
import me.earth.pingbypass.api.input.Bind;
import me.earth.pingbypass.api.input.KeyboardAndMouse;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.SettingRegistry;

public interface RegistersBinds extends SettingRegistry {
    KeyboardAndMouse getKeyboardAndMouse();

    default Setting<Bind> bind(String name, String description) {
        return bindBuilder(name).withDescription(description).registerOnBuild(this).build();
    }

    default BindBuilder bindBuilder(String name) {
        return new BindBuilder().withArgumentType(new BindArgument(this.getKeyboardAndMouse())).withName(name);
    }

}
