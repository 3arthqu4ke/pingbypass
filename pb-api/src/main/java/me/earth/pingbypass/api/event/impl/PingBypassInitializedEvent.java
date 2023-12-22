package me.earth.pingbypass.api.event.impl;

import lombok.Data;
import me.earth.pingbypass.PingBypass;

/**
 * Posted when a PingBypass instance has been initialized.
 */
@Data
public class PingBypassInitializedEvent {
    private final PingBypass pingBypass;

}
