package com.rabbitminers.buildersbundle.util;

import com.rabbitminers.buildersbundle.ArchitectsSatchel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class InventoryUtil {

    public static @Nullable InteractionHand getHandOfBundle(Player player, Item item) {
        return player.getOffhandItem().getItem() == item
                ? InteractionHand.OFF_HAND
                : player.getMainHandItem().getItem() == item
                ? InteractionHand.MAIN_HAND : null;
    }
}
