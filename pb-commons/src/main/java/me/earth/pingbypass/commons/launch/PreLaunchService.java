package me.earth.pingbypass.commons.launch;

import me.earth.pingbypass.api.files.FileManager;
import me.earth.pingbypass.api.side.Side;
import me.earth.pingbypass.commons.platform.PlatformProvider;

import java.util.stream.Stream;

public interface PreLaunchService {
    void init(Side side);

    boolean isInitialized(Side side);

    TransformerRegistry getTransformerRegistry();

    FileManager getRootFileManager();

    PluginDiscoveryService getPluginDiscoveryService(Side side);

    FileManager getFileManager(Side side);

    Stream<Side> getLoadedSides();

    PlatformProvider getPlatformProvider();

    default void assertInitialized(Side side) {
        if (!isInitialized(side)) {
            throw new IllegalStateException("PreMixinService has not been initialized for side " + side.getName());
        }
    }

}
