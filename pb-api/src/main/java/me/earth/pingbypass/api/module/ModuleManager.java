package me.earth.pingbypass.api.module;

import me.earth.pingbypass.api.registry.GetsByClass;
import me.earth.pingbypass.api.registry.Registry;

import java.util.stream.Stream;

public interface ModuleManager extends Registry<Module>, GetsByClass<Module> {
    CategoryManager getCategoryManager();

    default Stream<Module> getModulesByCategory(Category category) {
        return stream().filter(module -> module.getCategory().equals(category));
    }

}
