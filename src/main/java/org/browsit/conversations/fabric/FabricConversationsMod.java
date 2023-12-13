package org.browsit.conversations.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

/**
 * @author Illusion
 * created on 2/22/2023
 */
public class FabricConversationsMod implements ModInitializer {

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            FabricConversations.init(server);
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            FabricConversations.cleanUp();
        });
    }

}
