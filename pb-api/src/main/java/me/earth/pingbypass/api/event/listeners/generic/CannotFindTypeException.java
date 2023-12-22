package me.earth.pingbypass.api.event.listeners.generic;

import lombok.experimental.StandardException;

@StandardException
public class CannotFindTypeException extends RuntimeException {
    public CannotFindTypeException(String message) {
        super(message);
    }

}
