package me.earth.pingbypass.api.event.gui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.event.event.CancellableEvent;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

@Getter
@RequiredArgsConstructor
public class GuiScreenEvent<T extends Screen> extends CancellableEvent {
    private final @Nullable T screen;

    public static final class Post<T extends Screen> extends GuiScreenEvent<T> {
        public Post(T screen) {
            super(screen);
        }
    }

    // why was this a separate event again?
    public static final class TitleScreen<T extends Screen> extends GuiScreenEvent<T> {
        public TitleScreen(T screen) {
            super(screen);
        }
    }

}
