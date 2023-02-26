package com.rabbitminers.buildersbundle;

import com.rabbitminers.buildersbundle.container.SatchelGui;
import com.rabbitminers.buildersbundle.events.ClientEvents;
import com.rabbitminers.buildersbundle.registry.BuildersBundleKeymaps;
import com.rabbitminers.buildersbundle.registry.BuildersBundleMenus;
import dev.architectury.registry.menu.MenuRegistry;

public class ArchitectsSatchelClient {
    public static void clientInit() {
        MenuRegistry.registerScreenFactory(BuildersBundleMenus.SATCHEL_MENU.get(), SatchelGui::new);

        BuildersBundleKeymaps.init();
        ClientEvents.init();
    }
}
