package me.earth.pingbypass.api.setting.impl.types;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.earth.pingbypass.api.config.impl.Parsers;

public class IntBuilder extends NumberSetting.Builder<Integer, NumberSetting<Integer>, IntBuilder> {
    public IntBuilder() {
        super(NumberSetting::new, IntegerArgumentType::integer);
        this.withArgumentType(IntegerArgumentType.integer())
                .withCompareFunction(Integer::compareTo)
                .withParser(Parsers.INT)
                .withValue(0)
                .withMax(0)
                .withMin(0);
    }

}
