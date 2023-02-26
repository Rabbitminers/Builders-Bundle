package com.rabbitminers.buildersbundle.satchel;

import com.rabbitminers.buildersbundle.container.SatchelInventory;
import com.rabbitminers.buildersbundle.satchel.modes.EmptyPlacementMode;
import com.rabbitminers.buildersbundle.satchel.modes.IPlacementMode;
import com.rabbitminers.buildersbundle.satchel.modes.RandomPlacementMode;
import com.rabbitminers.buildersbundle.satchel.modes.SimplePlacementMode;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

public enum PlacementMode {
    NONE(new TranslatableComponent("buildersbundle.placement.none"), new EmptyPlacementMode()),
    SIMPLE(new TranslatableComponent("buildersbundle.placement.simple"), new SimplePlacementMode()),
    RANDOMIZED(new TranslatableComponent("buildersbundle.placement.random"), new RandomPlacementMode())
    ;

    private final BaseComponent name;
    private final IPlacementMode mode;

    PlacementMode(BaseComponent name, IPlacementMode mode) {
        this.name = name;
        this.mode = mode;
    }

    public PlacementMode cycleMode(PlacementMode mode) {
        PlacementMode newMode = values()[(ordinal() + 1) % values().length];
        return newMode == NONE ? cycleMode(newMode) : newMode;
    }

    public ItemStack getStackForPlacement(SatchelInventory inventory) {
        return this.mode.getBlockForPlacement(inventory);
    }
}
