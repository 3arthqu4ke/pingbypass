package me.earth.pingbypass.api.command.impl.arguments;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.traits.Streamable;

@Getter
@RequiredArgsConstructor
public final class SettingArgument implements DescriptionArgumentType<Setting<?>> {
    private final String type = "setting";
    private final Streamable<Setting<?>> nameables;

}
