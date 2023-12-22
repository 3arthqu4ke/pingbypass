package me.earth.pingbypass.commons.protocol.login;

import me.earth.pingbypass.commons.protocol.IPacketListener;
import me.earth.pingbypass.commons.protocol.login.c2s.C2SStartLoginPacket;
import me.earth.pingbypass.commons.protocol.login.c2s.C2SValidateServerPacket;

public interface ServerLoginHandler extends IPacketListener {
    void processValidateServer(C2SValidateServerPacket packet);

    void processStartLogin(C2SStartLoginPacket packet);

}
