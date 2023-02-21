package com.rabbitminers.buildersbundle.events;

import com.rabbitminers.buildersbundle.satchel.SatchelItem;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientScreenInputEvent;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;

public class ClientEvents {
    public static void init() {
        if (Platform.getEnvironment() == Env.CLIENT)
            clientEvents();
    }

    public static void clientEvents() {
        ClientRawInputEvent.MOUSE_SCROLLED.register(SatchelItem::cycleActiveBlock);
    }
}
