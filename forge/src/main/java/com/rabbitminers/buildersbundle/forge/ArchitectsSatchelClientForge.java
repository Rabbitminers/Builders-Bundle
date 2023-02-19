package com.rabbitminers.buildersbundle.forge;

import com.rabbitminers.buildersbundle.ArchitectsSatchel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ArchitectsSatchel.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArchitectsSatchelClientForge {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        com.rabbitminers.buildersbundle.ArchitectsSatchelClient.clientInit();
    }
}
