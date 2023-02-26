package com.rabbitminers.buildersbundle;

import com.google.common.base.Suppliers;
import com.rabbitminers.buildersbundle.events.ServerEvents;
import com.rabbitminers.buildersbundle.registry.AllItems;
import com.rabbitminers.buildersbundle.registry.AllMenus;
import com.rabbitminers.buildersbundle.registry.BuildersBundleNetwork;
import dev.architectury.registry.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ArchitectsSatchel {
    public static final String MOD_ID = "buildersbundle";
    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
    public static ResourceLocation asResource(String location) {
        return new ResourceLocation(MOD_ID, location);
    }
    public static void init() {
        AllItems.init();
        AllMenus.init();
        ServerEvents.init();
        BuildersBundleNetwork.init();
        
        System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
