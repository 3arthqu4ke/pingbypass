package me.earth.pingbypass.api.config.impl;

import lombok.Data;
import lombok.Getter;
import lombok.Synchronized;
import me.earth.pingbypass.api.config.ConfigException;
import me.earth.pingbypass.api.config.ConfigType;
import me.earth.pingbypass.api.config.ConfigWithCurrentConfig;
import me.earth.pingbypass.api.config.Configurable;
import me.earth.pingbypass.api.files.FileManager;
import me.earth.pingbypass.api.traits.Nameable;
import me.earth.pingbypass.api.util.exceptions.ExceptionUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class AbstractJsonConfig implements ConfigFromFile, ConfigWithCurrentConfig<Nameable>, Configurable {
    private final Map<String, ConfigHandle> handles = new ConcurrentHashMap<>();
    private final FileManager fileManager;
    @Getter
    private final ConfigType configType;
    private ConfigHandle currentConfig;

    public AbstractJsonConfig(FileManager fileManager, ConfigType configType) {
        this.fileManager = fileManager.relative(configType.getName());
        this.configType = configType;
    }

    @Override
    public void refresh() throws ConfigException {
        ensureDir();
        try (var files = Files.list(fileManager.getRoot())) {
            handles.clear();
            files.filter(Predicate.not(Files::isDirectory))
                 .filter(f -> f.getFileName().endsWith(_JSON))
                 .forEach(f -> {
                     String fileName = f.getFileName().toString();
                     String configName = fileName.substring(0, fileName.length() - _JSON.length());
                     handles.put(configName, createHandle(configName));
                 });

            ConfigHandle current = getCurrentConfig();
            handles.put(current.getName(), current);
        } catch (IOException e) {
            throw new ConfigException(e);
        }
    }

    @Override
    @Synchronized
    public void save() throws ConfigException {
        getCurrentConfig(); // initialize default config
        ExceptionUtil.forEach(handles.keySet(), this::save);
    }

    @Override
    @Synchronized
    public void save(String name) throws ConfigException {
        ensureDir();
        var handle = handles.computeIfAbsent(name, v -> new ConfigHandle(name, fileManager.get(name + _JSON)));
        this.toFile(handle.getPath());
    }

    @Override
    @Synchronized
    public void load(String name) throws ConfigException {
        ensureDir();
        Path path = fileManager.get(name + _JSON);
        this.fromFile(path);
        currentConfig = handles.computeIfAbsent(name, v -> new ConfigHandle(name, path));
    }

    @Override
    @Synchronized
    public void delete(String name) throws ConfigException {
        ensureDir();
        ConfigHandle handle = handles.get(name);
        if (getCurrentConfig().equals(handle)) {
            throw new ConfigException("Cannot delete current config, please save a new config first!");
        }

        try {
            Files.deleteIfExists(fileManager.get(getName(), name + ".json"));
        } catch (IOException e) {
            throw new ConfigException(e);
        }
    }

    @NotNull
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Iterator<Nameable> iterator() {
        return (Iterator) handles.values().iterator();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Stream<Nameable> stream() {
        return (Stream) handles.values().stream();
    }

    @Override
    public ConfigHandle getCurrentConfig() {
        if (currentConfig == null) {
            currentConfig = createHandle("default");
            handles.put("default", currentConfig);
        }

        return currentConfig;
    }

    private void ensureDir() throws ConfigException {
        try {
            Files.createDirectories(fileManager.getRoot());
        } catch (IOException e) {
            throw new ConfigException("Failed to create directory " + fileManager.getRoot(), e);
        }
    }

    private ConfigHandle createHandle(String name) {
        return new ConfigHandle(name, fileManager.get(name + ".json"));
    }

    @Data
    public static final class ConfigHandle implements Nameable {
        private final String name;
        private final Path path;
    }

}
