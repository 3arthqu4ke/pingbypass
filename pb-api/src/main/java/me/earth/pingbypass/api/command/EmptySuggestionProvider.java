package me.earth.pingbypass.api.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@MethodsReturnNonnullByDefault
public interface EmptySuggestionProvider extends SharedSuggestionProvider {
    EmptySuggestionProvider INSTANCE = new EmptySuggestionProvider() {};

    @Override
    default Collection<String> getOnlinePlayerNames() {
        return Collections.emptyList();
    }

    @Override
    default Collection<String> getAllTeams() {
        return Collections.emptyList();
    }

    @Override
    default Stream<ResourceLocation> getAvailableSounds() {
        return Stream.empty();
    }

    @Override
    default Stream<ResourceLocation> getRecipeNames() {
        return Stream.empty();
    }

    @Override
    default CompletableFuture<Suggestions> customSuggestion(CommandContext<?> commandContext) {
        return Suggestions.empty();
    }

    @Override
    default Set<ResourceKey<Level>> levels() {
        return Collections.emptySet();
    }

    @Override
    default RegistryAccess registryAccess() {
        return RegistryAccess.EMPTY;
    }

    @Override
    default FeatureFlagSet enabledFeatures() {
        return FeatureFlagSet.of();
    }

    @Override
    default CompletableFuture<Suggestions> suggestRegistryElements(ResourceKey<? extends Registry<?>> arg,
                                                                  ElementSuggestionType arg2,
                                                                  SuggestionsBuilder suggestionsBuilder,
                                                                  CommandContext<?> commandContext) {
        return Suggestions.empty();
    }

    @Override
    default boolean hasPermission(int i) {
        return false;
    }

}
