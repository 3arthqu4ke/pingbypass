package me.earth.pingbypass.api.platform;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.launch.Transformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.transformers.MixinClassWriter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
final class TransformingRegistry implements Transformer.Registry {
    private final Map<String, List<Transformer>> transformers = new HashMap<>();
    private final AtomicBoolean injected = new AtomicBoolean();

    @Override
    public void register(Transformer transformer) {
        for (String target : transformer.getTargetNames()) {
            transformers.computeIfAbsent(target.replace(".", "/"), v -> new CopyOnWriteArrayList<>()).add(transformer);
        }
    }

    public byte[] transform(String className, byte[] bytes) {
        List<Transformer> transformerList = transformers.get(className.replace(".", "/"));
        if (transformerList != null) {
            log.info("Transforming " + className);
            ClassNode cn = new ClassNode();
            ClassReader cr = new ClassReader(bytes);
            cr.accept(cn, ClassReader.EXPAND_FRAMES);

            for (Transformer transformer : transformerList) {
                transformer.transform(cn);
            }

            ClassWriter cw = new MixinClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
            cn.accept(cw);
            return cw.toByteArray();
        }

        return bytes;
    }

    @RequiredArgsConstructor
    public static class DelegatingMixinTransformer implements IMixinTransformer {
        @Delegate
        private final IMixinTransformer delegate;
    }

    static class MixinTransformerRegistry extends TransformingRegistry.DelegatingMixinTransformer {
        private final TransformingRegistry transformer;

        public MixinTransformerRegistry(IMixinTransformer delegate, TransformingRegistry transformer) {
            super(delegate);
            this.transformer = transformer;
        }

        @Override
        public byte[] transformClassBytes(String name, String transformedName, byte[] basicClass) {
            basicClass = super.transformClassBytes(name, transformedName, basicClass);
            return transformer.transform(name, basicClass);
        }
    }

}

