package me.earth.pingbypass.commons.platform;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.service.MixinService;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.UnaryOperator;

public interface PlatformService {
    void addToClassPath(Path path);

    void setMixinTransformer(UnaryOperator<IMixinTransformer> factory);

    default InputStream getResourceAsStream(String name) {
        return MixinService.getService().getResourceAsStream(name);
    }

    default Class<?> load(String name) throws ClassNotFoundException {
        return MixinService.getService().getClassProvider().findClass(name);
    }

    default void addMixinConfig(String config) {
        Mixins.addConfiguration(config);
    }

}
