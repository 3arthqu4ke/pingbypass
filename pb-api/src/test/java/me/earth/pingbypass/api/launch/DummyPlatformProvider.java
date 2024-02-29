package me.earth.pingbypass.api.launch;

import me.earth.pingbypass.api.platform.Platform;
import me.earth.pingbypass.api.platform.PlatformProvider;
import me.earth.pingbypass.api.platform.PlatformService;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.UnaryOperator;

public class DummyPlatformProvider extends PlatformProvider implements PlatformService {
    public DummyPlatformProvider() {
        super(Platform.FABRIC, null);
    }

    @Override
    public PlatformService getPlatformService() {
        return this;
    }

    @Override
    public void addToClassPath(Path path) {
        throw new UnsupportedOperationException("addToClassPath");
    }

    @Override
    public void setMixinTransformer(UnaryOperator<IMixinTransformer> factory) {

    }

    @Override
    public InputStream getResourceAsStream(String name) {
        return DummyPlatformProvider.class.getClassLoader().getResourceAsStream(name);
    }

    @Override
    public Class<?> load(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }

}
