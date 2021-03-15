package me.earth.pingbypass.client.managers;

import me.earth.earthhack.api.module.Module;
import me.earth.pingbypass.client.modules.autocrystal.AutoCrystal;
import me.earth.pingbypass.client.modules.autototem.AutoTotem;
import me.earth.pingbypass.client.modules.fakeplayer.FakePlayer;
import me.earth.pingbypass.client.modules.inventory.InventoryModule;
//import me.earth.pingbypass.client.modules.pingspoof.PingSpoof;
import me.earth.pingbypass.client.modules.safety.Safety;
import me.earth.pingbypass.client.modules.servermodule.ServerModule;
import me.earth.pingbypass.client.modules.surround.Surround;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager
{
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager()
    {
        modules.add(AutoCrystal.getInstance());
        modules.add(ServerModule.getInstance());
        modules.add(AutoTotem.getInstance());
        modules.add(FakePlayer.getInstance());
        modules.add(InventoryModule.getInstance());
        modules.add(Safety.getInstance());
        modules.add(Surround.getInstance());
        //modules.add(PingSpoof.getInstance());
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(String name)
    {
        for (Module module : modules)
        {
            if (module.getName().equalsIgnoreCase(name))
            {
                return (T) module;
            }
        }

        return null;
    }

    public List<Module> getModules()
    {
        return modules;
    }

}
