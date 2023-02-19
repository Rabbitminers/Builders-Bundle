package com.rabbitminers.buildersbundle;

import com.rabbitminers.buildersbundle.gui.SatchelGui;
import com.rabbitminers.buildersbundle.registry.AllMenus;
import dev.architectury.registry.menu.MenuRegistry;

public class ArchitectsSatchelClient {
    public static void clientInit() {
        MenuRegistry.registerScreenFactory(AllMenus.SATCHEL_MENU.get(), SatchelGui::new);
    }
}
