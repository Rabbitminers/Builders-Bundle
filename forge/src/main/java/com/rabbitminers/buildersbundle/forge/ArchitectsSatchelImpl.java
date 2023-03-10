package com.rabbitminers.buildersbundle.forge;

import com.rabbitminers.buildersbundle.ArchitectsSatchel;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ArchitectsSatchel.MOD_ID)
public class ArchitectsSatchelImpl {
    public ArchitectsSatchelImpl() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ArchitectsSatchel.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ArchitectsSatchel.init();
    }
}
