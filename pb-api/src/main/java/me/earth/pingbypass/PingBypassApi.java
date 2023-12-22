package me.earth.pingbypass;

import lombok.experimental.UtilityClass;
import me.earth.pingbypass.api.event.EventBusImpl;
import me.earth.pingbypass.api.event.api.EventBus;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

@UtilityClass
public class PingBypassApi {
    private static final List<PingBypass> INSTANCES = new CopyOnWriteArrayList<>();
    private static final EventBus EVENT_BUS = new EventBusImpl();

    static void addInstance(PingBypass instance) {
        if (!INSTANCES.contains(instance)) {
            INSTANCES.add(instance);
        }
    }

    public static Stream<PingBypass> instances() {
        return INSTANCES.stream();
    }

    public static EventBus getEventBus() {
        return EVENT_BUS;
    }

}
