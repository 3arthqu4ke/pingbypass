package me.earth.pingbypass.api.module;

import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.event.api.Subscriber;
import me.earth.pingbypass.api.setting.SettingRegistry;
import me.earth.pingbypass.api.traits.HasDescription;
import me.earth.pingbypass.api.traits.Nameable;
import me.earth.pingbypass.api.traits.Toggleable;

public interface Module extends Nameable, Subscriber, SettingRegistry, Toggleable, HasDescription {
    Nameable getCategory();

    PingBypass getPingBypass();

}
