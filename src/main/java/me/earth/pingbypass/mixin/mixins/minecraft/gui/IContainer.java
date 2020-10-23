package me.earth.pingbypass.mixin.mixins.minecraft.gui;

import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Container.class)
public interface IContainer
{

    @Accessor(value = "transactionID")
    void setTransactionID(short id);

}
