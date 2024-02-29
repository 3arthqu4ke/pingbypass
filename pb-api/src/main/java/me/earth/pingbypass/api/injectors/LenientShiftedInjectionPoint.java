package me.earth.pingbypass.api.injectors;

import lombok.RequiredArgsConstructor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.spongepowered.asm.mixin.injection.IInjectionPointContext;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * Allows injecting into constructors anywhere. BE CAREFUL!
 * Sadly the Shift class in {@link InjectionPoint} is not accessible, so I had to copy its code.
 * Maybe {@link InjectionPoint#shift(InjectionPoint, int)} and the other wrapping injection points should inherit
 * the {@link #getTargetRestriction(IInjectionPointContext)} of the InjectionPoints they wrap?
 *
 * @see InjectionPoint#shift(InjectionPoint, int)
 */
@RequiredArgsConstructor
public class LenientShiftedInjectionPoint extends InjectionPoint {
    private final InjectionPoint input;
    private final int shift;

    @Override
    public String toString() {
        return "InjectionPoint(%s)[%s]".formatted(this.getClass().getSimpleName(), this.input);
    }

    @Override
    public RestrictTargetLevel getTargetRestriction(IInjectionPointContext context) {
        return RestrictTargetLevel.ALLOW_ALL;
    }

    @Override
    public boolean find(String desc, InsnList insns, Collection<AbstractInsnNode> nodes) {
        List<AbstractInsnNode> list = (nodes instanceof List) ? (List<AbstractInsnNode>) nodes : new ArrayList<>(nodes);

        this.input.find(desc, insns, nodes);

        for (ListIterator<AbstractInsnNode> iter = list.listIterator(); iter.hasNext();) {
            int sourceIndex = insns.indexOf(iter.next());
            int newIndex = sourceIndex + this.shift;
            if (newIndex >= 0 && newIndex < insns.size()) {
                iter.set(insns.get(newIndex));
            } else {
                // Shifted beyond the start or end of the insnlist, into the dark void
                iter.remove();
                // Decorate the injector with the info in case it fails
                int absShift = Math.abs(this.shift);
                char operator = absShift != this.shift ? '-' : '+';
                this.addMessage(
                        "LenientShiftedInjectionPoint offset outside the target bounds:" +
                                " Index (index(%d) %s offset(%d) = %d) is outside the allowed range (0-%d)",
                    sourceIndex, operator, absShift, newIndex, insns.size());
            }
        }

        if (nodes != list) {
            nodes.clear();
            nodes.addAll(list);
        }

        return nodes.size() > 0;
    }

}
