package me.earth.pingbypass.api.config;

import me.earth.pingbypass.api.registry.Registry;

import java.util.Optional;

// TODO: it might be cool if we could also separate configs by platform?
/**
 * A registry for {@link Config}s.
 */
public interface ConfigManager extends Registry<ConfigWithCurrentConfig<?>>, Config<ConfigWithCurrentConfig<?>> {
    @Override
    default ConfigType getConfigType() {
        return ConfigType.ALL;
    }

    default Optional<ConfigWithCurrentConfig<?>> getByConfigType(ConfigType type) {
        return getByName(type.getName());
    }

}
