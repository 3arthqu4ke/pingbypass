package me.earth.pingbypass.security.mixins;

import me.earth.pingbypass.security.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// TODO: lets not do that, just make this a tool to create public/private keys!
@Mixin(value = SecurityManagerFactory.class, remap = false)
public abstract class MixinSecurityManagerFactory {
    // suppress warnings from the dev plugin, once we have built a jar of the pb-security-lib it should be fine.
    @SuppressWarnings({"UnresolvedMixinReference", "RedundantSuppression", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
        method = "fromConfig(Ljava/nio/file/Path;Lme/earth/pingbypass/api/config/properties/PropertyConfig;Lme/earth/pingbypass/security/KeyPairService;)Lme/earth/pingbypass/api/security/SecurityManager;",
        at = @At(
            value = "NEW",
            target = "Lme/earth/pingbypass/security/SecurityManagerImpl;<init>(Lme/earth/pingbypass/security/PrivateKeyManager;Lme/earth/pingbypass/security/PublicKeyManager;Lme/earth/pingbypass/security/KeyPairService;)V"))
    private static SecurityManagerImpl fromConfigHook(PrivateKeyManager privateKeyManager,
                                                      PublicKeyManager publicKeyManager,
                                                      KeyPairService keyPairService) throws SecurityException {
        var securityManager = new SecurityManagerImpl(privateKeyManager, publicKeyManager, keyPairService);
        SecurityManagerImplHelper.addKeyIfNecessary(securityManager);
        return securityManager;
    }

}
