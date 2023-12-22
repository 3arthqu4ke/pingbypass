package me.earth.pingbypass.client.network;

import com.google.common.primitives.Ints;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.commons.protocol.login.ClientLoginHandler;
import me.earth.pingbypass.commons.protocol.login.c2s.C2SStartLoginPacket;
import me.earth.pingbypass.commons.protocol.login.c2s.C2SValidateServerPacket;
import me.earth.pingbypass.commons.protocol.login.s2c.S2CStartLogin;
import me.earth.pingbypass.commons.protocol.login.s2c.S2CValidateClientPacket;
import me.earth.pingbypass.security.CipherUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;

import static me.earth.pingbypass.commons.Constants.RANDOM;

@RequiredArgsConstructor
public class ClientLoginHandlerImpl implements ClientLoginHandler {
    private final byte[] challenge = Ints.toByteArray(RANDOM.nextInt());
    @Getter
    private final Connection connection;
    private final PingBypass pingBypass;
    private final Minecraft mc;
    private String serverId;

    @Override
    public void processValidateClient(S2CValidateClientPacket packet) {
        this.serverId = packet.getServerId();
        PrivateKey privateKey = pingBypass.getSecurityManager().getPrivateKey();
        byte[] encrypted = CipherUtil.encryptUsingKey(privateKey, packet.getChallenge());
        connection.send(new C2SValidateServerPacket(packet.getId(), challenge, encrypted, "dummy"));
    }

    @Override
    public void processStartLogin(S2CStartLogin packet) {
        if (this.serverId == null) {
            this.connection.disconnect(Component.literal("Server did not send S2CValdiateClientPacket!"));
            return;
        }

        Optional<PublicKey> publicKey = pingBypass.getSecurityManager().getPublicKey(serverId);
        if (publicKey.isPresent()) {
            if (CipherUtil.isChallengeValid(challenge, packet.getSolvedChallenge(), publicKey.get())) {
                connection.send(new C2SStartLoginPacket(packet.getId()));
            } else {
                connection.disconnect(
                        Component.literal("PingBypass server'%s' failed verification!".formatted(serverId)));
            }
        } else {
            connection.disconnect(
                    Component.literal("Could not find public key for PingBypass server '%s'!".formatted(serverId)));
        }
    }

    @Override
    public void onDisconnect(@NotNull Component reason) {
        mc.setScreen(new DisconnectedScreen(new TitleScreen(), CommonComponents.CONNECT_FAILED, reason));
    }

}
