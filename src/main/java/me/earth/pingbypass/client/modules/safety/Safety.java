package me.earth.pingbypass.client.modules.safety;

import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;

public class Safety extends Module
{
    private static final Safety INSTANCE = new Safety();

    final Setting<Float> maxDamage      = register(new NumberSetting<>("MaxDamage", 4.0f, 0.0f, 36.0f));
    final Setting<Boolean> bedCheck     = register(new BooleanSetting("BedCheck", false));
    final Setting<Boolean> newerVersion = register(new BooleanSetting("1.13+", false));
    final Setting<Boolean> safetyPlayer = register(new BooleanSetting("SafetyPlayer", false));
    final Setting<UpdateMode> updates   = register(new EnumSetting<>("Updates", UpdateMode.Tick));
    final Setting<Integer> delay        = register(new NumberSetting<>("Delay", 25, 0, 100).withVisibility(v -> updates.getValue() == UpdateMode.Fast));

    private Safety()
    {
        super("S-Safety", Category.Client);
    }

    public static Safety getInstance()
    {
        return INSTANCE;
    }

    public float getMaxDamage()
    {
        return maxDamage.getValue();
    }

    public boolean newerVersion()
    {
        return newerVersion.getValue();
    }

    public boolean safetyPlayer()
    {
        return safetyPlayer.getValue();
    }

    public boolean shouldBedCheck()
    {
        return bedCheck.getValue();
    }

    public UpdateMode getMode()
    {
        return updates.getValue();
    }

    public int getDelay()
    {
        return delay.getValue();
    }

}
