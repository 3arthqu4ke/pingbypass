package me.earth.pingbypass.api.event.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import me.earth.pingbypass.api.event.api.EventBus;

@RequiredArgsConstructor
public class DelegatingEventBus implements EventBus {
    @Delegate
    private final EventBus eventBus;

}
