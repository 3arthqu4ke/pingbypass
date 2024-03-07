package me.earth.pingbypass.api.launch;

import net.minecraft.client.Minecraft;

public interface Initializer {
    void init(Minecraft mc, PreLaunchService preLaunchService);

}
