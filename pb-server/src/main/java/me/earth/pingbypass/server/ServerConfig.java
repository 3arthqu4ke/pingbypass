package me.earth.pingbypass.server;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.config.properties.PropertyConfigImpl;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;

@Slf4j
public class ServerConfig extends PropertyConfigImpl {
    public ServerConfig(Path path) throws IOException {
        super(path);
    }

    @SneakyThrows
    public InetAddress getInetAddress() {
        return InetAddress.getByName(get(ServerConstants.ADDRESS));
    }

}
