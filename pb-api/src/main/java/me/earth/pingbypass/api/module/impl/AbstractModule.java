package me.earth.pingbypass.api.module.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.event.api.EventListener;
import me.earth.pingbypass.api.input.KeyboardAndMouse;
import me.earth.pingbypass.api.module.Module;
import me.earth.pingbypass.api.setting.impl.SettingRegistryImpl;
import me.earth.pingbypass.api.setting.impl.types.RegistersBinds;
import me.earth.pingbypass.api.setting.impl.types.RegistersSettingTypes;
import me.earth.pingbypass.api.traits.ListensToEvents;
import me.earth.pingbypass.api.traits.Nameable;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class AbstractModule extends SettingRegistryImpl
        implements Module, RegistersSettingTypes, RegistersBinds, ListensToEvents {
    private final List<EventListener<?>> listeners = new ArrayList<>();
    private final PingBypass pingBypass;
    private final String name;
    private final Nameable category;
    private final String description;

    @Override
    public KeyboardAndMouse getKeyboardAndMouse() {
        return getPingBypass().getKeyBoardAndMouse();
    }

}
