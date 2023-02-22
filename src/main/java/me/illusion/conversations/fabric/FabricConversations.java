package me.illusion.conversations.fabric;

import me.illusion.conversations.api.Conversations;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.minecraft.server.MinecraftServer;

/**
 * Created by Illusion on 2/22/2023
 * <p>
 * Bukkit wrapper for {@link Conversations}.
 */
public class FabricConversations {

    private static boolean initialized;

    /**
     * Initalizes the Conversations API.
     */
    public static void init(MinecraftServer server) {
        if (initialized) throw new IllegalStateException("Conversations(Fabric) API already initialized");

        Conversations.init(FabricServerAudiences.of(server));
        initialized = true;
    }

    /**
     * Cleans up the Conversations API.
     */
    public static void cleanUp() {
        if (!initialized)
            throw new IllegalStateException("Conversations(Fabric) API not initialized");

        Conversations.cleanUp();
    }

}
