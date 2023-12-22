package me.earth.pingbypass.api.command.impl.module;

import me.earth.pingbypass.api.command.Command;

/**
 * Modules that implement this interface can override the behaviour of a {@link ModuleCommand} that targets them.
 */
public interface HasCustomModuleCommand extends Command {
    /**
     * @return {@code true} if the ModuleCommand should not add its setting arguments.
     */
    default boolean overrideCompletely() {
        return false;
    }

}
