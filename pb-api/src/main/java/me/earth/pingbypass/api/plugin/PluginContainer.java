package me.earth.pingbypass.api.plugin;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

// TODO: boolean loaded, allow un- and reloading
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PluginContainer extends PluginConfigContainer {
    private final Plugin plugin;

    public PluginContainer(Plugin plugin, @Nullable Path path, PluginConfig config) {
        super(config, path);
        this.plugin = plugin;
    }

}
