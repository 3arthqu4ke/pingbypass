package me.earth.pingbypass.api.plugin.impl;

import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.Command;
import me.earth.pingbypass.api.command.impl.module.ModuleCommand;
import me.earth.pingbypass.api.event.api.EventListener;
import me.earth.pingbypass.api.event.api.Subscriber;
import me.earth.pingbypass.api.module.Module;
import me.earth.pingbypass.api.plugin.Plugin;
import me.earth.pingbypass.api.registry.Registry;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.SettingRegistry;
import me.earth.pingbypass.api.traits.Nameable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Makes it easier to write {@link Plugin}s that support unloading.
 */
@RequiredArgsConstructor
public class PluginUnloadingService {
    private final Map<Registry<?>, List<Nameable>> registries = new HashMap<>();
    private final Map<Subscriber, List<EventListener<?>>> listeners = new HashMap<>();
    private final List<ModuleAddOn<?>> addOns = new ArrayList<>();
    private final List<Subscriber> subscribers = new ArrayList<>();
    private final List<Runnable> runOnUnload = new ArrayList<>();
    private final PingBypass pingBypass;

    public boolean registerCommand(Command command) {
        return register(pingBypass.getCommandManager(), command);
    }

    public boolean registerModule(Module module) {
        if (register(pingBypass.getModuleManager(), module)) {
            if (!register(pingBypass.getCommandManager(),
                    new ModuleCommand(module))) {
                pingBypass.getModuleManager().unregister(module);
                return false;
            }

            return true;
        }

        return false;
    }

    public boolean registerModuleWithoutCommand(Module module) {
        return register(pingBypass.getModuleManager(), module);
    }

    public boolean registerSetting(SettingRegistry module, Setting<?> setting) {
        return register(module, setting);
    }

    public boolean register(Module module) {
        return register(pingBypass.getModuleManager(), module);
    }

    public void registerSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void subscribe(Subscriber subscriber) {
        pingBypass.getEventBus().subscribe(subscriber);
        this.registerSubscriber(subscriber);
    }

    public void runOnUnload(Runnable runnable) {
        runOnUnload.add(runnable);
    }

    // TODO: test
    public void registerListener(Subscriber subscriber, EventListener<?> listener) {
        listeners.computeIfAbsent(subscriber, v -> new ArrayList<>()).add(listener);
        subscriber.getListeners().add(listener);
    }

    // TODO: test
    public void registerAddOn(ModuleAddOn<?> addOn) {
        addOns.add(addOn);
        addOn.add();
    }

    public <T extends Nameable> boolean register(Registry<T> registry, T nameable) {
        if (registry.register(nameable)) {
            if (nameable instanceof Subscriber subscriber) {
                registerSubscriber(subscriber);
            }

            registries.computeIfAbsent(registry, v -> new ArrayList<>()).add(nameable);
            return true;
        }

        return false;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void unload() {
        registries.forEach((key, value) -> value.forEach(nameable -> ((Registry) key).unregister(nameable)));
        subscribers.forEach(subscriber -> pingBypass.getEventBus().unsubscribe(subscriber));
        listeners.forEach((key, value) -> value.forEach(listener -> {
            key.getListeners().remove(listener);
            pingBypass.getEventBus().unregister(listener);
        }));
        addOns.forEach(ModuleAddOn::remove);
        runOnUnload.forEach(Runnable::run);
    }

}
