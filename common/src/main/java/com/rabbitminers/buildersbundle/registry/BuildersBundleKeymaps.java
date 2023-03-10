package com.rabbitminers.buildersbundle.registry;

import com.mojang.blaze3d.platform.InputConstants;
import com.rabbitminers.buildersbundle.ArchitectsSatchel;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;

public class BuildersBundleKeymaps {
    public static final KeyMapping CYCLE_BACKWARDS = new KeyMapping(
            "key." + ArchitectsSatchel.MOD_ID +  ".cycle_backwards", // Translation
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_LEFT,
            "catagory." + ArchitectsSatchel.MOD_ID + ".bundle"
    );

    public static final KeyMapping CYCLE_FORWARDS = new KeyMapping(
            "key." + ArchitectsSatchel.MOD_ID +  ".cycle_forwards", // Translation
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_RIGHT,
            "catagory." + ArchitectsSatchel.MOD_ID + ".bundle"
    );

    public static final KeyMapping CHANGE_PLACEMENT_MODE = new KeyMapping(
            "key." + ArchitectsSatchel.MOD_ID +  ".change_placement_mode", // Translation
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_GRAVE,
            "catagory." + ArchitectsSatchel.MOD_ID + ".bundle"
    );

    public static void init() {
        KeyMappingRegistry.register(CYCLE_FORWARDS);
        KeyMappingRegistry.register(CYCLE_BACKWARDS);
        KeyMappingRegistry.register(CHANGE_PLACEMENT_MODE);
    }
}
