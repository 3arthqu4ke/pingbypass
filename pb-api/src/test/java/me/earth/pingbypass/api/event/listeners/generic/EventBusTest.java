package me.earth.pingbypass.api.event.listeners.generic;

import lombok.experimental.UtilityClass;
import me.earth.pingbypass.api.event.EventBusImpl;
import me.earth.pingbypass.api.event.api.EventBus;
import me.earth.pingbypass.api.event.api.Subscriber;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EventBusTest {
    @Test
    public void testListeners() {
        assertEquals(Integer.class, Helper.LISTENER_1.getType());
        assertNull(Helper.LISTENER_1.getGenericType());
        assertEquals(0, Helper.LISTENER_1.getPriority());

        assertEquals(Integer.class, Helper.LISTENER_2.getType());
        assertNull(Helper.LISTENER_2.getGenericType());
        assertEquals(1, Helper.LISTENER_2.getPriority());

        assertEquals(Integer.class, Helper.LISTENER_3.getType());
        assertNull(Helper.LISTENER_3.getGenericType());
        assertEquals(2, Helper.LISTENER_3.getPriority());

        assertEquals(Long.class, Helper.LISTENER_4.getType());
        assertNull(Helper.LISTENER_4.getGenericType());
        assertEquals(0, Helper.LISTENER_4.getPriority());

        assertEquals(List.class, Helper.LISTENER_5.getType());
        assertEquals(Integer.class, Helper.LISTENER_5.getGenericType());
        assertEquals(0, Helper.LISTENER_5.getPriority());
    }

    @Test
    public void testPost() {
        Helper.reset();
        EventBus bus = new EventBusImpl();
        bus.post(1);
        assertEquals(0, Helper.CALLED_1.get());
        assertEquals(0, Helper.CALLED_2.get());
        assertEquals(0, Helper.CALLED_3.get());
        assertEquals(0, Helper.CALLED_4.get());
        assertEquals(0, Helper.CALLED_5.get());

        bus.subscribe(Helper.SUBSCRIBER_1);
        bus.post(1);
        assertEquals(1, Helper.CALLED_1.get());
        assertEquals(0, Helper.CALLED_2.get());
        assertEquals(0, Helper.CALLED_3.get());
        assertEquals(0, Helper.CALLED_4.get());
        assertEquals(0, Helper.CALLED_5.get());
    }

    @UtilityClass
    private static final class Helper {
        private final AtomicInteger CALLED_1 = new AtomicInteger();
        private final Listener<Integer> LISTENER_1 = new Listener<>() {
            @Override
            public void onEvent(Integer event) {
                CALLED_1.set(event);
            }
        };

        private final AtomicInteger CALLED_2 = new AtomicInteger();
        private final Listener<Integer> LISTENER_2 = new Listener<>(1) {
            @Override
            public void onEvent(Integer event) {
                CALLED_2.set(event);
            }
        };

        private final AtomicInteger CALLED_3 = new AtomicInteger();
        private final Listener<Integer> LISTENER_3 = new Listener<>(2) {
            @Override
            public void onEvent(Integer event) {
                CALLED_3.set(event);
            }
        };

        private final AtomicLong CALLED_4 = new AtomicLong();
        private final Listener<Long> LISTENER_4 = new Listener<>() {
            @Override
            public void onEvent(Long event) {
                CALLED_4.set(event);
            }
        };

        private final AtomicInteger CALLED_5 = new AtomicInteger();
        private final Listener<List<Integer>> LISTENER_5 = new Listener<>() {
            @Override
            public void onEvent(List<Integer> event) {
                CALLED_5.set(event.get(0));
            }
        };

        private final Subscriber SUBSCRIBER_1 =
            () -> List.of(LISTENER_1);

        private final Subscriber SUBSCRIBER_2 =
            () -> List.of(LISTENER_2, LISTENER_3);

        private final Subscriber SUBSCRIBER_3 =
            () -> List.of(LISTENER_4);

        private final Subscriber SUBSCRIBER_4 =
            () -> List.of(LISTENER_5);

        private void reset() {
            CALLED_1.set(0);
            CALLED_2.set(0);
            CALLED_3.set(0);
            CALLED_4.set(0);
            CALLED_5.set(0);
        }
    }

}
