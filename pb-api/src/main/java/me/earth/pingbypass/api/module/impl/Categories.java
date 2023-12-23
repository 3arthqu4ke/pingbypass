package me.earth.pingbypass.api.module.impl;

import me.earth.pingbypass.api.module.Category;
import me.earth.pingbypass.api.module.CategoryManager;
import me.earth.pingbypass.api.registry.impl.OrderedRegistryImpl;

public class Categories extends OrderedRegistryImpl<Category> implements CategoryManager {
    public static final Category COMBAT = new Category("Combat", 0xffff0000);
    public static final Category MISC = new Category("Misc", 0xffff0000);
    public static final Category RENDER = new Category("Render", 0xffff0000);
    public static final Category MOVEMENT = new Category("Movement", 0xffff0000);
    public static final Category CLIENT = new Category("Client", 0xffff0000);

    public Categories() {
        this.register(COMBAT);
        this.register(MISC);
        this.register(RENDER);
        this.register(MOVEMENT);
        this.register(CLIENT);
    }

}
