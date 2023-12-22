package net.minecraftforge.fml.common;

/**
 * Mock of Forge's Mod annotation.
 * This way do not need to include a forge dependency in this project.
 */
public @interface Mod {
    String value();
}
