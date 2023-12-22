package me.earth.pingbypass.api.setting.impl.types.container;

import com.mojang.brigadier.arguments.ArgumentType;
import lombok.AccessLevel;
import lombok.Getter;
import me.earth.pingbypass.api.command.impl.arguments.ContainerArgumentType;
import me.earth.pingbypass.api.command.impl.arguments.DummyArgumentType;
import me.earth.pingbypass.api.config.ConfigType;
import me.earth.pingbypass.api.config.JsonParser;
import me.earth.pingbypass.api.config.impl.Parsers;
import me.earth.pingbypass.api.setting.Complexity;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.impl.AbstractSettingBuilder;
import me.earth.pingbypass.api.setting.impl.SettingImpl;
import me.earth.pingbypass.api.traits.CanBeVisible;
import me.earth.pingbypass.api.traits.Nameable;
import me.earth.pingbypass.api.traits.NameableImpl;
import me.earth.pingbypass.api.traits.Streamable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

public class ContainerSetting<T extends Nameable> extends SettingImpl<Container<T>> implements Streamable<T> {
    private final ArgumentType<Container<T>> actualArgumentType;

    public ContainerSetting(Function<Container<T>, Component> componentFactory, CanBeVisible visibility, Function<Setting<Container<T>>, ArgumentType<Container<T>>> argumentType, Complexity complexity,
                            JsonParser<Container<T>> parser, ConfigType configType, String description,
                            Container<T> defaultValue, String name) {
        super(componentFactory, visibility, DummyArgumentType.dummy(), complexity, parser, configType, description, defaultValue, name);
        this.actualArgumentType = argumentType.apply(this);
    }

    @Override
    public boolean setValue(Container<T> value) {
        if (super.setValue(value)) {
            if (value.getAction() != null) {
                value.getAction().apply(value);
            }

            return true;
        }

        return false;
    }

    @Override
    public ArgumentType<Container<T>> getArgumentType() {
        return actualArgumentType;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return getValue().iterator();
    }

    @NotNull
    @Override
    public Stream<T> stream() {
        return getValue().stream();
    }

    private static <T extends Nameable> Function<Container<T>, Component> defaultComponentFactory() {
        return container -> {
            MutableComponent component = Component.literal("");
            Iterator<T> itr = container.iterator();
            while (itr.hasNext()) {
                component.append(Component.literal(itr.next().getName()));
                if (itr.hasNext()) {
                    component.append(", ");
                }
            }

            return component;
        };
    }

    @Getter(AccessLevel.PROTECTED)
    public static class Builder<T extends Nameable> extends AbstractSettingBuilder<Container<T>, ContainerSetting<T>, Builder<T>> {
        private Function<Setting<Container<T>>, ArgumentType<Container<T>>> argumentTypeFactory;

        public Builder() {
            withComponentFactory(defaultComponentFactory()).withArgumentType(DummyArgumentType.dummy());
        }

        public Builder<T> withArgumentTypeFactory(Function<Setting<Container<T>>, ArgumentType<Container<T>>> argumentTypeFactory) {
            this.argumentTypeFactory = argumentTypeFactory;
            return getSelf();
        }

        @Override
        public Builder<T> withArgumentType(ArgumentType<Container<T>> argumentType) {
            this.argumentTypeFactory = s -> argumentType;
            return super.withArgumentType(argumentType);
        }

        @Override
        protected ContainerSetting<T> create() {
            return new ContainerSetting<>(getComponentFactory(), getVisibility(), getArgumentTypeFactory(), getComplexity(), getParser(), getConfigType(), getDescription(), getDefaultValue(), getName());
        }

        public static Builder<Nameable> of(String... values) {
            return new Builder<>()
                    .withValue(Container.of(values))
                    .withParser(Parsers.CONTAINER)
                    .withArgumentTypeFactory(s -> new ContainerArgumentType<>(Parsers.NAME, NameableImpl.simpleArgumentType(), Arrays.asList(values), s));
        }
    }

}
