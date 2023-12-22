package me.earth.pingbypass.api.files;

import lombok.experimental.UtilityClass;

import java.nio.file.Path;

@UtilityClass
public class PathUtil {
    public static Path resolve(Path root, String... path) {
        Path result = root;
        for (String s : path) {
            result = result.resolve(s);
        }

        return result;
    }

}
