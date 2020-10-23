package me.earth.pingbypass.mixin.mixins.minecraft.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.impl.event.events.DisconnectEvent;
import me.earth.earthhack.impl.event.events.PacketEvent;
import me.earth.pingbypass.event.events.CPacketEvent;
import me.earth.pingbypass.util.wrappers.CPacketWrapper;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager
{
    @Shadow
    private Channel channel;

    @Shadow
    public abstract boolean isChannelOpen();

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacketPre(Packet<?> packet, CallbackInfo info)
    {
        PacketEvent<?> event = new PacketEvent.Send<>(packet);
        Bus.EVENT_BUS.post(event, packet.getClass());

        if (event.isCancelled())
        {
            info.cancel();
        }
    }

    @Inject(method = "dispatchPacket", at = @At("RETURN"))
    private void onSendPacketPre(final Packet<?> inPacket, @Nullable final GenericFutureListener<? extends Future<? super Void >>[] futureListeners, CallbackInfo info)
    {
        PacketEvent<?> event = new PacketEvent.Post<>(inPacket);
        Bus.EVENT_BUS.post(event, inPacket.getClass());
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void onChannelReadPre(ChannelHandlerContext context, Packet<?> packet, CallbackInfo info)
    {
        PacketEvent.Receive<?> event = new PacketEvent.Receive<>(packet);
        Bus.EVENT_BUS.post(event, packet.getClass());

        if (event.isCancelled())
        {
            info.cancel();
        }
    }

    @Inject(method = "dispatchPacket", at = @At("HEAD"), cancellable = true)
    private void dispatchPacket(final Packet<?> inPacket, @Nullable final GenericFutureListener <? extends Future <? super Void >> [] futureListeners, CallbackInfo info)
    {
        if (inPacket instanceof CPacketWrapper)
        {
            dispatchWrapper(inPacket, futureListeners);

            com.github.steveice10.packetlib.packet.Packet wrapped = ((CPacketWrapper) inPacket).getPacket();
            CPacketEvent<?> event = new CPacketEvent.Post<>(wrapped);
            Bus.EVENT_BUS.post(event, wrapped.getClass());

            info.cancel();
        }
    }

    private void dispatchWrapper(Packet<?> wrapper, @Nullable final GenericFutureListener <? extends Future <? super Void >> [] futureListeners)
    {
        if (this.channel.eventLoop().inEventLoop())
        {
            ChannelFuture channelfuture = this.channel.writeAndFlush(wrapper);

            if (futureListeners != null)
            {
                channelfuture.addListeners(futureListeners);
            }

            channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        }
        else
        {
            Channel channel = this.channel;
            this.channel.eventLoop().execute(() ->
            {
                ChannelFuture channelfuture1 = channel.writeAndFlush(wrapper);

                if (futureListeners != null)
                {
                    channelfuture1.addListeners(futureListeners);
                }

                channelfuture1.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            });
        }
    }

    @Inject(method = "closeChannel", at = @At(value = "INVOKE", target = "Lio/netty/channel/Channel;isOpen()Z", remap = false))
    private void onDisconnectHook(ITextComponent component, CallbackInfo callbackInfo)
    {
        if (this.isChannelOpen())
        {
            Bus.EVENT_BUS.post(new DisconnectEvent(component));
        }
    }

}
