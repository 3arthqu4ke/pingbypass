package me.earth.pingbypass.api.module.impl;

import lombok.Synchronized;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.SettingEvent;
import me.earth.pingbypass.api.traits.Nameable;
import net.minecraft.client.Minecraft;

import java.util.concurrent.atomic.AtomicBoolean;

// TODO: give every nameable a namespace, the plugin it came from, that could prevent naming conflicts!
public class ModuleImpl extends AbstractModule {
    private final AtomicBoolean reentrantOnEnable = new AtomicBoolean();
    private final AtomicBoolean currentlyEnabled = new AtomicBoolean();
    private final Setting<Boolean> enabled = bool("Enabled", false, "Toggles this module.");
    protected final Minecraft mc;

    public ModuleImpl(PingBypass pingBypass, String name, Nameable category, String description) {
        super(pingBypass, name, category, description);
        this.mc = pingBypass.getMinecraft();
        bind("Bind", "The key to press to toggle this module.");
        enabled.addPreObserver(this::onEnabledEvent);
    }

    @Override
    public final void enable() {
        if (!isEnabled()) {
            enabled.setValue(true);
        }
    }

    @Override
    public final void disable() {
        if (isEnabled()) {
            enabled.setValue(false);
        }
    }

    @Override
    public final boolean isEnabled() {
        return currentlyEnabled.get();
    }

    protected void onEnable() {
        // To be implemented by sub-classes
    }

    protected void onDisable() {
        // To be implemented by sub-classes
    }

    @Synchronized
    private void onEnabledEvent(SettingEvent<Boolean> event) {
        if (event.isCancelled()) {
            return;
        }

        currentlyEnabled.set(event.getValue());
        if (event.getValue() && !getPingBypass().getEventBus().isSubscribed(this)) {
            reentrantOnEnable.set(true);
            onEnable();
            reentrantOnEnable.set(false);
            if (currentlyEnabled.get()) {
                getPingBypass().getEventBus().subscribe(this);
            }
        } else if (!event.getValue() && (getPingBypass().getEventBus().isSubscribed(this) || reentrantOnEnable.get())) {
            getPingBypass().getEventBus().unsubscribe(this);
            onDisable();
        }
    }

}
