package com.rabbitminers.buildersbundle.registry;

import com.rabbitminers.buildersbundle.ArchitectsSatchel;
import com.rabbitminers.buildersbundle.networking.GrowItemStackPacket;
import com.rabbitminers.buildersbundle.networking.InventoryUpdatePacket;
import com.rabbitminers.buildersbundle.networking.SaveCompoundTagPacket;
import dev.architectury.networking.NetworkChannel;
import net.minecraft.resources.ResourceLocation;

public class BuildersBundleNetwork {
    public static final NetworkChannel HANDLER =
            NetworkChannel.create(new ResourceLocation(ArchitectsSatchel.MOD_ID, "main_channel"));
    public static void init() {
        HANDLER.register(GrowItemStackPacket.class,
                         GrowItemStackPacket::write,
                         GrowItemStackPacket::new,
                         GrowItemStackPacket::handle);
        HANDLER.register(SaveCompoundTagPacket.class,
                         SaveCompoundTagPacket::write,
                         SaveCompoundTagPacket::new,
                         SaveCompoundTagPacket::handle);
    }
}
