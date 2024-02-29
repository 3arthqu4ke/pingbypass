package me.earth.pingbypass.api.util.packet;

import net.minecraft.network.ConnectionProtocol;

public interface CustomPacket {
    Integer getId();

    ConnectionProtocol getProtocol();

}
