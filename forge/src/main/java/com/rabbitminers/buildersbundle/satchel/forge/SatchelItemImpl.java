package com.rabbitminers.buildersbundle.satchel.forge;

import com.rabbitminers.buildersbundle.container.SatchelContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;

public class SatchelItemImpl {
    public static void openScreen(ServerPlayer player, MenuProvider item, InteractionHand hand) {
        NetworkHooks.openGui(player, item, buf -> {
            // TODO: Write items
        });
    }

    public static MenuConstructor getServerMenuProvider(ItemStack stack) {
        return null;
    }

    public static SatchelContainerMenu getClientMenu(int id, Inventory playerInventory, FriendlyByteBuf extra) {
        return null;
    }
}
