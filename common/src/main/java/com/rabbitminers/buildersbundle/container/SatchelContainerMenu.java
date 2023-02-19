package com.rabbitminers.buildersbundle.container;

import com.rabbitminers.buildersbundle.registry.AllMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

public class SatchelContainerMenu extends AbstractContainerMenu {
    public final SatchelInventory inventory;

    public SatchelContainerMenu(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(id, playerInventory);
    }

    public SatchelContainerMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new SatchelInventory(27));
    }

    public SatchelContainerMenu(int id, Inventory playerInventory, SatchelInventory inventory) {
        super(AllMenus.SATCHEL_MENU.get(), id);
        this.inventory = inventory;
        checkContainerSize(inventory, 27);
        inventory.startOpen(playerInventory.player);

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
    }


    @Override
    public boolean stillValid(Player playerIn) {
        return this.inventory.stillValid(playerIn);
    }
}