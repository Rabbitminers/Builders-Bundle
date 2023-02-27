package com.rabbitminers.buildersbundle.satchel.modes;

import com.rabbitminers.buildersbundle.container.SatchelInventory;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class RandomPlacementMode implements IPlacementMode {
    public static int getCurrentSecond() {
        LocalDateTime now = LocalDateTime.now();
        return now.getSecond();
    }

    @Override
    public @NonNull ItemStack getBlockForPlacement(SatchelInventory inventory) {
        List<ItemStack> items = inventory.getAllItems();
        if (items.isEmpty())
            return ItemStack.EMPTY;
        Random random = new Random();
        // Use current second to prevent the rave that occurs from calling this each second
        random.setSeed(getCurrentSecond());
        int randomIndex = random.nextInt(items.size());
        return items.get(randomIndex);
    }
}
