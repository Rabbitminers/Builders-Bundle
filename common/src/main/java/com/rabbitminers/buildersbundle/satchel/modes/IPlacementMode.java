package com.rabbitminers.buildersbundle.satchel.modes;

import com.rabbitminers.buildersbundle.container.SatchelInventory;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface IPlacementMode {
    @NonNull
    public ItemStack getBlockForPlacement(SatchelInventory inventory);
}
