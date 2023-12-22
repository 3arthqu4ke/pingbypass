package me.earth.pingbypass.api.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.earth.pingbypass.api.traits.HasDescription;
import me.earth.pingbypass.api.traits.Nameable;

public interface Command extends Nameable, HasDescription {
    void build(LiteralArgumentBuilder<CommandSource> builder);

}
