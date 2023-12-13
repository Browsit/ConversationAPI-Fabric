package org.browsit.conversations.fabric.mixin;

import java.util.Iterator;
import org.browsit.conversations.api.ChatVisibility;
import org.browsit.conversations.api.Conversations;
import org.browsit.conversations.api.ConversationsForwarder;
import org.browsit.conversations.fabric.FabricConversationsMod;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Illusion
 * created on 2/22/2023
 */
@Mixin(ServerPlayNetworkHandler.class)
public abstract class ChatMessageMixin implements ConversationsForwarder<FabricConversationsMod> {

    @Shadow public ServerPlayerEntity player;

    @Override
    public void register(FabricConversationsMod fabricConversationsMod) {

    }

    // inject the handleDecoratedMessage method, at the HEAD of the method
    @Inject(method="handleDecoratedMessage" , at = @At("HEAD"), cancellable = true)
    public void onChatMessage(SignedMessage message, CallbackInfo ci) {
        Text text = message.getContent();
        String messageText = text.getString();

        Iterator<ServerPlayerEntity> recipients = player.getServer().getPlayerManager().getPlayerList().iterator();

        while (recipients.hasNext()) {
            ServerPlayerEntity recipient = recipients.next();

            Conversations.getConversationOf(recipient.getUuid()).ifPresent(conversation -> {
                if(conversation.getChatVisibility() != ChatVisibility.ALL) {
                    recipients.remove();
                }
            });
        }

        Conversations.getConversationOf(player.getUuid()).ifPresent(conversation -> {
            if(conversation.echoOn()) {
                player.sendMessage(text);
            }

            forwardInput(conversation, player.getUuid(), messageText, ci::cancel);
        });
    }

}
