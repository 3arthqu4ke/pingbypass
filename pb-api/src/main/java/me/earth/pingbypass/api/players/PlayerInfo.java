package me.earth.pingbypass.api.players;

import com.google.gson.annotations.SerializedName;
import me.earth.pingbypass.api.config.GsonSerializable;
import me.earth.pingbypass.api.traits.HasDescription;
import me.earth.pingbypass.api.traits.Nameable;

import java.util.UUID;

public record PlayerInfo(@SerializedName("name") String name,
                         @SerializedName("uuid") UUID uuid) implements Nameable, HasDescription, GsonSerializable {
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return uuid.toString();
    }

}
