package me.earth.pingbypass.api.config.impl.configs;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import me.earth.pingbypass.api.config.ConfigType;
import me.earth.pingbypass.api.config.JsonSerializable;
import me.earth.pingbypass.api.config.impl.AbstractRegistryConfig;
import me.earth.pingbypass.api.files.FileManager;
import me.earth.pingbypass.api.players.PlayerInfo;
import me.earth.pingbypass.api.players.PlayerRegistry;

public class PlayerRegistryConfig extends AbstractRegistryConfig<PlayerInfo, PlayerRegistry> {
    public PlayerRegistryConfig(FileManager fileManager, ConfigType configType, PlayerRegistry registry) {
        super(fileManager, configType, registry);
    }

    @Override
    protected JsonElement serialize(PlayerInfo element) {
        return element.toJson();
    }

    @Override
    protected PlayerInfo deserialize(JsonElement element) throws JsonParseException {
        return JsonSerializable.GSON.fromJson(element, PlayerInfo.class);
    }

}
