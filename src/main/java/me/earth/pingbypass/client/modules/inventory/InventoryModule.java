package me.earth.pingbypass.client.modules.inventory;

import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.NumberSetting;

//TODO: Rn we only sync "hotspots" that get desynced often (armor, dragslot, offhand)
// In the future second delay to sync everything?
public class InventoryModule extends Module
{
    /** The Singleton Instance for this Module. */
    private static final InventoryModule INSTANCE = new InventoryModule();

    /** Interval in seconds in which the Inventory gets synced. */
    final Setting<Integer> delay = register(new NumberSetting<>("Delay", 5, 1, 60));

    private InventoryModule()
    {
        super("S-Inventory", Category.Client);
        this.listeners.add(new TickListener(this));
    }

    public static InventoryModule getInstance()
    {
        return INSTANCE;
    }

}
