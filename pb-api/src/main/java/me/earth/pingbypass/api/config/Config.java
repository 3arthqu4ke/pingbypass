package me.earth.pingbypass.api.config;

import me.earth.pingbypass.api.traits.HasDescription;
import me.earth.pingbypass.api.traits.Nameable;
import me.earth.pingbypass.api.traits.Streamable;

public interface Config<C extends Nameable> extends Nameable, HasDescription, Streamable<C> {
    ConfigType getConfigType();

    void refresh() throws ConfigException;

    void save() throws ConfigException;

    void save(String name) throws ConfigException;

    void load(String name) throws ConfigException;

    void delete(String name) throws ConfigException;

    @Override
    default String getName() {
        return getConfigType().getName();
    }

    @Override
    default String getDescription() {
        return getConfigType().getDescription();
    }

}
