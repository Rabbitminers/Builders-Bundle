package com.rabbitminers.buildersbundle.satchel.modes;

import com.rabbitminers.buildersbundle.container.SatchelInventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EmptyPlacementMode implements IPlacementMode{
    @Override
    public @NotNull ItemStack getBlockForPlacement(SatchelInventory inventory) {
        return ItemStack.EMPTY;
    }
}
