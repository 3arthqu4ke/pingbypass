package me.earth.pingbypass.api.platform;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import me.earth.pingbypass.api.traits.Nameable;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class Platform implements Nameable {
    // TODO: support the new LaunchWrapper? Quilt? NeoForge?
    public static final Platform FABRIC = new Platform("fabric");
    public static final Platform FORGE = new Platform("forge");

    @SerializedName(value = "platform", alternate = {"name"})
    private String name;

}
