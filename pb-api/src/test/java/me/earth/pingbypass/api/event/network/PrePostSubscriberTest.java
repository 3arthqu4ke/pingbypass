package me.earth.pingbypass.api.event.network;

import lombok.Cleanup;
import lombok.Getter;
import me.earth.pingbypass.api.event.EventBusImpl;
import net.minecraft.network.protocol.common.ClientboundPingPacket;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class PrePostSubscriberTest {
    @Test
    public void testPrePostSubscriber() {
        var preEventCalled = new AtomicBoolean();
        var postEvenCalled = new AtomicInteger();
        var scheduledEventCalled = new AtomicBoolean();
        @Cleanup
        var eventLoop = new ReentrantBlockableEventLoop<>("t") {
            @Getter
            private final Thread runningThread = new Thread(() -> {
                throw new AssertionError();
            });

            @Override
            protected Runnable wrapRunnable(Runnable runnable) {
                return runnable;
            }

            @Override
            protected boolean shouldRun(Runnable runnable) {
                return true;
            }
        };

        var subscriber = new PrePostSubscriber<ClientboundPingPacket>() {
            @Override
            public void onPreEvent(PacketEvent.Receive<ClientboundPingPacket> event) {
                preEventCalled.set(true);
                this.addPostEventThreadLocal(() -> postEvenCalled.set(event.getPacket().getId()));
                this.addScheduledPostEvent(eventLoop, () -> scheduledEventCalled.set(true));
            }
        };

        assertEquals(ClientboundPingPacket.class, subscriber.getListeners().get(0).getGenericType());
        assertEquals(ClientboundPingPacket.class, subscriber.getListeners().get(1).getGenericType());

        var bus = new EventBusImpl();
        bus.subscribe(subscriber);

        assertFalse(preEventCalled.get());
        assertFalse(scheduledEventCalled.get());
        assertEquals(0, postEvenCalled.get());
        var packet = new ClientboundPingPacket(5);
        bus.post(new PacketEvent.Receive<>(packet, null), ClientboundPingPacket.class);
        assertTrue(preEventCalled.get());
        assertFalse(scheduledEventCalled.get());
        assertEquals(0, postEvenCalled.get());
        bus.post(new PacketEvent.PostReceive<>(packet, null), ClientboundPingPacket.class);
        assertTrue(preEventCalled.get());
        assertFalse(scheduledEventCalled.get());
        assertEquals(5, postEvenCalled.get());

        eventLoop.pollTask();
        assertTrue(scheduledEventCalled.get());

        // just to use that constructor
        var subscriber2 = new PrePostSubscriber<>(ClientboundPingPacket.class) {
            @Override
            public void onPreEvent(PacketEvent.Receive<ClientboundPingPacket> event) {

            }
        };

        assertEquals(ClientboundPingPacket.class, subscriber2.getListeners().get(0).getGenericType());
        assertEquals(ClientboundPingPacket.class, subscriber2.getListeners().get(1).getGenericType());
    }

}
