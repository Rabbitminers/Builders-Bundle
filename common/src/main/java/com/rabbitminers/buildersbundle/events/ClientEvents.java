package com.rabbitminers.buildersbundle.events;

import com.rabbitminers.buildersbundle.registry.BuildersBundleKeymaps;
import com.rabbitminers.buildersbundle.satchel.SatchelItem;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;

public class ClientEvents {
    public static void init() {
        if (Platform.getEnvironment() == Env.CLIENT)
            clientEvents();
    }

    public static void clientEvents() {
        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            while (BuildersBundleKeymaps.CYCLE_BACKWARDS.consumeClick() || BuildersBundleKeymaps.CYCLE_FORWARDS.consumeClick()) {
                SatchelItem.cycleSelectedBlock(minecraft,
                        BuildersBundleKeymaps.CYCLE_FORWARDS.isDown());
            }
        });

        BlockEvent.PLACE.register(SatchelItem::onBlockPlaced);
    }
}
