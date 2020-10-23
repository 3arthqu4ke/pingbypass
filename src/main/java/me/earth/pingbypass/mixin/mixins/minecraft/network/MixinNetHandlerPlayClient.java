package me.earth.pingbypass.mixin.mixins.minecraft.network;

import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient
{
    /*@Inject(method = "onDisconnect", at = @At(value = "HEAD"))
    private void onDisconnectHook(ITextComponent component, CallbackInfo callbackInfo)
    {
        Bus.EVENT_BUS.post(new DisconnectEvent(component));
    }*/

}
