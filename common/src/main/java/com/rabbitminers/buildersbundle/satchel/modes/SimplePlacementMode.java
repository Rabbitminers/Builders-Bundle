package com.rabbitminers.buildersbundle.satchel.modes;

import com.rabbitminers.buildersbundle.container.SatchelInventory;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

public class SimplePlacementMode implements IPlacementMode {
    @Override
    public @NonNull ItemStack getBlockForPlacement(SatchelInventory inventory) {
        return inventory.getSelectedItem();
    }
}
