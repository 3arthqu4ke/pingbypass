package me.earth.pingbypass.api.files;

import java.nio.file.Path;

public interface FileManager {
    Path getRoot();

    Path get(String... path);

    Path mkFile(String... path);

    Path mkdirs(String... path);

    FileManager relative(String path);

}
