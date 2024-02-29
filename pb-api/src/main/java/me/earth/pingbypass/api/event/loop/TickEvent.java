package me.earth.pingbypass.api.event.loop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TickEvent {
    @SuppressWarnings("InstantiationOfUtilityClass")
    public static final TickEvent INSTANCE = new TickEvent();

}
