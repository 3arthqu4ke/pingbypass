package me.earth.pingbypass.server.handlers.configuration;

import com.mojang.serialization.Lifecycle;
import lombok.Getter;
import lombok.SneakyThrows;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.WorldStem;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.Difficulty;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.*;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.storage.PrimaryLevelData;

import java.util.ArrayList;
import java.util.List;

/**
 * I do not really understand how {@link net.minecraft.network.protocol.configuration.ClientboundRegistryDataPacket} works, so I did this.
 * Code is lent from the GameTestServer.
 */
@Getter
public class ConfigWorldStem {
    private static final WorldOptions WORLD_OPTIONS = new WorldOptions(0L, false, false);
    private static final GameRules TEST_GAME_RULES = Util.make(new GameRules(), gameRules -> {
        gameRules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false, null);
        gameRules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, null);
    });

    @SneakyThrows
    public static WorldStem load(Minecraft mc) {
        PackRepository pr = mc.getResourcePackRepository();
        WorldDataConfiguration wdc = new WorldDataConfiguration(new DataPackConfig(new ArrayList<>(pr.getAvailableIds()), List.of()), FeatureFlags.REGISTRY.allFlags());
        LevelSettings lv2 = new LevelSettings("Test Level", GameType.CREATIVE, false, Difficulty.NORMAL, true, TEST_GAME_RULES, wdc);
        WorldLoader.PackConfig wlpc = new WorldLoader.PackConfig(pr, wdc, false, true);
        WorldLoader.InitConfig wlic = new WorldLoader.InitConfig(wlpc, Commands.CommandSelection.DEDICATED, 4);
        return Util.blockUntilDone(
            executor -> WorldLoader.load(
                wlic,
                loadContext -> {
                    Registry<LevelStem> lvx = new MappedRegistry<>(Registries.LEVEL_STEM, Lifecycle.stable()).freeze();
                    WorldDimensions.Complete lv2x = loadContext.datapackWorldgen()
                            .registryOrThrow(Registries.WORLD_PRESET)
                            .getHolderOrThrow(WorldPresets.FLAT)
                            .value()
                            .createWorldDimensions()
                            .bake(lvx);
                    return new WorldLoader.DataLoadOutput<>(
                            new PrimaryLevelData(lv2, WORLD_OPTIONS, lv2x.specialWorldProperty(), lv2x.lifecycle()), lv2x.dimensionsRegistryAccess()
                    );
                },
                WorldStem::new,
                Util.backgroundExecutor(),
                executor
            )
        ).get();
    }

}
