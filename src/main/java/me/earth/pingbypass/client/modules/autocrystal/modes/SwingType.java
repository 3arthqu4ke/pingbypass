package me.earth.pingbypass.client.modules.autocrystal.modes;

import net.minecraft.util.EnumHand;

public enum SwingType
{
    None
        {
            @Override
            public EnumHand getHand()
            {
                return null;
            }
        },
    MainHand
        {
            @Override
            public EnumHand getHand()
            {
                return EnumHand.MAIN_HAND;
            }
        },
    OffHand
        {
            @Override
            public EnumHand getHand()
            {
                return EnumHand.OFF_HAND;
            }
        };

    public abstract EnumHand getHand();
}
