package me.earth.pingbypass.commons.protocol;

import lombok.experimental.StandardException;

@StandardException
public class ProtocolException extends Exception {
    public ProtocolException(String message) {
        super(message);
    }

}
