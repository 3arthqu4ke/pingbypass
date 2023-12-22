package me.earth.pingbypass.api.command.impl.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.traits.HasDescription;
import me.earth.pingbypass.api.traits.Nameable;
import me.earth.pingbypass.api.traits.Streamable;

// TODO: this is an awful hack
@Slf4j
@Getter
@ToString
public class DynamicDescriptionArgument<T extends Nameable & HasDescription> implements DescriptionArgumentType<T> {
    private final SavingArgumentType<Streamable<T>> argumentType;
    private final String type;

    @SuppressWarnings("unchecked")
    public DynamicDescriptionArgument(SavingArgumentType<? extends Streamable<T>> argumentType, String type) {
        this.argumentType = (SavingArgumentType<Streamable<T>>) argumentType;
        this.type = type;
    }

    @Override
    public Streamable<T> getNameables() {
        Streamable<T> result = argumentType.getCurrent();
        if (result == null) {
            log.error("Streamable for " + this + " was null!");
            result = Streamable.empty();
        }

        return result;
    }

    @Getter
    public static class SavingArgumentType<T> extends DelegatingArgumentType<T> {
        private T current;

        @SuppressWarnings("unchecked")
        public SavingArgumentType(ArgumentType<? extends T> delegate) {
            super((ArgumentType<T>) delegate);
        }

        @Override
        public T parse(StringReader reader) throws CommandSyntaxException {
            current = super.parse(reader);
            return current;
        }
    }

}
