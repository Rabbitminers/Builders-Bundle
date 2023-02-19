package com.rabbitminers.buildersbundle;

import com.google.common.base.Suppliers;
import com.rabbitminers.buildersbundle.registry.AllMenus;
import com.rabbitminers.buildersbundle.satchel.SatchelItem;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ArchitectsSatchel {
    public static final String MOD_ID = "buildersbundle";
    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
    // Registering a new creative tab
    public static final CreativeModeTab EXAMPLE_TAB = CreativeTabRegistry
            .create(new ResourceLocation(MOD_ID, "example_tab"),
                    () -> new ItemStack(ArchitectsSatchel.EXAMPLE_ITEM.get()));
    
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);

    public static final RegistrySupplier<Item> EXAMPLE_ITEM = ITEMS.register("builders_bundle", () ->
            new SatchelItem(new Item.Properties()
                    .stacksTo(1)
                    .tab(ArchitectsSatchel.EXAMPLE_TAB)));
    public static ResourceLocation asResource(String location) {
        return new ResourceLocation(MOD_ID, location);
    }
    public static void init() {
        ITEMS.register();
        AllMenus.init();
        
        System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
