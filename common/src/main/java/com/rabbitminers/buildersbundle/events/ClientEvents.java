package com.rabbitminers.buildersbundle.events;

import com.rabbitminers.buildersbundle.container.SatchelInventory;
import com.rabbitminers.buildersbundle.registry.BuildersBundleItems;
import com.rabbitminers.buildersbundle.registry.BuildersBundleKeymaps;
import com.rabbitminers.buildersbundle.satchel.SatchelItem;
import com.rabbitminers.buildersbundle.util.InventoryUtil;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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

        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            while (BuildersBundleKeymaps.CHANGE_PLACEMENT_MODE.consumeClick()) {
                Player player = Minecraft.getInstance().player;
                if (player == null) break;
                InteractionHand usedHand = InventoryUtil
                        .getHandOfItem(player, BuildersBundleItems.BUILDERS_BUNDLE.get());
                if (usedHand == null) break;
                ItemStack bundleStack = player.getItemInHand(usedHand);
                SatchelItem.cyclePlacementModeClient(bundleStack);
            }
        });

        BlockEvent.PLACE.register(SatchelItem::onBlockPlaced);
    }
}
