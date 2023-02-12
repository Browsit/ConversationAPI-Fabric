package me.illusion.testmod.mixin;

import com.pixelsandmagic.conversations.api.ChatVisibility;
import com.pixelsandmagic.conversations.api.Conversations;
import com.pixelsandmagic.conversations.api.ConversationsForwarder;
import java.util.Iterator;
import me.illusion.testmod.Testmod;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ChatMessageMixin implements ConversationsForwarder<Testmod> {

    @Shadow public ServerPlayerEntity player;

    @Override
    public void register(Testmod testmod) {

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
