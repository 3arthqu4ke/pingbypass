package me.earth.pingbypass.server;

import me.earth.pingbypass.api.config.properties.Property;
import me.earth.pingbypass.commons.Constants;
import net.minecraft.util.Crypt;

import java.security.KeyPair;

import static me.earth.pingbypass.api.config.properties.PropertyHelper.*;
import static me.earth.pingbypass.api.util.exceptions.ExceptionUtil.sneaky;

public interface ServerConstants {
    KeyPair KEY_PAIR = sneaky(Crypt::generateKeyPair);
    int MAX_PLAYERS = 1;

    Property<String> ADDRESS = string("pb.address", "localhost");
    Property<Integer> PORT = number("pb.port", Constants.DEFAULT_PORT);
    Property<Boolean> EPOLL = bool("pb.epoll", true);
    Property<Boolean> REPLY_TO_STATUS = bool("pb.replyToStatus", true);
    Property<Integer> COMPRESSION = number("pb.compression", 256);
    Property<String> PASSWORD = string("pb.password", "");
    Property<Boolean> ENCRYPT = bool("pb.encrypt", true);

}
