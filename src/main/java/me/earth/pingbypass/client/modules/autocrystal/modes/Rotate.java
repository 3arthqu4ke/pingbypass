package me.earth.pingbypass.client.modules.autocrystal.modes;

public enum Rotate
{
    None
        {
            @Override
            public boolean noRotate(Rotate rotate)
            {
                return true;
            }
        },
    Break
        {
            @Override
            public boolean noRotate(Rotate rotate)
            {
                return rotate == Place;
            }
        },
    Place
        {
            @Override
            public boolean noRotate(Rotate rotate)
            {
                return rotate == Break;
            }
        },
    All
        {
            @Override
            public boolean noRotate(Rotate rotate)
            {
                return false;
            }
        };

    public abstract boolean noRotate(Rotate rotate);
}
