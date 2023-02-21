package com.rabbitminers.buildersbundle.events;

import com.rabbitminers.buildersbundle.satchel.SatchelItem;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.BlockEvent;

import java.util.logging.Level;

public class ServerEvents {
    public static void init() {
        events();
    }

    public static void events() {
        BlockEvent.PLACE.register(SatchelItem::onBlockPlaced);
    }
}
