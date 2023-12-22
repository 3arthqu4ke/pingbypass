package me.earth.pingbypass.api.module.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.module.CategoryManager;
import me.earth.pingbypass.api.module.Module;
import me.earth.pingbypass.api.module.ModuleManager;
import me.earth.pingbypass.api.registry.impl.SortedRegistry;

@Getter
@RequiredArgsConstructor
public class ModuleManagerImpl extends SortedRegistry<Module> implements ModuleManager {
    private final CategoryManager categoryManager;

}
