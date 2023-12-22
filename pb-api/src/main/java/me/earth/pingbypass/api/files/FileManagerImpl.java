package me.earth.pingbypass.api.files;

import lombok.Getter;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;

@Getter
public class FileManagerImpl implements FileManager {
    private static final Set<OpenOption> CREATE = Set.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE);

    private final Path root;

    public FileManagerImpl(Path root) {
        this.root = root;
    }

    @Override
    public Path get(String... path) {
        return PathUtil.resolve(root, path);
    }

    @Override
    @SneakyThrows
    public Path mkFile(String... path) {
        Path file = PathUtil.resolve(root, path);
        Files.createDirectories(file.getParent());
        Files.newByteChannel(file, CREATE).close();
        return file;
    }

    @Override
    @SneakyThrows
    public Path mkdirs(String... path) {
        return Files.createDirectories(PathUtil.resolve(root, path));
    }

    @Override
    public FileManager relative(String path) {
        return new FileManagerImpl(root.resolve(path));
    }

}
