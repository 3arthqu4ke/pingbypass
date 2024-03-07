package me.earth.pingbypass.api.module;

import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.module.client.Notifications;

public interface CommonModuleInitializer {
    default void registerCommonModules(PingBypass pb) {
        ModuleManager modules = pb.getModuleManager();
        modules.register(new Notifications(pb));
    }

}
