package me.earth.pingbypass.server.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.commands.api.AbstractServerCommand;
import me.earth.pingbypass.server.commands.api.ServerCommandSource;
import me.earth.pingbypass.server.handlers.play.GameProfileTranslation;
import me.earth.pingbypass.server.handlers.play.JoinWorldService;
import me.earth.pingbypass.server.handlers.play.PlayPacketHandler;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;

@Slf4j
public class ConnectCommand extends AbstractServerCommand {
    public ConnectCommand(PingBypassServer server) {
        super(server, "connect", "Connects PingBypass to a server");
    }

    @Override
    public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
        builder.executes(ctx -> {
            Session session = ctx.getSource().getSession();
            // TODO: check if session has permissions!
            LocalPlayer player = server.getMinecraft().player;
            ClientLevel level = server.getMinecraft().level;
            MultiPlayerGameMode gameMode = server.getMinecraft().gameMode;
            if (player != null && level != null && gameMode != null) {
                session.getPipeline().lock();
                // TODO: when is the best time to allow proxying packets from client to server?
                session.whenMadePrimarySession();
                session.setListener(new PlayPacketHandler(server, session, session.getCookie()));
                new JoinWorldService().join(session, player, gameMode, level);
                session.getPipeline().unlockAndFlush();
            } else {
                session.send(new ClientboundSystemChatPacket(Component.literal("Proxy is currently not connected.").withStyle(ChatFormatting.RED), false));
            }

            return Command.SINGLE_SUCCESS;
        }).then(arg("server", StringArgumentType.string()).executes(ctx -> { // TODO: suggestions?


            return Command.SINGLE_SUCCESS;
        }));
    }

}
