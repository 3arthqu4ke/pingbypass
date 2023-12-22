package me.earth.pingbypass.api.security;

import lombok.experimental.StandardException;

@StandardException
public class SecurityException extends RuntimeException {
    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityException(Throwable throwable) {
        super(throwable);
    }

}
