package me.earth.pingbypass.api.protocol.c2s;

import me.earth.pingbypass.api.Constants;
import me.earth.pingbypass.api.protocol.C2SPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Send instead of a {@link net.minecraft.network.protocol.game.ServerboundMovePlayerPacket}, if the client has ran a tick but not changed its position.
 */
public class C2SNoMovementUpdate implements C2SPacket {
    public static final ResourceLocation ID = new ResourceLocation(Constants.NAME_LOW, "no-movement-update");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void writePacket(FriendlyByteBuf buf) {
        // nothing to write
    }

}
