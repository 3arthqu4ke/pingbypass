package me.earth.pingbypass.server.handlers;

import com.google.common.base.Suppliers;
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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.ServerConstants;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * A custom {@link net.minecraft.server.network.ServerConnectionListener}.
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class ServerConnectionListener {
    public static final Supplier<NioEventLoopGroup> SERVER_EVENT_GROUP = Suppliers.memoize(
            () -> new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty PingBypass Server IO #%d").setDaemon(true).build()));
    public static final Supplier<EpollEventLoopGroup> SERVER_EPOLL_EVENT_GROUP = Suppliers.memoize
            (() -> new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty PingBypass Epoll Server IO #%d").setDaemon(true).build()));

    private final List<ChannelFuture> channels = Collections.synchronizedList(Lists.newArrayList());
    private final PingBypassServer server;

    public void startTcpServerListener(@Nullable InetAddress address, int port) {
        synchronized (this.channels) {
            Class<? extends ServerChannel> channelType;
            EventLoopGroup eventLoopGroup;
            if (Epoll.isAvailable() && server.getServerConfig().get(ServerConstants.EPOLL)) {
                channelType = EpollServerSocketChannel.class;
                eventLoopGroup = SERVER_EPOLL_EVENT_GROUP.get();
                log.info("Using epoll channel type");
            } else {
                channelType = NioServerSocketChannel.class;
                eventLoopGroup = SERVER_EVENT_GROUP.get();
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
                        protected void initChannel(@NotNull Channel channel) {
                            Connection.setInitialProtocolAttributes(channel);

                            try {
                                channel.config().setOption(ChannelOption.TCP_NODELAY, true);
                            } catch (ChannelException channelException) {
                                log.error(channelException.getMessage());
                            }

                            ChannelPipeline channelPipeline = channel.pipeline()
                                    .addLast("timeout", new ReadTimeoutHandler(30))
                                    .addLast("legacy_query", new LegacyQueryHandler(server.getServerStatusService()));

                            Connection.configureSerialization(channelPipeline, PacketFlow.SERVERBOUND, null);
                            Session session = new Session(server);
                            session.configurePacketHandler(channelPipeline);
                            server.getSessionManager().addSession(session);
                            session.setListenerForServerboundHandshake(new HandshakeHandler(server, session));
                        }
                    }).group(eventLoopGroup)
                    .localAddress(address, port)
                    .bind()
                    .syncUninterruptibly());
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

    // TODO: for testing purposes or other?
    // public SocketAddress startMemoryChannel() {

}
