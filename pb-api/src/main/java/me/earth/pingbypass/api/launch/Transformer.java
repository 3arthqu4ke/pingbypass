package me.earth.pingbypass.api.launch;

import lombok.Getter;
import org.objectweb.asm.tree.ClassNode;

@Getter
public abstract class Transformer {
    private final String[] targetNames;

    protected Transformer(String... targetNames) {
        this.targetNames = targetNames;
    }

    public abstract void transform(ClassNode classNode);

    public interface Registry {
        void register(Transformer transformer);
    }

}
