package me.earth.pingbypass.api.event.event;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class CancellableEvent {
    private boolean cancelled;

}
