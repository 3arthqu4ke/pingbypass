package me.earth.pingbypass.api.config;

import me.earth.pingbypass.api.command.CommandManager;
import me.earth.pingbypass.api.traits.HasInfo;

public class ConfigType extends HasInfo {
    /**
     * Represents the {@link Config} type that delegates its methods to a number of other configs.
     * This is typically the task of a {@link CommandManager}.
     */
    public static final ConfigType ALL = new ConfigType("all", "Configures all configs at the same time.");

    public ConfigType(String name, String description) {
        super(name, description);
    }

}
