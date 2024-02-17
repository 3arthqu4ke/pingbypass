package me.earth.pingbypass.server.handlers;

import me.earth.pingbypass.commons.protocol.IPacketListener;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface IHandler extends IPacketListener {
    Session getSession();

    @Override
    default Connection getConnection() {
        return getSession();
    }

    @Override
    default void onDisconnect(Component component) {

    }

}
