package me.earth.pingbypass.commons.protocol.login.s2c;

import lombok.Getter;
import me.earth.pingbypass.commons.protocol.login.ClientLoginHandler;
import me.earth.pingbypass.commons.protocol.login.LoginProtocolIds;
import me.earth.pingbypass.commons.protocol.login.S2CLoginPacket;
import net.minecraft.network.FriendlyByteBuf;

@Getter
public class S2CStartLogin extends S2CLoginPacket {
    private final byte[] solvedChallenge;

    public S2CStartLogin(int transactionId, FriendlyByteBuf buf) {
        this(transactionId, buf.readByteArray());
    }

    public S2CStartLogin(int transactionId, byte[] solvedChallenge) {
        super(LoginProtocolIds.S2C_START_LOGIN, transactionId);
        this.solvedChallenge = solvedChallenge;
    }

    @Override
    public void writeInnerBuffer(FriendlyByteBuf buf) {
        buf.writeByteArray(solvedChallenge);
    }

    @Override
    public void process(ClientLoginHandler listener) {
        listener.processStartLogin(this);
    }

}
