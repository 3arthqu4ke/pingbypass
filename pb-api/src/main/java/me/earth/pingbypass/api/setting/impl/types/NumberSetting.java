package me.earth.pingbypass.api.setting.impl.types;

import com.mojang.brigadier.arguments.ArgumentType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.config.ConfigType;
import me.earth.pingbypass.api.config.JsonParser;
import me.earth.pingbypass.api.setting.Complexity;
import me.earth.pingbypass.api.setting.impl.AbstractSettingBuilder;
import me.earth.pingbypass.api.setting.impl.SettingImpl;
import me.earth.pingbypass.api.traits.CanBeVisible;
import net.minecraft.network.chat.Component;

import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
public class NumberSetting<N extends Number> extends SettingImpl<N> {
    private final BiFunction<N, N, Integer> compareFunction;
    private final boolean floating;
    private final N min;
    private final N max;

    public NumberSetting(Function<N, Component> componentFactory, CanBeVisible visibility, ArgumentType<N> argumentType, Complexity complexity,
                         JsonParser<N> parser, ConfigType configType, String description, N defaultValue, String name,
                         BiFunction<N, N, Integer> compareFunction, N min, N max, boolean floating) {
        super(componentFactory, visibility, argumentType, complexity, parser, configType, description, defaultValue, name);
        this.compareFunction = compareFunction;
        this.floating = floating;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean setValue(N value) {
        if (compareFunction.apply(value, min) >= 0 && compareFunction.apply(value, max) <= 0) {
            return super.setValue(value);
        }

        return false;
    }

    @RequiredArgsConstructor
    @Getter(AccessLevel.PROTECTED)
    public static class Builder<N extends Number, S extends NumberSetting<N>, B extends Builder<N, S, B>>
            extends AbstractSettingBuilder<N, S, B> {
        private final NumberSettingFactoryFunction<N, S> factory;
        private final BiFunction<N, N, ArgumentType<N>> argumentTypeFactory;
        BiFunction<N, N, Integer> compareFunction;
        private boolean floating = false;
        private N min;
        private N max;

        public B withCompareFunction(BiFunction<N, N, Integer> compareFunction) {
            this.compareFunction = compareFunction;
            return getSelf();
        }

        public B withMin(N min) {
            this.min = min;
            return recalculateArgumentFactory();
        }

        public B withMax(N max) {
            this.max = max;
            return recalculateArgumentFactory();
        }

        protected B floating() {
            this.floating = true;
            return getSelf();
        }

        public B recalculateArgumentFactory() {
            return this.getMin() != null && this.getMax() != null
                    ? this.withArgumentType(argumentTypeFactory.apply(getMin(), getMax()))
                    : getSelf();
        }

        @Override
        protected S create() {
            return factory.create(getComponentFactory(), getVisibility(), getArgumentType(), getComplexity(), getParser(), getConfigType(),
                    getDescription(), getDefaultValue(), getName(), getCompareFunction(), getMin(), getMax(),
                    isFloating());
        }
    }

    @FunctionalInterface
    public interface NumberSettingFactoryFunction<N extends Number, S extends NumberSetting<N>> {
        S create(Function<N, Component> componentFactory, CanBeVisible visibility, ArgumentType<N> argumentType, Complexity complexity,
                 JsonParser<N> parser, ConfigType configType, String description, N defaultValue, String name,
                 BiFunction<N, N, Integer> compareFunction, N min, N max, boolean floating);
    }

}
