package com.rabbitminers.buildersbundle.events;

import com.rabbitminers.buildersbundle.registry.AllKeymaps;
import com.rabbitminers.buildersbundle.satchel.SatchelItem;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientScreenInputEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;

public class ClientEvents {
    public static void init() {
        if (Platform.getEnvironment() == Env.CLIENT)
            clientEvents();
    }

    public static void clientEvents() {
        ClientRawInputEvent.MOUSE_SCROLLED.register(SatchelItem::cycleActiveBlock);
        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            while (AllKeymaps.CYCLE_BACKWARDS.consumeClick()
                    || AllKeymaps.CYCLE_FORWARDS.consumeClick()) {
                SatchelItem.cycleSelectedBlock(minecraft,
                        AllKeymaps.CYCLE_FORWARDS.consumeClick());
            }
        });

    }
}
