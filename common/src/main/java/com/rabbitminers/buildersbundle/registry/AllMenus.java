package com.rabbitminers.buildersbundle.registry;

import com.rabbitminers.buildersbundle.ArchitectsSatchel;
import com.rabbitminers.buildersbundle.container.SatchelContainerMenu;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

import static com.rabbitminers.buildersbundle.ArchitectsSatchel.asResource;

public class AllMenus {

    // Registry.Menu in 1.19 -
    private static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ArchitectsSatchel.MOD_ID, Registry.MENU_REGISTRY);
    public static final RegistrySupplier<MenuType<SatchelContainerMenu>> SATCHEL_MENU =
            MENUS.register(asResource("builders_bundle"), () -> MenuRegistry.ofExtended(SatchelContainerMenu::new));

    public static void init() {
        MENUS.register();
    }
}
