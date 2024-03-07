package me.earth.pingbypass.api.plugin;

import lombok.Data;
import lombok.experimental.Delegate;
import me.earth.pingbypass.api.traits.HasDescription;
import me.earth.pingbypass.api.traits.Nameable;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

@Data
public class PluginConfigContainer implements Nameable, HasDescription {
    @Delegate(types = {Nameable.class, HasDescription.class})
    private final PluginConfig config;
    @Nullable
    private final Path path;

}
