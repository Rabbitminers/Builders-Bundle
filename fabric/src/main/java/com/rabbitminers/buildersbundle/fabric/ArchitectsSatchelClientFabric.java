package com.rabbitminers.buildersbundle.fabric;

import net.fabricmc.api.ClientModInitializer;

public class ArchitectsSatchelClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        com.rabbitminers.buildersbundle.ArchitectsSatchelClient.clientInit();
    }
}
