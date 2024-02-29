package me.earth.pingbypass.api.protocol;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;

public interface IPacketListener extends PacketListener {
    Connection getConnection();

    @Override
    default boolean isAcceptingMessages() {
        return getConnection().isConnected();
    }

}
