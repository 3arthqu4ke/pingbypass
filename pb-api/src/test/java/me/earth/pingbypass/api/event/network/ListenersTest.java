package me.earth.pingbypass.api.event.network;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListenersTest {
    @Test
    public void testSafeAsnycReceiveListener() {
        var listener = new AsyncReceiveListener<ClientboundSetEntityMotionPacket>(null) {
            @Override
            public void onEvent(PacketEvent.Receive<ClientboundSetEntityMotionPacket> event,
                                LocalPlayer player, ClientLevel level, MultiPlayerGameMode gameMode) {

            }
        };

        assertEquals(PacketEvent.Receive.class, listener.getType());
        assertEquals(ClientboundSetEntityMotionPacket.class, listener.getGenericType());
    }

    @Test
    public void testReceiveListener() {
        var listener = new ReceiveListener<ClientboundSetEntityMotionPacket>() {
            @Override
            public void onEvent(PacketEvent.Receive<ClientboundSetEntityMotionPacket> event) {

            }
        };

        assertEquals(PacketEvent.Receive.class, listener.getType());
        assertEquals(ClientboundSetEntityMotionPacket.class, listener.getGenericType());
    }

    @Test
    public void testSendListener() {
        var listener = new SendListener<ServerboundAcceptTeleportationPacket>() {
            @Override
            public void onEvent(PacketEvent.Send<ServerboundAcceptTeleportationPacket> event) {

            }
        };

        assertEquals(PacketEvent.Send.class, listener.getType());
        assertEquals(ServerboundAcceptTeleportationPacket.class, listener.getGenericType());
    }

}
