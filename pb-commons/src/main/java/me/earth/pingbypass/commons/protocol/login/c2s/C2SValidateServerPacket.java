package me.earth.pingbypass.commons.protocol.login.c2s;

import lombok.Getter;
import me.earth.pingbypass.commons.protocol.login.C2SLoginPacket;
import me.earth.pingbypass.commons.protocol.login.LoginProtocolIds;
import me.earth.pingbypass.commons.protocol.login.ServerLoginHandler;
import net.minecraft.network.FriendlyByteBuf;

@Getter
public class C2SValidateServerPacket extends C2SLoginPacket {
    private final byte[] challenge;
    private final byte[] solvedChallenge;
    private final String clientId;

    public C2SValidateServerPacket(int transactionId, FriendlyByteBuf buf) {
        this(transactionId, buf.readByteArray(), buf.readByteArray(), buf.readUtf());
    }

    public C2SValidateServerPacket(int transactionId, byte[] challenge, byte[] solvedChallenge, String clientId) {
        super(LoginProtocolIds.C2S_VALIDATE_SERVER, transactionId);
        this.solvedChallenge = solvedChallenge;
        this.challenge = challenge;
        this.clientId = clientId;
    }

    @Override
    public void writeInnerBuffer(FriendlyByteBuf buf) {
        buf.writeByteArray(challenge);
        buf.writeByteArray(solvedChallenge);
        buf.writeUtf(clientId);
    }

    @Override
    public void process(ServerLoginHandler listener) {
        listener.processValidateServer(this);
    }

}
