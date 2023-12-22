package me.earth.pingbypass.api.plugin.impl;

import lombok.Getter;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.module.Module;
import me.earth.pingbypass.api.module.impl.ModuleImpl;

/**
 * Makes adding new Settings and EventListeners to an existing module easy.
 *
 * @param <T> the type of the targeted module.
 */
@Getter
public class ModuleAddOn<T extends Module> extends ModuleImpl {
    private final T target;

    public ModuleAddOn(PingBypass pingBypass, Class<T> target) {
        this(pingBypass, require(pingBypass, target));
    }

    public ModuleAddOn(PingBypass pingBypass, T target) {
        super(pingBypass, target.getName(), target.getCategory(), target.getDescription());
        stream().forEach(this::unregister);
        this.target = target;
    }

    public void add() {
        stream().forEach(target::register);
        getListeners().forEach(listener -> target.getListeners().add(listener));
    }

    public void remove() {
        stream().forEach(target::unregister);
        getListeners().forEach(listener -> {
            target.getListeners().remove(listener);
            getPingBypass().getEventBus().unregister(listener);
        });
    }

    private static <T extends Module> T require(PingBypass pingBypass, Class<T> moduleType) {
        return pingBypass.getModuleManager().getByClass(moduleType).orElseThrow();
    }

}
