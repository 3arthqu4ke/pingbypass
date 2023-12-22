package me.earth.pingbypass.api.module;

import lombok.Data;
import me.earth.pingbypass.api.traits.Nameable;

// TODO this, Icon?
@Data
public class Category implements Nameable {
    private final String name;
    /**
     * The RGB color of this category. (No Alpha!)
     */
    private final int color;
    // private final ResourceLocation icon;

}
