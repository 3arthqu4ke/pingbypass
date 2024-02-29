package me.earth.pingbypass.server.service;

import lombok.Getter;
import me.earth.pingbypass.api.event.SubscriberImpl;
import me.earth.pingbypass.api.event.network.ReceiveListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class QueueService extends SubscriberImpl {
    private static final Pattern REGEX = compile("Position in queue: ([0-9]+)");
    private final Minecraft mc;
    @Getter
    private int position = -1;

    public QueueService(Minecraft mc) {
        this.mc = mc;
        listen(new ReceiveListener.Direct<ClientboundTabListPacket>() {
            @Override
            public void onPacket(ClientboundTabListPacket packet) {
                if (isOn2b2t()) {
                    Matcher matcher = REGEX.matcher(packet.getHeader().getString());
                    if (matcher.find()) {
                        String group = matcher.group(1);
                        position = Integer.parseInt(group);
                        return;
                    }
                }

                position = -1;
            }
        });
    }

    public boolean isOn2b2t() {
        ServerData data = mc.getCurrentServer();
        return data != null && ("2b2t.org".equalsIgnoreCase(data.ip) || "connect.2b2t.org.".equalsIgnoreCase(data.ip));
    }

}
