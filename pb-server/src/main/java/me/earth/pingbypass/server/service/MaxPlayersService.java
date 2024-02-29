package me.earth.pingbypass.server.service;

import lombok.Getter;
import me.earth.pingbypass.api.event.SubscriberImpl;
import me.earth.pingbypass.api.event.network.PacketEvent;
import me.earth.pingbypass.api.event.network.ReceiveListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;

@Getter
public class MaxPlayersService extends SubscriberImpl {
    private int maxPlayers = 1;

    public MaxPlayersService() {
        this.listen(new ReceiveListener<ClientboundLoginPacket>() {
            @Override
            public void onEvent(PacketEvent.Receive<ClientboundLoginPacket> event) {
                maxPlayers = event.getPacket().maxPlayers();
            }
        });
    }

}
