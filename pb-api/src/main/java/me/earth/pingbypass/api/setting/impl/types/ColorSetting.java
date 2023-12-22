package me.earth.pingbypass.api.setting.impl.types;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.mojang.brigadier.arguments.ArgumentType;
import me.earth.pingbypass.api.config.ConfigType;
import me.earth.pingbypass.api.config.JsonParser;
import me.earth.pingbypass.api.setting.Complexity;
import me.earth.pingbypass.api.setting.impl.AbstractSettingBuilder;
import me.earth.pingbypass.api.setting.impl.SettingImpl;
import me.earth.pingbypass.api.traits.CanBeVisible;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.function.Function;

public class ColorSetting extends SettingImpl<Color> {
    @SerializedName("red")
    private int red;
    @SerializedName("green")
    private int green;
    @SerializedName("blue")
    private int blue;
    @SerializedName("alpha")
    private int alpha;
    @SerializedName("sync")
    private boolean sync;
    @SerializedName("rainbow")
    private boolean rainbow;
    @SerializedName("staticRainbow")
    private boolean staticRainbow;
    @SerializedName("rainbowSpeed")
    private float rainbowSpeed;
    @SerializedName("rainbowSaturation")
    private float rainbowSaturation;
    @SerializedName("rainbowBrightness")
    private float rainbowBrightness;

    public ColorSetting(Function<Color, Component> componentFactory, CanBeVisible visibility, ArgumentType<Color> argumentType, Complexity complexity,
                        JsonParser<Color> parser, ConfigType configType, String description, Color defaultValue,
                        String name, int red, int green, int blue, int alpha, boolean sync, boolean rainbow,
                        boolean staticRainbow, float rainbowSpeed, float rainbowSaturation, float rainbowBrightness) {
        super(componentFactory, visibility, argumentType, complexity, parser, configType, description, defaultValue, name);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.sync = sync;
        this.rainbow = rainbow;
        this.staticRainbow = staticRainbow;
        this.rainbowSpeed = rainbowSpeed;
        this.rainbowSaturation = rainbowSaturation;
        this.rainbowBrightness = rainbowBrightness;
    }

    public static class Builder extends AbstractSettingBuilder<Color, ColorSetting, Builder> {
        public Builder() {

        }

        @Override
        public Builder withValue(Color value) {
            return super.withValue(value);
        }

        @Override
        protected ColorSetting create() {
            return null;
        }
    }

    private enum ColorSerializer implements JsonParser<Color> {
        INSTANCE;

        @Override
        public Color deserialize(JsonElement element) {
            return null;
        }

        @Override
        public JsonElement serialize(Color element) {
            return null;
        }
    }

}
