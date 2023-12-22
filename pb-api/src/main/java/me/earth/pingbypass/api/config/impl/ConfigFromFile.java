package me.earth.pingbypass.api.config.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import me.earth.pingbypass.api.config.ConfigException;
import me.earth.pingbypass.api.config.ConfigParseException;
import me.earth.pingbypass.api.config.Configurable;
import me.earth.pingbypass.api.config.JsonSerializable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public interface ConfigFromFile extends Configurable {
    default void toFile(Path path) throws ConfigException {
        try (var osw = new OutputStreamWriter(Files.newOutputStream(path))) {
            JsonSerializable.GSON.toJson(this.toJson(), osw);
        } catch (IllegalArgumentException e) {
            throw new ConfigException("%s contains illegal characters!".formatted(path.getFileName()), e);
        } catch (JsonIOException | IOException e) {
            throw new ConfigException(e);
        }
    }

    default void fromFile(Path path) throws ConfigException {
        if (!Files.exists(path)) {
            throw new ConfigException(
                "The config '%s' does not exist! Save it first.".formatted(path.getFileName()));
        }

        try (var isr = new InputStreamReader(Files.newInputStream(path))) {
            JsonElement element = JsonParser.parseReader(isr);
            this.fromJson(element);
        } catch (IllegalStateException e) {
            throw new ConfigException("Illegal State! " + path.getFileName(), e);
        } catch (IllegalArgumentException e) {
            throw new ConfigException("%s contains illegal characters!".formatted(path.getFileName()), e);
        } catch (NoSuchFileException e) {
            throw new ConfigException(
                "The config '%s' does not exist! Save it first.".formatted(path.getFileName()), e);
        } catch (JsonParseException | IOException | ConfigParseException e) {
            throw new ConfigException(e);
        }
    }

}
