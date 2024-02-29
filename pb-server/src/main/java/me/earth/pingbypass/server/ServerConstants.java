package me.earth.pingbypass.server;

import me.earth.pingbypass.api.config.properties.Property;
import me.earth.pingbypass.api.Constants;
import net.minecraft.util.Crypt;

import java.security.KeyPair;

import static me.earth.pingbypass.api.config.properties.PropertyHelper.*;
import static me.earth.pingbypass.api.util.exceptions.ExceptionUtil.sneaky;

public interface ServerConstants {
    KeyPair KEY_PAIR = sneaky(Crypt::generateKeyPair);
    int MAX_PLAYERS = 1;

    Property<String> ADDRESS = string("pb.address", "0.0.0.0");
    Property<Integer> PORT = number("pb.port", Constants.DEFAULT_PORT);
    Property<Boolean> EPOLL = bool("pb.epoll", true);
    Property<Boolean> REPLY_TO_STATUS = bool("pb.replyToStatus", true);
    Property<Integer> COMPRESSION = number("pb.compression", 256);
    Property<String> PASSWORD = string("pb.password", "");
    Property<Boolean> ENCRYPT = bool("pb.encrypt", true);
    Property<Boolean> AUTH = bool("pb.auth", false);
    Property<Boolean> LOG_IPS = bool("pb.logips", true);

}
