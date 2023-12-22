package me.earth.pingbypass.api.config.impl;

import me.earth.pingbypass.api.config.Config;
import me.earth.pingbypass.api.config.ConfigException;
import me.earth.pingbypass.api.config.ConfigType;
import me.earth.pingbypass.api.traits.Nameable;
import me.earth.pingbypass.api.traits.Streamable;
import me.earth.pingbypass.api.util.exceptions.ExceptionUtil;

// TODO:? forgot what this was for?
interface MultiConfig extends Config<Nameable> {
    Config<?> getPrimaryConfig();

    Streamable<Config<?>> getConfigs();

    @Override
    default ConfigType getConfigType() {
        return getPrimaryConfig().getConfigType();
    }

    @Override
    default void refresh() throws ConfigException {
        ExceptionUtil.forEach(getConfigs(), Config::refresh);
    }

    @Override
    default void save() throws ConfigException {
        ExceptionUtil.forEach(getConfigs(), Config::save);
    }

    @Override
    default void save(String name) throws ConfigException {
        ExceptionUtil.forEach(getConfigs(), config -> config.save(name));
    }

    @Override
    default void load(String name) throws ConfigException {
        ExceptionUtil.forEach(getConfigs(), config -> config.load(name));
    }

    @Override
    default void delete(String name) throws ConfigException {
        ExceptionUtil.forEach(getConfigs(), config -> config.delete(name));
    }

    @Override
    default String getName() {
        return Config.super.getName();
    }

    @Override
    default String getDescription() {
        return Config.super.getDescription();
    }

}
