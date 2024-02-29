package me.earth.pingbypass.api.injectors;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;

/**
 * This is needed because using {@link At#shift()} causes Mixin to wrap our custom injector in a Shift InjectionPoint.
 * Allows injecting into constructors anywhere. BE CAREFUL!
 *
 * @see LenientBeforeFieldAccess
 * @see At.Shift#AFTER
 */
@SuppressWarnings("unused")
// I will not make use of this because I have multiple MixinConfigs? Instead, I will just use the fully qualified name.
@InjectionPoint.AtCode(namespace = "PINGBYPASS", value = "LENIENT_FIELD_AFTER")
public class LenientAfterFieldAccess extends LenientShiftedInjectionPoint {
    public LenientAfterFieldAccess(InjectionPointData data) {
        super(new LenientBeforeFieldAccess(data), 1 /* At.Shift.AFTER */);
    }

}
