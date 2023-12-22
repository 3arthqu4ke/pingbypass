package me.earth.pingbypass.api.event.event;

import lombok.Getter;
import lombok.Setter;

public abstract class CancellableEvent {
    @Getter
    @Setter
    private boolean cancelled;

}
