package me.earth.pingbypass.api.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.command.impl.CommandManagerImpl;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class CommandManagerTest {
    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void testCommandManager() throws CommandSyntaxException {
        var manager = new CommandManagerImpl();
        var value1 = new AtomicBoolean();
        var value1Command = new TestCommand(value1, "value1");
        manager.register(value1Command);
        var value2 = new AtomicBoolean();
        var value2Command = new TestCommand(value2, "value2");
        manager.register(value2Command);
        assertFalse(value1.get());
        assertFalse(value2.get());
        manager.execute("value1", DummyCommandSource.INSTANCE);
        assertTrue(value1.get());
        assertFalse(value2.get());
        value1.set(false);
        manager.execute("value2", DummyCommandSource.INSTANCE);
        assertFalse(value1.get());
        assertTrue(value2.get());

        assertEquals(manager.getByName("value1").get(), value1Command);
        assertEquals(manager.getByName("value2").get(), value2Command);

        assertTrue(manager.unregister(value1Command));
        assertTrue(manager.stream().noneMatch(value1Command::equals));
        assertThrows(CommandSyntaxException.class, () -> manager.execute("value1", DummyCommandSource.INSTANCE));

        // TODO: test command exceptions etc.
    }

    @Getter
    @RequiredArgsConstructor
    private static final class TestCommand implements Command {
        private final String description = "";
        private final AtomicBoolean value;
        private final String name;

        @Override
        public void build(LiteralArgumentBuilder<CommandSource> builder) {
            builder.executes(ctx -> {
                value.set(true);
                return com.mojang.brigadier.Command.SINGLE_SUCCESS;
            });
        }
    }

}
