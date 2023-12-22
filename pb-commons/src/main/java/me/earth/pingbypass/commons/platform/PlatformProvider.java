package me.earth.pingbypass.commons.platform;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.platform.Platform;
import me.earth.pingbypass.commons.util.ReflectionUtil;

@Slf4j
@Getter
@RequiredArgsConstructor
public class PlatformProvider {
    private final Platform current;
    private final PlatformService platformService;

    public static PlatformProvider detect() {
        Platform platform; PlatformService platformService;
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
        return new PlatformProvider(platform, platformService);
    }

}
