package me.earth.pingbypass.api.side;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import me.earth.pingbypass.api.traits.Nameable;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class Side implements Nameable {
    public static final Side SERVER = new Side("server");
    public static final Side CLIENT = new Side("client");
    public static final Side ANY = new Side("any");

    @SerializedName(value = "side", alternate = {"name"})
    private String name;

}
