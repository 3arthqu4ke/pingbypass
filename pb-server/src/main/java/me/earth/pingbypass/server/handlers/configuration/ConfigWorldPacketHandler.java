package me.earth.pingbypass.server.handlers.configuration;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.commands.api.ServerCommandSource;
import me.earth.pingbypass.server.handlers.AbstractCommonPacketListener;
import me.earth.pingbypass.server.handlers.IServerGamePacketListener;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import net.minecraft.server.network.CommonListenerCookie;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class ConfigWorldPacketHandler extends AbstractCommonPacketListener implements IServerGamePacketListener {
    public ConfigWorldPacketHandler(PingBypassServer server, Session session, CommonListenerCookie cookie) {
        super(server, session, cookie, server.getMinecraft());
    }

    @Override
    public void handleChatCommand(@NotNull ServerboundChatCommandPacket packet) {
        PacketUtils.ensureRunningOnSameThread(packet, this, mc);
        try {
            log.info("Executing command {}", packet.command());
            server.getServerCommandManager().execute(packet.command(), new ServerCommandSource(server, session));
        } catch (CommandSyntaxException e) {
            session.send(new ClientboundSystemChatPacket(ComponentUtils.fromMessage(e.getRawMessage()), false));
        }
    }

    @Override
    public void handleCustomCommandSuggestions(@NotNull ServerboundCommandSuggestionPacket packet) {
        PacketUtils.ensureRunningOnSameThread(packet, this, mc);
        StringReader stringReader = new StringReader(packet.getCommand());
        if (stringReader.canRead() && stringReader.peek() == '/') {
            stringReader.skip();
        }

        ParseResults<ServerCommandSource> parse = server.getServerCommandManager().parse(stringReader, new ServerCommandSource(server, session));
        this.server.getServerCommandManager()
                .getCompletionSuggestions(parse, parse.getReader().getTotalLength())
                .thenAccept(suggestions -> this.send(new ClientboundCommandSuggestionsPacket(packet.getId(), suggestions)));
    }

    @Override
    public void onPacket(@NotNull Packet<?> packet) {

    }

    @Override
    public @NotNull Connection getConnection() {
        return super.getConnection();
    }

    @Override
    public void onDisconnect(@NotNull Component component) {
        log.info("Disconnected: " + component);
    }

}
