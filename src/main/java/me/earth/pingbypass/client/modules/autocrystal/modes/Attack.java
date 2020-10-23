package me.earth.pingbypass.client.modules.autocrystal.modes;

import me.earth.earthhack.api.util.Globals;

public enum Attack implements Globals
{
    Always
        {
            @Override
            public boolean shouldAttack()
            {
                return false;
            }
        },
    BreakSlot
        {
            @Override
            public boolean shouldAttack()
            {
                return false;
            }
        },
    Calc
        {
            @Override
            public boolean shouldAttack()
            {
                return false;
            }
        };


    public abstract boolean shouldAttack();

}
