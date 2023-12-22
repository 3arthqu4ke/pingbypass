package me.earth.pingbypass.commons.protocol.login.c2s;

import me.earth.pingbypass.commons.protocol.login.C2SLoginPacket;
import me.earth.pingbypass.commons.protocol.login.LoginProtocolIds;
import me.earth.pingbypass.commons.protocol.login.ServerLoginHandler;
import net.minecraft.network.FriendlyByteBuf;

public class C2SStartLoginPacket extends C2SLoginPacket {
    public C2SStartLoginPacket(int transactionId) {
        super(LoginProtocolIds.C2S_START_LOGIN, transactionId);
    }

    @Override
    public void writeInnerBuffer(FriendlyByteBuf buf) {

    }

    @Override
    public void process(ServerLoginHandler listener) {
        listener.processStartLogin(this);
    }

}
