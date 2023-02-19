package com.rabbitminers.buildersbundle.fabric;

import com.rabbitminers.buildersbundle.ArchitectsSatchel;
import net.fabricmc.api.ModInitializer;

public class ArchitectsSatchelImpl implements ModInitializer {
    @Override
    public void onInitialize() {
        ArchitectsSatchel.init();
    }
}
