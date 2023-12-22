package me.earth.pingbypass.commons.launch;

import net.minecraft.client.Minecraft;

public interface Initializer {
    void init(Minecraft mc, PreLaunchService preLaunchService);

}
