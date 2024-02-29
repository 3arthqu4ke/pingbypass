package me.earth.pingbypass.api.mappings;

import java.nio.file.Path;
import java.util.List;

public interface Remapper {
    void remap(Path path);

    List<Path> finish();

}
