package me.earth.pingbypass.api.config;

import lombok.experimental.StandardException;

@StandardException
public class ConfigParseException extends Exception {
    public ConfigParseException(String message) {
        super(message);
    }

}
