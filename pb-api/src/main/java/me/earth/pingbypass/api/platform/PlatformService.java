package me.earth.pingbypass.api.platform;

import me.earth.pingbypass.api.launch.Transformer;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.service.MixinService;

import java.io.InputStream;
import java.nio.file.Path;

public interface PlatformService {
    void addToClassPath(Path path);

    default InputStream getResourceAsStream(String name) {
        return MixinService.getService().getResourceAsStream(name);
    }

    default Class<?> load(String name) throws ClassNotFoundException {
        return MixinService.getService().getClassProvider().findClass(name);
    }

    default void addMixinConfig(String config) {
        Mixins.addConfiguration(config);
    }

    Transformer.Registry injectTransformerRegistry();

}
