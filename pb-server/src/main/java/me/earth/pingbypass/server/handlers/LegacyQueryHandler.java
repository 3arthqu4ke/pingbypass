package me.earth.pingbypass.server.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.server.service.LegacyServerStatus;
import me.earth.pingbypass.server.service.ServerStatusService;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * A custom {@link net.minecraft.server.network.LegacyQueryHandler}.
 */
@Slf4j
@RequiredArgsConstructor
public class LegacyQueryHandler extends ChannelInboundHandlerAdapter {
    private final ServerStatusService service;

    @Override
    public void channelRead(@NonNull ChannelHandlerContext ctx, @NonNull Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        buf.markReaderIndex();
        boolean hasNotBeenReleased = true;

        try {
            if (buf.readUnsignedByte() != 254) {
                return;
            }

            LegacyServerStatus serverInfo = service.getLegacyStatus();
            InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
            int readableBytes = buf.readableBytes();
            switch (readableBytes) {
                case 0 -> {
                    log.debug("Ping: (<1.3.x) from {}:{}", address.getAddress(), address.getPort());
                    String reply = String.format(Locale.ROOT, "%s§%d§%d",
                            serverInfo.motd(),
                            serverInfo.players(),
                            serverInfo.maxPlayers());
                    this.sendFlushAndClose(ctx, this.createReply(reply));
                }
                case 1 -> {
                    if (buf.readUnsignedByte() != 1) {
                        return;
                    }

                    log.debug("Ping: (1.4-1.5.x) from {}:{}", address.getAddress(), address.getPort());
                    String reply = String.format(Locale.ROOT,
                            "§1\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d",
                            127,
                            serverInfo.serverVersion(),
                            serverInfo.motd(),
                            serverInfo.players(),
                            serverInfo.maxPlayers());
                    this.sendFlushAndClose(ctx, this.createReply(reply));
                }
                default -> {
                    boolean mcPingHost = buf.readUnsignedByte() == 1;
                    mcPingHost &= buf.readUnsignedByte() == 250;
                    mcPingHost &= "MC|PingHost".equals(
                            new String(buf.readBytes(buf.readShort() * 2).array(), StandardCharsets.UTF_16BE));

                    int length = buf.readUnsignedShort();
                    mcPingHost &= buf.readUnsignedByte() >= 73;
                    mcPingHost &= 3 + buf.readBytes(buf.readShort() * 2).array().length + 4 == length;
                    mcPingHost &= buf.readInt() <= 65535;
                    if (!(mcPingHost & buf.readableBytes() == 0)) {
                        return;
                    }

                    log.debug("Ping: (1.6) from {}:{}", address.getAddress(), address.getPort());
                    String reply = String.format(
                            Locale.ROOT,
                            "§1\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d",
                            127,
                            serverInfo.serverVersion(),
                            serverInfo.motd(),
                            serverInfo.players(),
                            serverInfo.maxPlayers());
                    this.sendFlushAndClose(ctx, this.createReply(reply));
                }
            }

            buf.release();
            hasNotBeenReleased = false;
        } catch (RuntimeException exception) {
            exception.printStackTrace();
        } finally {
            if (hasNotBeenReleased) {
                buf.resetReaderIndex();
                ctx.channel().pipeline().remove("legacy_query");
                ctx.fireChannelRead(msg);
            }
        }
    }

    private void sendFlushAndClose(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        try {
            ctx.pipeline().firstContext().writeAndFlush(byteBuf).addListener(ChannelFutureListener.CLOSE);
        } finally {
            byteBuf.release();
        }
    }

    private ByteBuf createReply(String msg) {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeByte(0xff);
        char[] chars = msg.toCharArray();
        buffer.writeShort(chars.length);
        for (char character : chars) {
            buffer.writeChar(character);
        }

        return buffer;
    }

}
