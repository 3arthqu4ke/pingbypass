package me.earth.pingbypass.api.protocol;

import io.netty.buffer.Unpooled;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.TestPingBypass;
import me.earth.pingbypass.api.ducks.DummyConnection;
import me.earth.pingbypass.api.event.network.PacketEvent;
import me.earth.pingbypass.api.protocol.event.CustomPayloadInitEvent;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProtocolManagerTest {
    private static final ThreadLocal<Boolean> HANDLED = new ThreadLocal<>();

    @Test
    public void testProtocolManager() {
        HANDLED.set(false);
        PingBypass pingBypass = new TestPingBypass();
        ProtocolManager protocolManager = new ProtocolManager(pingBypass);

        TestPacket packet = new TestPacket(10);
        protocolManager.register(packet.getId(), TestPacket::new);

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(new byte[20]));
        buf.resetWriterIndex();
        packet.write(buf);

        ProtocolListener protocolListener = new ProtocolListener(protocolManager, PacketFlow.SERVERBOUND);
        ResourceLocation resourceLocation = buf.readResourceLocation();
        assertEquals(packet.getId(), resourceLocation);
        CustomPayloadInitEvent payloadInitEvent = new CustomPayloadInitEvent(ServerboundCustomPayloadPacket.class, resourceLocation, buf);
        protocolListener.onCustomPayloadInit(payloadInitEvent);
        assertNotNull(payloadInitEvent.getPayload());

        protocolListener.onPacketReceived(payloadInitEvent.getPayload(), new PacketEvent.Receive<>(null, new DummyConnection(PacketFlow.CLIENTBOUND)));
        assertFalse(HANDLED.get());
        protocolListener.onPacketReceived(payloadInitEvent.getPayload(), new PacketEvent.Receive<>(null, new DummyConnection(PacketFlow.SERVERBOUND)));
        assertTrue(HANDLED.get());
        HANDLED.set(false);
    }

    @RequiredArgsConstructor
    private static final class TestPacket implements C2SPacket, ProtocolHandler.SelfHandling {
        private final int id;

        public TestPacket(FriendlyByteBuf buf) {
            this(buf.readVarInt());
        }

        @Override
        public void handle(PingBypass pingBypass, Connection connection) {
            HANDLED.set(true);
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation("pingbypass", "test");
        }

        @Override
        public void writePacket(FriendlyByteBuf buf) {
            buf.writeInt(id);
        }
    }

}
