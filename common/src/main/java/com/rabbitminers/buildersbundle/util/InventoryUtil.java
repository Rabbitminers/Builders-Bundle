package com.rabbitminers.buildersbundle.util;

import com.rabbitminers.buildersbundle.ArchitectsSatchel;
import com.rabbitminers.buildersbundle.container.SatchelInventory;
import com.rabbitminers.buildersbundle.satchel.SatchelItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryUtil {

    public static @Nullable InteractionHand getHandOfBundle(Player player, Item item) {
        return player.getOffhandItem().getItem() == item
                ? InteractionHand.OFF_HAND
                : player.getMainHandItem().getItem() == item
                ? InteractionHand.MAIN_HAND : null;
    }

    public static int getFirstInventoryIndex(Player player, Item item) {
        return getFirstInventoryIndex(player.getInventory(), item);
    }

    public static int getFirstInventoryIndex(SatchelInventory inventory, Item item) {
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack currentStack = inventory.getItem(i);
            if (!currentStack.isEmpty() && currentStack.sameItem(new ItemStack(item))) {
                return i;
            }
        }
        return -1;
    }

    public static int getFirstInventoryIndex(Inventory inventory, Item item) {
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack currentStack = inventory.getItem(i);
            if (!currentStack.isEmpty() && currentStack.sameItem(new ItemStack(item))) {
                return i;
            }
        }
        return -1;
    }

    public static ItemStack findItemInBundles(Inventory inventory, Item query) {
        List<ItemStack> bundles = inventory.items.stream()
                .filter(item -> item.is(ArchitectsSatchel.EXAMPLE_ITEM.get()))
                .toList();

        for (ItemStack bundle : bundles) {
            SatchelInventory bundleInventory = SatchelItem.getInventory(bundle);
            int index = InventoryUtil
                    .getFirstInventoryIndex(bundleInventory, query);
            if (index != -1)
                return bundleInventory.getItem(index);
        }

        return ItemStack.EMPTY;
    }
}
