package me.earth.pingbypass.api.ducks;

import me.earth.pingbypass.api.ducks.network.IConnection;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.jetbrains.annotations.Nullable;

public class DummyConnection extends Connection implements IConnection {
    private @Nullable Packet<?> justSent;

    public DummyConnection(PacketFlow arg) {
        super(arg);
    }

    @Override
    public void pingbypass$send(Packet<?> packet) {
        justSent = packet;
    }

    @Override
    public PacketFlow pingbypass$getReceiving() {
        return getReceiving();
    }

}
