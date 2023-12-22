package me.earth.pingbypass.commons.resource;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.commons.launch.PreLaunchServiceImpl;
import me.earth.pingbypass.commons.mixins.resource.IPackRepository;

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
