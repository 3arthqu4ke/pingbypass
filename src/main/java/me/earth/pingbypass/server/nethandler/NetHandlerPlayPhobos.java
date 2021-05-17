package me.earth.pingbypass.server.nethandler;

import com.github.steveice10.mc.protocol.packet.handshake.client.HandshakePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientResourcePackStatusPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import com.github.steveice10.mc.protocol.packet.login.client.EncryptionResponsePacket;
import com.github.steveice10.mc.protocol.packet.login.client.LoginStartPacket;
import com.github.steveice10.packetlib.packet.Packet;
import me.earth.pingbypass.server.nethandler.handlers.ChatHandler;
import me.earth.pingbypass.server.nethandler.handlers.CloseWindowHandler;
import me.earth.pingbypass.server.nethandler.handlers.EncryptionHandler;
import me.earth.pingbypass.server.nethandler.handlers.HandShakeHandler;
import me.earth.pingbypass.server.nethandler.handlers.HeldItemHandler;
import me.earth.pingbypass.server.nethandler.handlers.KeepAliveHandler;
import me.earth.pingbypass.server.nethandler.handlers.LoginHandler;
import me.earth.pingbypass.server.nethandler.handlers.PosRotationHandler;
import me.earth.pingbypass.server.nethandler.handlers.PositionHandler;
import me.earth.pingbypass.server.nethandler.handlers.RotationHandler;
import me.earth.pingbypass.server.nethandler.handlers.WindowClickHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles Packets coming in from out Client.
 * Supplied by {@link me.earth.pingbypass.server.managers.CPacketManager}.
 */
public class NetHandlerPlayPhobos
{
    private final Map<Class<? extends Packet>, IHandler<?>> handlers = new HashMap<>();

    public NetHandlerPlayPhobos()
    {
        handlers.put(HandshakePacket.class, new HandShakeHandler());
        handlers.put(LoginStartPacket.class, new LoginHandler());

        handlers.put(EncryptionResponsePacket.class, new EncryptionHandler());
        handlers.put(ClientKeepAlivePacket.class, new KeepAliveHandler());

        handlers.put(ClientPlayerPositionPacket.class, new PositionHandler());
        handlers.put(ClientPlayerRotationPacket.class, new RotationHandler());
        handlers.put(ClientPlayerPositionRotationPacket.class, new PosRotationHandler());

        handlers.put(ClientWindowActionPacket.class, new WindowClickHandler());
        handlers.put(ClientPlayerChangeHeldItemPacket.class, new HeldItemHandler());
        handlers.put(ClientCloseWindowPacket.class, new CloseWindowHandler());
        handlers.put(ClientChatPacket.class, new ChatHandler());
        handlers.put(ClientResourcePackStatusPacket.class, new SimpleHandler());
    }

    @SuppressWarnings("unchecked")
    public <T extends Packet> boolean handlePacket(T packet)
    {
        IHandler<T> handler = (IHandler<T>) handlers.get(packet.getClass());
        return handler == null || handler.handle(packet);
    }

}
