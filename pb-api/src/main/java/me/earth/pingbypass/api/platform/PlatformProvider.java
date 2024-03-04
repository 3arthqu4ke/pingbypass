package me.earth.pingbypass.api.platform;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.util.ReflectionUtil;
import org.jetbrains.annotations.VisibleForTesting;

@Slf4j
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED, onConstructor_={@VisibleForTesting})
public class PlatformProvider {
    private static final PlatformProvider INSTANCE;

    private final PlatformService platformService;
    private final Platform current;

    public static PlatformProvider getInstance() {
        return INSTANCE;
    }

    static {
        Platform platform;
        PlatformService platformService;
        if (ReflectionUtil.doesClassExist("cpw.mods.modlauncher.Launcher")) {
            platform = Platform.FORGE;
            platformService = new ForgePlatformService();
        } else if (ReflectionUtil.doesClassExist("net.fabricmc.loader.impl.launch.FabricLauncher")) {
            platform = Platform.FABRIC;
            platformService = new FabricPlatformService();
        } else {
            throw new IllegalStateException("Could not detect Platform!");
        }

        log.info("Detected platform: " + platform.getName());
        INSTANCE = new PlatformProvider(platformService, platform);
    }

}
