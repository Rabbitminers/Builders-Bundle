package com.rabbitminers.buildersbundle.registry;

import com.rabbitminers.buildersbundle.ArchitectsSatchel;
import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class AllItems {
    public static final CreativeModeTab EXAMPLE_TAB = CreativeTabRegistry
            .create(new ResourceLocation(ArchitectsSatchel.MOD_ID, "tab"),
                    () -> new ItemStack(ArchitectsSatchel.EXAMPLE_ITEM.get()));

}
