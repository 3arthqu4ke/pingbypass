package me.earth.pingbypass.api.mixins.resource;

import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(PackRepository.class)
public interface IPackRepository {
    @Accessor("sources")
    Set<RepositorySource> getSources();

    @Mutable
    @Accessor("sources")
    void setSources(Set<RepositorySource> sources);

}
