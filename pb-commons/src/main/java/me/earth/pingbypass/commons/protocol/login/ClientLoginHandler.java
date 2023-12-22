package me.earth.pingbypass.commons.protocol.login;

import me.earth.pingbypass.commons.protocol.IPacketListener;
import me.earth.pingbypass.commons.protocol.login.s2c.S2CStartLogin;
import me.earth.pingbypass.commons.protocol.login.s2c.S2CValidateClientPacket;

public interface ClientLoginHandler extends IPacketListener {
    void processValidateClient(S2CValidateClientPacket packet);

    void processStartLogin(S2CStartLogin packet);

}
