package me.earth.pingbypass.api.setting.impl.types.container;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.config.JsonParser;
import me.earth.pingbypass.api.config.impl.Parsers;
import me.earth.pingbypass.api.traits.Nameable;

@RequiredArgsConstructor
public enum ContainerAction implements Nameable {
    ADD("add") {
        @Override
        public <V extends Nameable> void apply(Container<V> container) {
            if (container.getValue() != null) {
                container.register(container.getValue());
            }
        }
    },
    DEL("del") {
        @Override
        public <V extends Nameable> void apply(Container<V> container) {
            if (container.getValue() != null) {
                container.unregister(container.getValue());
            }
        }
    },
    CLEAR("clear") {
        @Override
        public <V extends Nameable> void apply(Container<V> container) {
            container.clear();
        }
    };

    public static final JsonParser<ContainerAction> PARSER = Parsers.enumParser(ContainerAction.class);

    @Getter
    private final String name;

    public abstract <V extends Nameable> void apply(Container<V> container);
}

