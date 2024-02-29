package me.earth.pingbypass.api.event.impl;

import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.NotAvailableAtRuntime;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.TestPingBypass;
import me.earth.pingbypass.api.event.api.Subscriber;
import me.earth.pingbypass.api.side.Side;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class SideSpecificSubscriberTest {
    @Test
    @SuppressWarnings("Convert2MethodRef") // NotAvailableAtRuntime.class::getName will cause the NoClassDefFoundError to happen earlier!
    public void testThatClassIsNotAvailable() {
        assertThrows(NoClassDefFoundError.class, () -> NotAvailableAtRuntime.class.getName());
    }

    @Test
    public void testSideSpecificSubscriber() {
        PingBypass pingBypass = new TestPingBypass();
        List<?> list = SideSpecificSubscriber.create(Stream.of(pingBypass), Side.SERVER, () -> TestPingBypass.class, subscriber -> subscriber.addListener(null));
        assertTrue(list.isEmpty());
        list = SideSpecificSubscriber.create(Stream.of(pingBypass), Side.CLIENT, () -> OtherPingBypassImpl.class, subscriber -> subscriber.addListener(null));
        assertTrue(list.isEmpty());
        list = SideSpecificSubscriber.create(Stream.of(pingBypass), Side.CLIENT, () -> NotAvailableAtRuntime.class, subscriber -> subscriber.addListener(null));
        assertTrue(list.isEmpty());
        list = SideSpecificSubscriber.create(Stream.of(pingBypass), Side.CLIENT, () -> TestPingBypass.class, subscriber -> subscriber.addListener(null));
        assertEquals(1, list.size());
        Subscriber subscriber = (Subscriber) list.get(0);
        assertEquals(1, subscriber.getListeners().size());
    }

    public interface OtherPingBypassImpl extends PingBypass {

    }

}
