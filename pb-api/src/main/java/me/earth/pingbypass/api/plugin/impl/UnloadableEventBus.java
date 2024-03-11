package me.earth.pingbypass.api.plugin.impl;

import me.earth.pingbypass.api.event.impl.DelegatingEventBus;

class UnloadableEventBus extends DelegatingEventBus {
    private final PluginUnloadingService unloadingService;

    public UnloadableEventBus(PluginUnloadingService unloadingService) {
        super(unloadingService.pingBypass.getEventBus());
        this.unloadingService = unloadingService;
    }

    @Override
    public void subscribe(Object subscriber) {
        unloadingService.subscribe(subscriber);
    }

}
