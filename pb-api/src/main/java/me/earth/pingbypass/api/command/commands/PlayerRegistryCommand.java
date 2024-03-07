package me.earth.pingbypass.api.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.impl.AbstractCommand;
import me.earth.pingbypass.api.command.impl.UsesExtendedBuilders;
import me.earth.pingbypass.api.command.impl.arguments.NameableArgumentTypeImpl;
import me.earth.pingbypass.api.command.impl.arguments.StringArgument;
import me.earth.pingbypass.api.command.impl.util.ComponentUtil;
import me.earth.pingbypass.api.players.PlayerInfo;
import me.earth.pingbypass.api.players.PlayerRegistry;
import me.earth.pingbypass.api.players.UUIDLookupService;
import me.earth.pingbypass.api.players.impl.MojangApiService;
import me.earth.pingbypass.api.players.impl.UUIDLookupServiceImpl;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static net.minecraft.ChatFormatting.*;

public class PlayerRegistryCommand extends AbstractCommand implements UsesExtendedBuilders {
    private final UUIDLookupService lookupService;
    private final PlayerRegistry registry;
    private final String verb;
    private final String as;
    private final String list;

    public PlayerRegistryCommand(String name, Minecraft mc, PlayerRegistry playerRegistry, String verb, String as,
                                 String list) {
        super(name, "(un)%s players.".formatted(name));
        // TODO: make UUIDLookupService part of the API?
        // TODO: give PingBypass a global ThreadExecutor?
        this.lookupService = new UUIDLookupServiceImpl(new MojangApiService(Executors.newSingleThreadExecutor()), mc);
        this.registry = playerRegistry;
        this.verb = verb;
        this.as = as;
        this.list = list;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add").then(arg("player", StringArgument.word("player"))
            .suggests((ctx, suggestionsBuilder) ->
                    SharedSuggestionProvider.suggest(ctx.getSource().getOnlinePlayerNames(), suggestionsBuilder))
            .executes(ctx -> {
                String name = ctx.getArgument("player", String.class);
                lookupService.getUuid(name).thenAccept(uuid -> ctx.getSource().getMinecraft().submit(() -> {
                    PlayerInfo playerInfo = new PlayerInfo(name, uuid);
                    registry.register(playerInfo);
                    ctx.getSource().getChat().send(
                            Component.literal("Added ").withStyle(ChatFormatting.GREEN)
                                    .append(ComponentUtil.getComponent(playerInfo, ChatFormatting.AQUA))
                                    .append(Component.literal(" as ").withStyle(ChatFormatting.GREEN))
                                    .append(Component.literal(as + ".").withStyle(ChatFormatting.GREEN)));
                })).exceptionally(log(ctx));
        }))).then(literal("del").then(arg("player", new NameableArgumentTypeImpl<>(registry, "player"))
            .executes(ctx -> {
                PlayerInfo playerInfo = ctx.getArgument("player", PlayerInfo.class);
                registry.unregister(playerInfo);
                ctx.getSource().getChat().send(
                        Component.literal("Un").withStyle(ChatFormatting.RED)
                                .append(Component.literal(verb + " ").withStyle(ChatFormatting.RED))
                                .append(ComponentUtil.getComponent(playerInfo, ChatFormatting.AQUA))
                                .append(Component.literal(".").withStyle(ChatFormatting.RED)));
        }))).then(literal("refresh").executes(ctx -> {
            ctx.getSource().getChat().send(
                    Component.literal("Checking the server for players that have changed their name recently."));
            ClientPacketListener connection = ctx.getSource().getMinecraft().getConnection();
            if (connection != null) {
                for (net.minecraft.client.multiplayer.PlayerInfo info : connection.getOnlinePlayers()) {
                    Optional<PlayerInfo> playerInfo = registry.getByUUID(info.getProfile().getId());
                    if (playerInfo.isPresent() && !playerInfo.get().name().equals(info.getProfile().getName())) {
                        PlayerInfo newInfo = new PlayerInfo(info.getProfile().getName(), info.getProfile().getId());
                        registry.register(newInfo);
                        ctx.getSource().getChat().send(Component.literal("Player ")
                                .append(ComponentUtil.getComponent(newInfo, AQUA))
                                .append(Component.literal(" used to be named ").withStyle(WHITE))
                                .append(ComponentUtil.getComponent(playerInfo.get(), AQUA))
                                .append(Component.literal(".").withStyle(WHITE)));
                    }
                }
            }
        })).then(literal("list").executes(ctx -> {
            var component = Component.literal(list).append(Component.literal(": ").withStyle(GRAY));
            Iterator<PlayerInfo> itr = registry.iterator();
            while (itr.hasNext()) {
                PlayerInfo playerInfo = itr.next();
                component.append(ComponentUtil.getComponent(playerInfo, AQUA));
                if (itr.hasNext()) {
                    component.append(Component.literal(", ").withStyle(GRAY));
                }
            }

            ctx.getSource().getChat().send(component);
        }));
    }

    private Function<Throwable, Void> log(CommandContext<CommandSource> ctx) {
        return t -> {
            ctx.getSource().getMinecraft().submit(() ->
                ctx.getSource().getChat().send(Component.literal(t.getMessage()).withStyle(ChatFormatting.RED)));
            return null;
        };
    }

}
