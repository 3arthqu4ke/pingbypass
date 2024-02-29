package me.earth.pingbypass.server.mixins.network;

import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ClientboundPlayerInfoUpdatePacket.class)
public interface IClientboundPlayerInfoUpdatePacket {
    @Mutable
    @Accessor("entries")
    void setEntries(List<ClientboundPlayerInfoUpdatePacket.Entry> entries);

}
