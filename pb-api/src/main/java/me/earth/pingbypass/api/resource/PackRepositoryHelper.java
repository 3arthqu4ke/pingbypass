package me.earth.pingbypass.api.resource;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.launch.PreLaunchServiceImpl;
import me.earth.pingbypass.api.mixins.resource.IPackRepository;

import java.util.LinkedHashSet;

@Slf4j
@UtilityClass
public class PackRepositoryHelper {
    public static void addPingBypassRepositorySource(IPackRepository repository) {
        if (repository.getSources().stream().noneMatch(PbRepositorySource.class::isInstance)) {
            var sources = new LinkedHashSet<>(repository.getSources());
            sources.add(new PbRepositorySource(PreLaunchServiceImpl.INSTANCE));
            repository.setSources(sources);
        }
    }

}
