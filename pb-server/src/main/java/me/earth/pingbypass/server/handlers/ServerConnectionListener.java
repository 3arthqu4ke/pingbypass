package me.earth.pingbypass.server.handlers;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.event.SubscriberImpl;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.ServerConstants;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.network.PacketDecoder;
import net.minecraft.network.PacketEncoder;
import net.minecraft.network.Varint21FrameDecoder;
import net.minecraft.network.Varint21LengthFieldPrepender;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.util.LazyLoadedValue;

import java.net.InetAddress;
import java.util.Collections;
import java.util.List;

/**
 * A custom {@link net.minecraft.server.network.ServerConnectionListener}.
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("deprecation")
public class ServerConnectionListener extends SubscriberImpl {
    private static final LazyLoadedValue<NioEventLoopGroup> SERVER_EVENT_GROUP = new LazyLoadedValue<>(
        () -> new NioEventLoopGroup(0,
                new ThreadFactoryBuilder().setNameFormat("Netty Server IO #%d").setDaemon(true).build()));
    private static final LazyLoadedValue<EpollEventLoopGroup> SERVER_EPOLL_EVENT_GROUP = new LazyLoadedValue<>(
        () -> new EpollEventLoopGroup(0,
                new ThreadFactoryBuilder().setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build()));

    private final List<ChannelFuture> channels = Collections.synchronizedList(Lists.newArrayList());
    private final PingBypassServer server;

    public void addChannel(InetAddress address, int port) {
        synchronized (this.channels) {
            Class<? extends ServerChannel> channelType;
            LazyLoadedValue<? extends EventLoopGroup> lazyLoadedValue;
            if (Epoll.isAvailable() && server.getServerConfig().get(ServerConstants.EPOLL)) {
                channelType = EpollServerSocketChannel.class;
                lazyLoadedValue = SERVER_EPOLL_EVENT_GROUP;
                log.info("Using epoll channel type");
            } else {
                channelType = NioServerSocketChannel.class;
                lazyLoadedValue = SERVER_EVENT_GROUP;
                log.info("Using default channel type");
            }

            /* TODO: allow binding to port 0 and log resulting port?
            ChannelFuture channelFuture = serverBootstrap.bind().sync()
            InetSocketAddress localAddress = (InetSocketAddress) channelFuture.channel().localAddress();
            int port = localAddress.getPort(); */
            this.channels.add(new ServerBootstrap()
                .channel(channelType)
                .childHandler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(@NonNull Channel channel) {
                        try {
                            channel.config().setOption(ChannelOption.TCP_NODELAY, true);
                        } catch (ChannelException channelException) {
                            log.error(channelException.getMessage());
                        }

                        channel
                            .pipeline()
                            .addLast("timeout", new ReadTimeoutHandler(30))
                            .addLast("legacy_query", new LegacyQueryHandler(server.getServerStatusService()))
                            /*.addLast("splitter", new Varint21FrameDecoder()) TODO
                            .addLast("decoder", new PacketDecoder(PacketFlow.SERVERBOUND))
                            .addLast("prepender", new Varint21LengthFieldPrepender())
                            .addLast("encoder", new PacketEncoder(PacketFlow.CLIENTBOUND))*/;

                        Session session = new Session();
                        server.getSessionManager().addSession(session);
                        channel.pipeline().addLast("packet_handler", session);
                        session.setListener(new HandshakeHandler(server, session));
                    }
                }).group(lazyLoadedValue.get()).localAddress(address, port).bind().syncUninterruptibly());
        }
    }

    public void stop() {
        for (ChannelFuture channelFuture : this.channels) {
            try {
                channelFuture.channel().close().sync();
            } catch (InterruptedException interruptedException) {
                log.error("Interrupted whilst closing channel");
            }
        }
    }

}
