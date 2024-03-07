package me.earth.pingbypass.api.mixins.network;

import io.netty.channel.ChannelHandlerContext;
import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.api.event.event.CancellableEvent;
import me.earth.pingbypass.api.ducks.network.IConnection;
import me.earth.pingbypass.api.util.mixin.MixinHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.BundlePacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.function.Function;

@Mixin(Connection.class)
public abstract class MixinConnection implements IConnection {
    @Shadow @Final private static Logger LOGGER;

    @Shadow public abstract void send(Packet<?> packet);

    @Shadow public abstract PacketFlow getReceiving();

    @Override
    public void pingbypass$send(Packet<?> packet) {
        this.send(packet);
    }

    @Override
    public PacketFlow pingbypass$getReceiving() {
        return getReceiving();
    }

    // is this the best injection point?
    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    private void sendPacketHook(Packet<?> packet, PacketSendListener sendListener, boolean flush, CallbackInfo ci) {
        MixinHelper.hook(getSendEvent(packet), packet.getClass(), ci);
    }

    @Inject(method = "sendPacket", at = @At("RETURN"))
    private void postSendHook(Packet<?> packet, PacketSendListener sendListener, boolean flush, CallbackInfo ci) {
        PingBypassApi.getEventBus().post(getPostSendEvent(packet), packet.getClass());
    }

    @Inject(
        method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V",
        at = @At("HEAD"),
        cancellable = true)
    private void channelRead0Hook(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        try {
            MixinHelper.hook(getReceiveEvent(packet), packet.getClass(), ci);
            handleBundle(packet, this::getReceiveEvent, this::getPostReceiveEvent);
        } catch (Exception e) {
            LOGGER.error("PingBypass channelRead0", e);
        }
    }

    @Inject(
        method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V",
        at = @At("RETURN"))
    private void postChannelRead0Hook(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        try {
            PingBypassApi.getEventBus().post(getPostReceiveEvent(packet), packet.getClass());
            handleBundle(packet, this::getPostReceiveEvent, null);
        } catch (Exception e) {
            LOGGER.error("PingBypass channelRead0", e);
        }
    }

    @Inject(method = "exceptionCaught", at = @At("HEAD"))
    private void exceptionCaughtHook(ChannelHandlerContext context, Throwable throwable, CallbackInfo ci) {
        LOGGER.error("exceptionCaught", throwable);
    }

    @Unique
    private void handleBundle(Packet<?> packet, Function<Packet<?>, CancellableEvent> eventFactory,
                              @Nullable Function<Packet<?>, CancellableEvent> postReceiveEventFactory) {
        if (packet instanceof BundlePacket<?> bundlePacket) {
            Iterator<? extends Packet<?>> itr = bundlePacket.subPackets().iterator();
            while (itr.hasNext()) {
                Packet<?> bundled = itr.next();
                CancellableEvent event = eventFactory.apply(bundled);
                PingBypassApi.getEventBus().post(event, bundled.getClass());
                if (event.isCancelled() && postReceiveEventFactory != null) {
                    try {
                        itr.remove();
                        // this is not really correct but whatever
                        PingBypassApi.getEventBus().post(postReceiveEventFactory.apply(bundled), bundled.getClass());
                    } catch (UnsupportedOperationException e) {
                        LOGGER.error("Failed to remove packet from BundledPacket iterator", e);
                    }
                }
            }
        }
    }

}
