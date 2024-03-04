package me.earth.pingbypass.api.protocol.c2s;

import me.earth.pingbypass.api.protocol.C2SPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Send when instead of a {@link net.minecraft.network.protocol.game.ServerboundMovePlayerPacket}, if the client does not want to.
 */
public class C2SNoMovementUpdate implements C2SPacket {
    public static final ResourceLocation ID = new ResourceLocation("pingbypass", "no-movement-update");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void writePacket(FriendlyByteBuf buf) {
        // nothing to write
    }

}
