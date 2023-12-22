package me.earth.pingbypass.api.command.impl.arguments;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.module.Category;
import me.earth.pingbypass.api.traits.Streamable;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class CategoryArgument implements NameableArgumentType<Category> {
    private final String type = "category";
    private final Streamable<Category> nameables;

}
