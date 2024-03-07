package me.earth.pingbypass.api.resource;

import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.files.PathUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

// TODO: unsupported protocol union?
@Slf4j
@MethodsReturnNonnullByDefault
@ExtensionMethod(PathUtil.class)
final class PbPackResources extends AbstractPackResources {
    private final Set<String> nameSpaces;
    @Nullable
    private final Path jarPath;

    public PbPackResources(String namespace, @Nullable Path jarPath) {
        super(namespace, false);
        this.nameSpaces = Collections.singleton(namespace);
        this.jarPath = jarPath;
    }

    @Override
    @Nullable
    public IoSupplier<InputStream> getRootResource(String... location) {
        String path = String.join("/", location);
        return hasResource(packId(), path) ? () -> getInputStream(packId(), path) : null;
    }

    @Override
    @Nullable
    public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation location) {
        if (!packId().equals(location.getNamespace())
                || !hasResource(location.getNamespace(), location.getPath())
                || packType != PackType.CLIENT_RESOURCES) {
            return null;
        }

        return () -> getInputStream(location.getNamespace(), location.getPath());
    }

    @Override
    public void listResources(PackType packType, String nameSpace, String path, ResourceOutput output) {
        // TODO: see PathPackResources
        if (packType == PackType.CLIENT_RESOURCES && nameSpace.equals(packId())) {
            try {
                listResources(path).forEach(resource -> {
                    String rPath = "%s/%s".formatted(path, resource);
                    output.accept(new ResourceLocation(packId(), rPath), () -> getInputStream(packId(), rPath));
                });
            } catch (Throwable t) { // Exceptions thrown within this method seem to get swallowed
                log.error("Throwable while listing resources %s, %s, %s".formatted(packType, nameSpace, path), t);
                throw t;
            }
        }
    }

    @Override
    public Set<String> getNamespaces(PackType packType) {
        return packType == PackType.CLIENT_RESOURCES ? nameSpaces : Collections.emptySet();
    }

    @Override
    public void close() {

    }

    private InputStream getInputStream(String nameSpace, String path) throws IOException {
        String fullPath = "assets/%s/%s".formatted(nameSpace, path);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fullPath);
        if (inputStream == null) {
            log.error("Could not find resource " + fullPath);
            throw new IOException("Could not find resource " + fullPath);
        }

        return inputStream;
    }

    private boolean hasResource(String nameSpace, String path) {
        try (InputStream ignored = getInputStream(nameSpace, path)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private Set<String> listResources(String subPath) {
        String path = "assets/%s/%s/".formatted(packId(), subPath);
        log.info("Listing resources for subPath {}, path: {}", subPath, path);
        if (jarPath != null) {
            return readJar(jarPath.toString(), path);
        }

        URL resUrl = getClass().getClassLoader().getResource(path);
        if (resUrl != null && "file".equals(resUrl.getProtocol())) {
            Path fileLocation;
            try (var stream = Files.walk((fileLocation = Paths.get(resUrl.toURI())))) {
                String fl = fileLocation.toString();
                return stream
                        .map(Path::toAbsolutePath)
                        .map(Path::toString)
                        .filter(p -> p.length() > fl.length() + File.separator.length())
                        .map(s -> s.substring(fl.length() + File.separator.length()))
                        .collect(Collectors.toSet());
            } catch (IOException | URISyntaxException e) {
                log.error("Failed to read resource directory " + path, e);
                return Collections.emptySet();
            }
        }

        if (resUrl == null) {
            log.info("Failed to find " + path);
            return Collections.emptySet();
        }

        if ("jar".equals(resUrl.getProtocol())) {
            String resourceJar = resUrl.getPath().substring(5, resUrl.getPath().indexOf("!"));
            return readJar(URLDecoder.decode(resourceJar, StandardCharsets.UTF_8), path);
        }

        // TODO: protocol union?!
        log.error("%s unsupported protocol %s".formatted(packId(), resUrl));
        return Collections.emptySet();
    }

    private Set<String> readJar(String path, String resourcePath) {
        try (JarFile jar = new JarFile(path)) {
            Set<String> resources = new HashSet<>();
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }

                String name = entry.getName();
                if (name.startsWith(resourcePath)) {
                    resources.add(name.substring(resourcePath.length()));
                }
            }

            return resources;
        } catch (IOException e) {
            log.error("Could not read Jar " + jarPath, e);
            return Collections.emptySet();
        }
    }

}
