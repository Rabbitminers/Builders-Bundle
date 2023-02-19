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

public class SatchelContainerMenu extends AbstractContainerMenu {
    public final SatchelInventory inventory;
    public final int containerRows = 3;

    public SatchelContainerMenu(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(id, playerInventory);
    }

    public SatchelContainerMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new SatchelInventory(SatchelItem.getSlotCount()));
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

        /*

        // Add satchel inventory
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                this.addSlot(new SatchelSlot(inventory, j + (i + 1) * 9, 8 + j * 18, 15 + i * 18 + 3));

        // Add player inventory
        for (int si = 0; si < 3; ++si)
            for (int sj = 0; sj < 9; ++sj)
                this.addSlot(new Slot(playerInventory, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18 + 1));

        // Hotbar
        for (int si = 0; si < 9; ++si)
            this.addSlot(new Slot(playerInventory, si, 8 + si * 18, 142 + 1));

         */
    }

    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(i);
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
    public boolean stillValid(Player playerIn) {
        return this.inventory.stillValid(playerIn);
    }
}