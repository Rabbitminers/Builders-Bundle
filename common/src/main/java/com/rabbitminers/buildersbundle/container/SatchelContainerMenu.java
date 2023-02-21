package com.rabbitminers.buildersbundle.container;

import com.rabbitminers.buildersbundle.registry.AllMenus;
import com.rabbitminers.buildersbundle.satchel.SatchelItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SatchelContainerMenu extends AbstractContainerMenu {
    public final SatchelInventory inventory;
    public final int containerRows = 3;

    public SatchelContainerMenu(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(id, playerInventory);
    }

    public SatchelContainerMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new SatchelInventory(playerInventory.getSelected(),
                SatchelItem.getSlotCount()));
    }

    public SatchelContainerMenu(int id, Inventory playerInventory, SatchelInventory inventory) {
        super(AllMenus.SATCHEL_MENU.get(), id);
        this.inventory = inventory;
        checkContainerSize(inventory, SatchelItem.getSlotCount());
        inventory.startOpen(playerInventory.player);

        int k = (containerRows - 4) * 18;

        for(int l = 0; l < this.containerRows; ++l) {
            for(int m = 0; m < 9; ++m) {
                this.addSlot(new SatchelSlot(inventory, m + l * 9, 8 + m * 18, 18 + l * 18));
            }
        }

        for(int l = 0; l < 3; ++l) {
            for(int m = 0; m < 9; ++m) {
                this.addSlot(new Slot(playerInventory, m + l * 9 + 9, 8 + m * 18, 103 + l * 18 + k));
            }
        }

        for(int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 161 + k));
        }
    }

    // Re-used from chest quick inserting
    public @NotNull ItemStack quickMoveStack(Player player, int i) {
        ItemStack itemStack = ItemStack.EMPTY;
        if (i == 27) // Current Choice
            return ItemStack.EMPTY;

        Slot slot = this.slots.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (i < this.containerRows * 9) {
                if (!this.moveItemStackTo(itemStack2, this.containerRows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, 0, this.containerRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    @Override
    public void broadcastChanges() {
        inventory.writeItemStack();
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.inventory.stillValid(playerIn);
    }
}