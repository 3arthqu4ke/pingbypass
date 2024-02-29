package me.earth.pingbypass.api.security;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class PublicKeyManager {
    private static final String FILE_SUFFIX = ".pem";

    @Getter
    private final Map<String, PublicKey> publicKeys = new HashMap<>();
    private final KeyPairService keyPairService;
    private final Path directory;

    PublicKeyManager(KeyPairService keyPairService, Path directory) throws SecurityException {
        this.keyPairService = keyPairService;
        this.directory = directory;
        this.load();
    }

    public void load() throws SecurityException {
        try {
            publicKeys.clear();
            Files.createDirectories(directory);
            try (var files = Files.walk(directory)) {
                files.forEach(this::readKey);
            }
        } catch (IOException e) {
            throw new SecurityException(e);
        }
    }

    public void addPublicKey(String alias, PublicKey key) throws SecurityException {
        try {
            Files.writeString(directory.resolve(alias.concat(FILE_SUFFIX)), keyPairService.toString(key));
            publicKeys.put(alias, key);
        } catch (IOException e) {
            throw new SecurityException(e);
        }
    }

    @SneakyThrows
    private void readKey(Path file) {
        String fileName = file.getFileName().toString();
        if (!Files.isDirectory(file) && fileName.endsWith(FILE_SUFFIX)) {
            String id = fileName.substring(0, fileName.length() - FILE_SUFFIX.length());

            try {
                String contents = Files.readString(file);
                PublicKey key = keyPairService.parsePublicKey(contents);
                publicKeys.put(id, key);
            } catch (IOException | SecurityException e) {
                e.printStackTrace();
            }
        }
    }

}
