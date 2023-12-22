package me.earth.pingbypass.api.command.impl.arguments;

import lombok.Getter;
import me.earth.pingbypass.api.config.Config;
import me.earth.pingbypass.api.config.ConfigManager;
import me.earth.pingbypass.api.traits.Streamable;
import me.earth.pingbypass.api.util.ExtendedStreamable;

@Getter
public class ConfigTypeArgument implements DescriptionArgumentType<Config<?>> {
    private final String type = "config";
    private final Streamable<Config<?>> nameables;

    public ConfigTypeArgument(ConfigManager configs) {
        this.nameables = new ExtendedStreamable<>(configs, configs);
    }

}
