package com.rabbitminers.buildersbundle;

import com.rabbitminers.buildersbundle.events.ClientEvents;
import com.rabbitminers.buildersbundle.container.SatchelGui;
import com.rabbitminers.buildersbundle.registry.AllKeymaps;
import com.rabbitminers.buildersbundle.registry.AllMenus;
import dev.architectury.registry.menu.MenuRegistry;

public class ArchitectsSatchelClient {
    public static void clientInit() {
        MenuRegistry.registerScreenFactory(AllMenus.SATCHEL_MENU.get(), SatchelGui::new);

        AllKeymaps.init();
        ClientEvents.init();
    }
}
