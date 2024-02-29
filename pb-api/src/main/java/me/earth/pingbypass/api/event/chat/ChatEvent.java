package me.earth.pingbypass.api.event.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.event.event.CancellableEvent;

@Getter
@RequiredArgsConstructor
public class ChatEvent extends CancellableEvent {
    private final String message;

}
