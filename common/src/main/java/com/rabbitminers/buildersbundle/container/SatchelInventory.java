package com.rabbitminers.buildersbundle.container;


import com.rabbitminers.buildersbundle.networking.SaveCompoundTagPacket;
import com.rabbitminers.buildersbundle.registry.BuildersBundleNetwork;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SatchelInventory extends SimpleContainer {
    private final ItemStack stack;
    private int selectedSlot;
    public SatchelInventory(ItemStack stack, int count) {
        super(count);
        this.stack = stack;
        this.selectedSlot = 0;
        readItemStack();
    }

    public int getSelectedSlot() {
        return this.selectedSlot;
    }

    public ItemStack getSelectedItem() {
        return this.getItem(this.selectedSlot);
    }

    public void setSelectedSlot(int slot) {
        this.selectedSlot = slot;
    }

    public void setSelectedSlotClient(int slot, InteractionHand hand, ItemStack bundle) {
        CompoundTag nbt = bundle.hasTag() ? bundle.getTag() : new CompoundTag();
        if (nbt == null) return;
        nbt.putInt("SelectedSlot", slot);
        BuildersBundleNetwork.HANDLER
                .sendToServer(new SaveCompoundTagPacket(nbt, hand));
        this.selectedSlot = slot;
    }

    public List<ItemStack> getAllItems() {
        return IntStream.range(0, getContainerSize())
                .mapToObj(this::getItem)
                .filter(stack -> !stack.isEmpty())
                .collect(Collectors.toList());
    }

    public List<Integer> getAllFilledSlots() {
        return IntStream.range(0, getContainerSize())
                .filter(i -> !getItem(i).isEmpty())
                .boxed()
                .collect(Collectors.toList());
    }

    public void readItemStack() {
        if (stack.getTag() != null) {
            readNBT(stack.getTag());
            readSelectedItem(stack.getTag());
        }
    }

    public void writeItemStack() {
        if (isEmpty()) {
            stack.removeTagKey("Items");
        } else {
            writeNBT(stack.getOrCreateTag());
        }
    }

    private void readSelectedItem(CompoundTag tag) {
        if (tag.contains("SelectedSlot")) {
            this.selectedSlot = tag.getInt("SelectedSlot");
        } else {
            this.selectedSlot = 0;
        }
    }

    private void writeSelectedItem(CompoundTag tag) {
        tag.putInt("SelectedSlot", this.selectedSlot);
    }

    private void readNBT(CompoundTag compound) {
        final NonNullList<ItemStack> list = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, list);
        for (int index = 0; index < list.size(); index++) {
            setItem(index, list.get(index));
        }
    }

    private void writeNBT(CompoundTag compound) {
        final NonNullList<ItemStack> list = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        for (int index = 0; index < list.size(); index++) {
            list.set(index, getItem(index));
        }
        ContainerHelper.saveAllItems(compound, list, false);
    }

}
