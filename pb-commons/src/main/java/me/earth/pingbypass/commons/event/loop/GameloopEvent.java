package me.earth.pingbypass.commons.event.loop;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameloopEvent {
    public static final GameloopEvent INSTANCE = new GameloopEvent();

    @Getter
    @Setter
    private int ticks;

}
