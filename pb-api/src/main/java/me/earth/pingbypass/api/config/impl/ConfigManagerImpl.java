package me.earth.pingbypass.api.config.impl;

import me.earth.pingbypass.api.config.ConfigException;
import me.earth.pingbypass.api.config.ConfigManager;
import me.earth.pingbypass.api.config.ConfigWithCurrentConfig;
import me.earth.pingbypass.api.registry.impl.ConflictSolvers;
import me.earth.pingbypass.api.registry.impl.SortedRegistry;
import me.earth.pingbypass.api.util.exceptions.ExceptionUtil;

public class ConfigManagerImpl extends SortedRegistry<ConfigWithCurrentConfig<?>> implements ConfigManager {
    public ConfigManagerImpl() {
        super(ConflictSolvers.error());
    }

    @Override
    public void refresh() throws ConfigException {
        ExceptionUtil.forEach(this, ConfigWithCurrentConfig::refresh);
    }

    @Override
    public void save() throws ConfigException {
        ExceptionUtil.forEach(this, ConfigWithCurrentConfig::save);
    }

    @Override
    public void save(String name) throws ConfigException {
        ExceptionUtil.forEach(this, config -> config.save(name));
    }

    @Override
    public void load(String name) throws ConfigException {
        ExceptionUtil.forEach(this, config -> config.load(name));
    }

    @Override
    public void delete(String name) throws ConfigException {
        ExceptionUtil.forEach(this, config -> config.delete(name));
    }

}
