package me.earth.pingbypass.api.setting.impl.types;

import com.mojang.brigadier.arguments.FloatArgumentType;
import me.earth.pingbypass.api.config.impl.Parsers;

public class FloatBuilder extends NumberSetting.Builder<Float, NumberSetting<Float>, FloatBuilder> {
    public FloatBuilder() {
        super(NumberSetting::new, FloatArgumentType::floatArg);
        this.withArgumentType(FloatArgumentType.floatArg())
                .withCompareFunction(Float::compareTo)
                .withParser(Parsers.FLOAT)
                .floating()
                .withValue(0.0f)
                .withMin(0.0f)
                .withMax(0.0f);
    }

}
