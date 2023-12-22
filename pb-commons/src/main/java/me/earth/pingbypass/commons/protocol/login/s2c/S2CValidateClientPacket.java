package me.earth.pingbypass.commons.protocol.login.s2c;

import lombok.Getter;
import me.earth.pingbypass.commons.protocol.login.ClientLoginHandler;
import me.earth.pingbypass.commons.protocol.login.LoginProtocolIds;
import me.earth.pingbypass.commons.protocol.login.S2CLoginPacket;
import net.minecraft.network.FriendlyByteBuf;

@Getter
public class S2CValidateClientPacket extends S2CLoginPacket {
    private final byte[] challenge;
    private final String serverId;

    public S2CValidateClientPacket(int transactionId, FriendlyByteBuf buf) {
        this(transactionId, buf.readByteArray(), buf.readUtf());
    }

    public S2CValidateClientPacket(int transactionId, byte[] challenge, String serverId) {
        super(LoginProtocolIds.S2C_VALIDATE_CLIENT, transactionId);
        this.challenge = challenge;
        this.serverId = serverId;
    }

    @Override
    public void writeInnerBuffer(FriendlyByteBuf buf) {
        buf.writeByteArray(challenge);
        buf.writeUtf(serverId);
    }

    @Override
    public void process(ClientLoginHandler listener) {
        listener.processValidateClient(this);
    }

}
