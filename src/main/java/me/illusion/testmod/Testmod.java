package me.illusion.testmod;

import com.pixelsandmagic.conversations.api.Conversations;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;

public class Testmod implements ModInitializer {

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            Conversations.init(FabricServerAudiences.of(server));
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            Conversations.cleanUp();
        });
    }

}
