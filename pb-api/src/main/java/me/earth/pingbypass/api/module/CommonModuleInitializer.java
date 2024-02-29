package me.earth.pingbypass.api.module;

import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.module.ModuleManager;
import me.earth.pingbypass.api.module.client.Notifications;
import me.earth.pingbypass.api.module.misc.AutoRespawn;
import me.earth.pingbypass.api.module.misc.Potions;
import me.earth.pingbypass.api.module.movement.Velocity;
import me.earth.pingbypass.api.module.render.Fullbright;
import me.earth.pingbypass.api.module.render.NoRender;

public interface CommonModuleInitializer {
    default void registerCommonModules(PingBypass pb) {
        ModuleManager modules = pb.getModuleManager();

        modules.register(new Notifications(pb));

        modules.register(new AutoRespawn(pb));
        modules.register(new Potions(pb));

        modules.register(new Velocity(pb));

        modules.register(new Fullbright(pb));
        modules.register(new NoRender(pb));
    }

}
