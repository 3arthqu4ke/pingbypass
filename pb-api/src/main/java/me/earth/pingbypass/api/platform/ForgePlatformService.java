package me.earth.pingbypass.api.platform;

import cpw.mods.cl.ModuleClassLoader;
import cpw.mods.jarhandling.SecureJar;
import dev.xdark.deencapsulation.Deencapsulation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.launch.Transformer;

import java.lang.module.Configuration;
import java.lang.module.ModuleReference;
import java.lang.module.ResolvedModule;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Slf4j
final class ForgePlatformService implements PlatformService {
    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void addToClassPath(Path path) {
        log.debug("Adding %s to class path!".formatted(path));
        SecureJar jar = SecureJar.from(path);
        Class<?> jarModuleReferenceClass;
        boolean lexForge = false;
        try {
            jarModuleReferenceClass = Class.forName("cpw.mods.cl.JarModuleFinder$JarModuleReference");
        } catch (ClassNotFoundException e) {
            log.debug("JarModuleFinder$JarModuleReference not found, we might be on a securemodules of LexForge");
            lexForge = true;
            jarModuleReferenceClass = Class.forName("net.minecraftforge.securemodules.SecureModuleFinder$Reference");
        }

        Deencapsulation.deencapsulate(jarModuleReferenceClass);
        Constructor<?> ctr = jarModuleReferenceClass.getDeclaredConstructor(SecureJar.ModuleDataProvider.class);
        ctr.setAccessible(true);
        ModuleReference jarModuleReference = (ModuleReference) ctr.newInstance(jar.moduleDataProvider());

        ModuleClassLoader classLoader = (ModuleClassLoader) Thread.currentThread().getContextClassLoader();
        // TODO: True!!!! log.info((classLoader instanceof TransformingClassLoader) + " is transforming classloader!");

        Class<?> moduleClassLoaderClass = lexForge ? ModuleClassLoader.class.getSuperclass() : ModuleClassLoader.class;
        Field resolvedRootsField = lexForge
                ? moduleClassLoaderClass.getDeclaredField("ourModulesSecure")
                : moduleClassLoaderClass.getDeclaredField("resolvedRoots");

        resolvedRootsField.setAccessible(true);
        Map<String, Object> resolvedRoots = (Map<String, Object>) resolvedRootsField.get(classLoader);
        resolvedRoots.put(jar.moduleDataProvider().descriptor().name(), jarModuleReference);

        if (lexForge) {
            Field ourModulesField = moduleClassLoaderClass.getDeclaredField("ourModules");
            ourModulesField.setAccessible(true);
            Map<String, Object> ourModules = (Map<String, Object>) ourModulesField.get(classLoader);
            ourModules.put(jar.moduleDataProvider().descriptor().name(), jarModuleReference);
        }

        Constructor<ResolvedModule> rmCtr = ResolvedModule.class.getDeclaredConstructor(Configuration.class, ModuleReference.class);
        rmCtr.setAccessible(true);

        Field configurationField = moduleClassLoaderClass.getDeclaredField("configuration");
        configurationField.setAccessible(true);
        Configuration configuration = (Configuration) configurationField.get(classLoader);

        Field packageLookupField = lexForge
                ? moduleClassLoaderClass.getDeclaredField("packageToOurModules")
                : moduleClassLoaderClass.getDeclaredField("packageLookup");

        packageLookupField.setAccessible(true);
        Map<String, ResolvedModule> packageLookup = (Map<String, ResolvedModule>) packageLookupField.get(classLoader);

        Field nameToModuleField = Configuration.class.getDeclaredField("nameToModule");
        nameToModuleField.setAccessible(true);
        Map<String, ResolvedModule> nameToModule = (Map<String, ResolvedModule>) nameToModuleField.get(configuration);
        Map<String, ResolvedModule> newNameToModule = new HashMap<>(nameToModule);

        for (String pack : jar.moduleDataProvider().descriptor().packages()) {
            ResolvedModule resolvedModule = packageLookup.get(pack);
            if (resolvedModule != null) {
                log.error("%s contains package %s but it is already being looked up by %s"
                        .formatted(path, pack, resolvedModule));
            } else {
                ResolvedModule rm = rmCtr.newInstance(configuration, jarModuleReference);
                newNameToModule.put(rm.name(), rm);
                packageLookup.put(pack, rm);
            }
        }

        nameToModuleField.set(configuration, Map.copyOf(newNameToModule));
    }

    @Override
    public Transformer.Registry injectTransformerRegistry() {
        return transformer -> log.warn("ASM Transformers are not yet supported on Forge. For now, use a MixinConfigPlugin");
    }

}
