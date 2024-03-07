package me.earth.pingbypass.api.security;

import me.earth.pingbypass.api.config.properties.Property;

import static me.earth.pingbypass.api.config.properties.PropertyHelper.bool;
import static me.earth.pingbypass.api.config.properties.PropertyHelper.string;

public interface SecurityProperties {
    Property<Boolean> ENABLED = bool("pb.security.enabled", false);
    Property<String> KEY_STORE_NAME = string("pb.keystore.name", ".keystore");
    Property<String> KEY_STORE_PASSWORD = string("pb.keystore.password", null);
    Property<String> KEY_ALIAS = string("pb.key.alias", "pingbypass");
    Property<String> KEY_PASSWORD = string("pb.key.password", null);

}
