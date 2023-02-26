package com.rabbitminers.buildersbundle.satchel;

import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.awt.*;

public enum PlacementMode {
    SIMPLE(new TranslatableComponent("buildersbundle.placement.simple")),
    RANDOMIZED(new TranslatableComponent("buildersbundle.placement.random"))
    ;

    private final BaseComponent name;

    PlacementMode(BaseComponent name) {
        this.name = name;
    }

    public PlacementMode cycleMode(PlacementMode mode) {
        return values()[(ordinal() + 1) % values().length];
    }

}
