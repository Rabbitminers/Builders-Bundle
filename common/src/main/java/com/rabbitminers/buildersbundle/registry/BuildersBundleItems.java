package com.rabbitminers.buildersbundle.registry;

import com.rabbitminers.buildersbundle.satchel.SatchelItem;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

import static com.rabbitminers.buildersbundle.ArchitectsSatchel.MOD_ID;

public class BuildersBundleItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);

    public static final RegistrySupplier<Item> BUILDERS_BUNDLE = register("builders_bundle", () ->
            new SatchelItem(new Item.Properties().stacksTo(1).tab(BuildersBundleItems.BUILDERS_BUNDLE_TAB)));

    public static final CreativeModeTab BUILDERS_BUNDLE_TAB = CreativeTabRegistry
            .create(new ResourceLocation(MOD_ID, "tab"),
                    () -> new ItemStack(BUILDERS_BUNDLE.get()));

    protected static <T extends Item> RegistrySupplier<T> register(String name, Supplier<T> b) {
        return ITEMS.register(name, b);
    }

    public static void init() {
        ITEMS.register();
    }
}
