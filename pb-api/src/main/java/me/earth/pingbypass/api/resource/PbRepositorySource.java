package me.earth.pingbypass.api.resource;

import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.plugin.PluginConfig;
import me.earth.pingbypass.api.plugin.PluginConfigContainer;
import me.earth.pingbypass.api.Constants;
import me.earth.pingbypass.api.launch.PluginDiscoveryService;
import me.earth.pingbypass.api.launch.PreLaunchService;
import me.earth.pingbypass.api.util.StreamUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.flag.FeatureFlagSet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@MethodsReturnNonnullByDefault
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
                                    new Pack.ResourcesSupplier() {
                                        @Override
                                        public PackResources openPrimary(String id) {
                                            return new PbPackResources(namespace, container.getPath());
                                        }

                                        @Override
                                        public PackResources openFull(String id, Pack.Info info) {
                                            return new PbPackResources(namespace, container.getPath());
                                        }
                                    },
                                    getInfo(namespace),
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
                PackCompatibility.COMPATIBLE,
                /*Constants.PACK_FORMAT,*/
                FeatureFlagSet.of(),
                new ArrayList<>());
    }

    private PluginConfigContainer getPingBypassContainer() {
        PluginConfig config = new PluginConfig();
        config.setName(Constants.NAME);
        return new PluginConfigContainer(config, null);
    }

}
