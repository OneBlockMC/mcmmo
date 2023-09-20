package com.gmail.nossr50.chat.mailer;

import com.gmail.nossr50.chat.author.Author;
import com.gmail.nossr50.chat.message.ChatMessage;
import com.gmail.nossr50.chat.message.PartyChatMessage;
import com.gmail.nossr50.datatypes.chat.ChatChannel;
import com.gmail.nossr50.datatypes.party.Party;
import com.gmail.nossr50.events.chat.McMMOChatEvent;
import com.gmail.nossr50.events.chat.McMMOPartyChatEvent;
import com.gmail.nossr50.mcMMO;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class PartyChatMailer extends AbstractChatMailer {

    public PartyChatMailer(@NotNull Plugin pluginRef) {
        super(pluginRef);
    }

    /**
     * Processes a chat message from an author to an audience of party members
     *
     * @param author    the author
     * @param rawString the raw message as the author typed it before any styling
     * @param isAsync   whether or not this is being processed asynchronously
     * @param canColor  whether or not the author can use colors in chat
     */
    public void processChatMessage(@NotNull Author author, @NotNull String rawString, @NotNull Party party, boolean isAsync, boolean canColor, boolean isLeader) {
        PartyChatMessage chatMessage = new PartyChatMessage(pluginRef, author, constructPartyAudience(party), rawString, addStyle(author, rawString, canColor, isLeader), party);

        McMMOChatEvent chatEvent = new McMMOPartyChatEvent(pluginRef, chatMessage, party, isAsync);
        Bukkit.getPluginManager().callEvent(chatEvent);

        if (!chatEvent.isCancelled()) {
            sendMail(chatMessage);
        }
    }

    /**
     * Constructs an {@link Audience} of party members
     *
     * @param party target party
     * @return an {@link Audience} of party members
     */
    public @NotNull Audience constructPartyAudience(@NotNull Party party) {
        return mcMMO.getAudiences().filter(party.getSamePartyPredicate());
    }

    /**
     * Styles a string using a locale entry
     *
     * @param author   message author
     * @param message  message contents
     * @param canColor whether to replace colors codes with colors in the raw message
     * @return the styled string, based on a locale entry
     */
    public @NotNull TextComponent addStyle(@NotNull Author author, @NotNull String message, boolean canColor, boolean isLeader) {
        String name = author.getAuthoredName(ChatChannel.PARTY);
        return Component.text("(Party)", NamedTextColor.GREEN)
                .append(Component.text(" " + name)
                        .color(isLeader ? NamedTextColor.GOLD : NamedTextColor.YELLOW))
                .append(Component.text(": ")
                        .color(NamedTextColor.WHITE)
                        .append(Component.text(message).color(TextColor.color(HSVLike.fromRGB(225, 245, 254)))));
    }

    @Override
    public void sendMail(@NotNull ChatMessage chatMessage) {
        chatMessage.sendMessage();
    }
}
