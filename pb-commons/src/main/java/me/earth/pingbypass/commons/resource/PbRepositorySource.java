package me.earth.pingbypass.commons.resource;

import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.plugin.PluginConfig;
import me.earth.pingbypass.api.plugin.PluginConfigContainer;
import me.earth.pingbypass.commons.Constants;
import me.earth.pingbypass.commons.launch.PluginDiscoveryService;
import me.earth.pingbypass.commons.launch.PreLaunchService;
import me.earth.pingbypass.commons.util.StreamUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.flag.FeatureFlagSet;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@ExtensionMethod(StreamUtil.class)
final class PbRepositorySource implements RepositorySource {
    private final PreLaunchService preLaunchService;

    @Override
    public void loadPacks(@NotNull Consumer<Pack> consumer) {
        log.info("Loading PingBypass packs");
        preLaunchService
                .getLoadedSides()
                .map(preLaunchService::getPluginDiscoveryService)
                .flatMap(PluginDiscoveryService::getPluginConfigs)
                .add(getPingBypassContainer())
                .forEach(container -> {
                    String namespace = container.getConfig().getNameLowerCase();
                    consumer.accept(
                            Pack.create(
                                namespace,
                                Component.literal(container.getConfig().getName()),
                                true,
                                string -> new PbPackResources(namespace, container.getPath()),
                                getInfo(namespace),
                                PackType.CLIENT_RESOURCES,
                                Pack.Position.BOTTOM,
                                false, //fixedPosition?
                                PackSource.DEFAULT)
                    );
                });
    }

    private Pack.Info getInfo(String namespace) {
        return new Pack.Info(
                Constants.NAME_LOW.equals(namespace)
                        ? Component.literal("PingBypass resource pack")
                        : Component.literal("Plugin resource pack"),
                Constants.PACK_FORMAT,
                FeatureFlagSet.of());
    }

    private PluginConfigContainer getPingBypassContainer() {
        PluginConfig config = new PluginConfig();
        config.setName(Constants.NAME);
        return new PluginConfigContainer(config, null);
    }

}
