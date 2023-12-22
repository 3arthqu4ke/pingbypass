package me.earth.pingbypass.api.config.impl;

import me.earth.pingbypass.api.config.ConfigType;

public interface ConfigTypes {
    ConfigType SETTINGS = new ConfigType("settings", "Configures the settings of modules.");
    ConfigType STYLE = new ConfigType("render", "Configures settings which only have a cosmetic impact.");
    ConfigType BINDS = new ConfigType("binds", "Configures bind settings.");

    ConfigType FRIENDS = new ConfigType("friends", "Configures friended players.");
    ConfigType ENEMIES = new ConfigType("enemies", "Configures enemy players.");

}
