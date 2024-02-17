package me.earth.pingbypass.server.mixins.inventory;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(AbstractContainerMenu.class)
public interface IAbstractContainerMenu {
    @Accessor("dataSlots")
    List<DataSlot> getDataSlots();

}
