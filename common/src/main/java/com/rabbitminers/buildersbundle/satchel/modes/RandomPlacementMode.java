package com.rabbitminers.buildersbundle.satchel.modes;

import com.rabbitminers.buildersbundle.container.SatchelInventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Random;

public class RandomPlacementMode implements IPlacementMode {
    @Override
    public @NonNull ItemStack getBlockForPlacement(SatchelInventory inventory) {
        List<ItemStack> items = inventory.getAllItems();
        if (items.isEmpty())
            return ItemStack.EMPTY;
        Random random = new Random();
        int randomIndex = random.nextInt(items.size());
        return items.get(randomIndex);
    }
}
