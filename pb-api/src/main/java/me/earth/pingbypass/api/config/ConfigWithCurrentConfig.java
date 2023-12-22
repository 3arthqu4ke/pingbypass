package me.earth.pingbypass.api.config;

import me.earth.pingbypass.api.traits.Nameable;

public interface ConfigWithCurrentConfig<C extends Nameable> extends Config<C> {
    C getCurrentConfig();

}
