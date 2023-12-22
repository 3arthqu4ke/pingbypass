package me.earth.pingbypass.api.plugin;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.earth.pingbypass.api.platform.Platform;
import me.earth.pingbypass.api.side.Side;
import me.earth.pingbypass.api.traits.HasDescription;
import me.earth.pingbypass.api.traits.Nameable;

@Data
@AllArgsConstructor
public final class PluginConfig implements Nameable, HasDescription {
    public static final String MANIFEST_ENTRY = "PingBypassPluginConfig";

    @SerializedName("name")
    private String name;

    @SerializedName("mainClass")
    private String mainClass = null;

    @SerializedName("mixinConfig")
    private String mixinConfig = null;

    @SerializedName("mixinConnector")
    private String mixinConnector = null;

    @SerializedName("description")
    private String description = null;

    @SerializedName("url")
    private String url = null;

    @SerializedName("supports")
    private Side[] supports;

    @SerializedName("platforms")
    private Platform[] platforms;

    @SerializedName("authors")
    private String[] authors;

    /**
     * Initializes default values.
     */
    public PluginConfig() {
        this.supports = new Side[]{Side.CLIENT, Side.SERVER};
        this.platforms = new Platform[]{Platform.FABRIC, Platform.FORGE};
        this.authors = new String[]{"3arthqu4ke"};
    }

}
