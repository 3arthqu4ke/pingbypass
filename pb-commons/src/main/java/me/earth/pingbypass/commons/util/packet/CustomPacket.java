package me.earth.pingbypass.commons.util.packet;

import net.minecraft.network.ConnectionProtocol;

public interface CustomPacket {
    Integer getId();

    ConnectionProtocol getProtocol();

}
