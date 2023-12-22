package me.earth.pingbypass.commons.module.movement;

import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.event.event.CancellableEvent;
import me.earth.pingbypass.api.event.listeners.generic.Listener;
import me.earth.pingbypass.api.module.impl.Categories;
import me.earth.pingbypass.api.module.impl.ModuleImpl;
import me.earth.pingbypass.commons.event.CancellingListener;
import me.earth.pingbypass.commons.event.network.AsyncReceiveListener;
import me.earth.pingbypass.commons.event.network.PacketEvent;
import me.earth.pingbypass.commons.mixins.network.s2c.IClientBoundExplodePacket;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;

public class Velocity extends ModuleImpl {
    public Velocity(PingBypass pingBypass) {
        super(pingBypass, "Velocity", Categories.MOVEMENT, "Prevents knockback.");
        listen(new AsyncReceiveListener<ClientboundSetEntityMotionPacket>(mc) {
            @Override
            public void onEvent(PacketEvent.Receive<ClientboundSetEntityMotionPacket> e, LocalPlayer p, ClientLevel ignore, MultiPlayerGameMode ignored) {
                if (e.getPacket().getId() == p.getId()) {
                    e.setCancelled(true);
                }
            }
        });

        listen(new Listener<PacketEvent.Receive<ClientboundExplodePacket>>() {
            @Override
            public void onEvent(PacketEvent.Receive<ClientboundExplodePacket> event) {
                IClientBoundExplodePacket explodePacket = ((IClientBoundExplodePacket) event.getPacket());
                explodePacket.setKnockbackX(0.0f);
                explodePacket.setKnockbackY(0.0f);
                explodePacket.setKnockbackZ(0.0f);
            }
        });

        listen(new CancellingListener.WithSetting<>(PushOutOfBlocks.class, bool("Blocks", true, "Prevents you from getting pushed out of blocks.")));
        listen(new CancellingListener.WithSetting<>(EntityPush.class, bool("Entities", true, "Prevents you from getting pushed by entities.")));
    }

    public static final class PushOutOfBlocks extends CancellableEvent {}
    public static final class EntityPush extends CancellableEvent {}

}
