package me.earth.pingbypass.api.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import lombok.experimental.UtilityClass;
import me.earth.pingbypass.api.command.EmptySuggestionProvider;
import net.minecraft.commands.SharedSuggestionProvider;

@UtilityClass
public class ParseResultUtil {
    @SuppressWarnings("rawtypes")
    private static final CommandDispatcher DUMMY = new CommandDispatcher();

    @SuppressWarnings("unchecked")
    public static <S> ParseResults<S> dummy(S source) {
        return DUMMY.parse("", source);
    }

    public static ParseResults<SharedSuggestionProvider> dummy() {
        return dummy(EmptySuggestionProvider.INSTANCE);
    }

}
