package me.earth.pingbypass.api.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.impl.arguments.CategoryArgument;
import me.earth.pingbypass.api.command.impl.util.ComponentUtil;
import me.earth.pingbypass.api.module.Category;
import me.earth.pingbypass.api.module.ModuleManager;
import me.earth.pingbypass.api.command.AbstractPbCommand;
import me.earth.pingbypass.api.command.components.NameableComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;

public class ModulesCommand extends AbstractPbCommand {
    public ModulesCommand(PingBypass pingBypass, Minecraft mc) {
        super("modules", "Lists all modules currently registered in the client.", pingBypass, mc);
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        ModuleManager modules = pingBypass.getModuleManager();
        builder.executes(ctx -> {
            print(NameableComponent
                    .builder(modules)
                    .withName("Modules")
                    .withFormatting(module -> module.isEnabled() ? ChatFormatting.GREEN : ChatFormatting.RED)
                    .withClickEvent(module -> new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                            pingBypass.getCommandManager().getPrefix() + "toggle " + module.getName()))
                    .build());

            return Command.SINGLE_SUCCESS;
        }).then(arg("category", CategoryArgument.of(modules.getCategoryManager())).executes(ctx -> {
            Category category = ctx.getArgument("category", Category.class);
            print(Component.literal("Category ")
                .append(Component.literal(category.getName()).withStyle(style ->
                        style.withColor(category.getColor()).applyFormats(ChatFormatting.BOLD)))
                .append(Component.literal(":").withStyle(ChatFormatting.WHITE)));

            pingBypass.getModuleManager().getModulesByCategory(category).forEach(module ->
                    print(ComponentUtil.getComponent(module, ChatFormatting.AQUA)));
        }));
    }

}
