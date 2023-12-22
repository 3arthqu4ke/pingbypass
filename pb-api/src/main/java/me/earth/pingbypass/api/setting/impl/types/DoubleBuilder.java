package me.earth.pingbypass.api.setting.impl.types;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import me.earth.pingbypass.api.config.impl.Parsers;

public class DoubleBuilder extends NumberSetting.Builder<Double, NumberSetting<Double>, DoubleBuilder> {
    public DoubleBuilder() {
        super(NumberSetting::new, DoubleArgumentType::doubleArg);
        this.withArgumentType(DoubleArgumentType.doubleArg())
                .withCompareFunction(Double::compareTo)
                .withParser(Parsers.DOUBLE)
                .floating()
                .withValue(0.0)
                .withMin(0.0)
                .withMax(0.0);
    }

}
