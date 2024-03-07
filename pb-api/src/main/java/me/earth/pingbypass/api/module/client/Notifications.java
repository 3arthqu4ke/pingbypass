package me.earth.pingbypass.api.module.client;

import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.event.impl.PingBypassInitializedEvent;
import me.earth.pingbypass.api.event.listeners.generic.SubscriberListener;
import me.earth.pingbypass.api.module.impl.Categories;
import me.earth.pingbypass.api.module.impl.ModuleImpl;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.util.ModuleUtil;

public class Notifications extends ModuleImpl {
    public Notifications(PingBypass pingBypass) {
        super(pingBypass, "Notifications", Categories.CLIENT, "Notifies you about stuff.");
        Setting<Boolean> modules = bool("Modules", true, "Notifies you when you toggle a module.");
        pingBypass.getEventBus().subscribe(new SubscriberListener<PingBypassInitializedEvent>() {
            @Override
            public void onEvent(PingBypassInitializedEvent event) {
                event.getPingBypass()
                        .getModuleManager()
                        .stream()
                        .forEach(module -> module.getSetting("Enabled", Boolean.class).ifPresent(setting ->
                                setting.addPostObserver(settingEvent -> {
                                    if (isEnabled() && modules.getValue()) {
                                        ModuleUtil.sendModuleToggleMessage(pingBypass.getChat(), module, false);
                                    }
                                })));
                pingBypass.getEventBus().unsubscribe(this);
            }
        });
    }

}
