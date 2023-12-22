package me.earth.pingbypass.api.command.impl.arguments;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.module.Module;
import me.earth.pingbypass.api.traits.Streamable;

@Getter
@RequiredArgsConstructor
public class ModuleArgument implements DescriptionArgumentType<Module> {
    private final String type = "module";
    private final Streamable<Module> nameables;

}
