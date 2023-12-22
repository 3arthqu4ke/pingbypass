package me.earth.pingbypass.api.setting;

import me.earth.pingbypass.api.traits.HasDescription;
import me.earth.pingbypass.api.traits.Nameable;
import org.jetbrains.annotations.NotNull;

public interface Complexity extends Comparable<Complexity>, Nameable, HasDescription {
    int getComplexityValue();

    default boolean shouldDisplaySetting(Setting<?> setting) {
        return setting.getComplexity().compareTo(this) >= 0;
    }

    @Override
    default int compareTo(@NotNull Complexity o) {
        return Integer.compare(this.getComplexityValue(), o.getComplexityValue());
    }

}
