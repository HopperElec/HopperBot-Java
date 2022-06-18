package uk.co.hopperelec.hopperbot.commands.command_responders;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TextCommandResponder extends CommandResponder {
    @NotNull private final Message eventMessage;

    public TextCommandResponder(@NotNull Message eventMessage) {
        this.eventMessage = eventMessage;
    }

    @Override
    public void respond(@NotNull String message) {
        eventMessage.reply(message).allowedMentions(allowedMentions).queue();
    }
    @Override
    public void respond(@NotNull MessageEmbed embed) {
        eventMessage.replyEmbeds(embed).allowedMentions(allowedMentions).queue();
    }
    @Override
    public void respond(@NotNull String message, @NotNull List<Button> buttons) {
        eventMessage.reply(message).setActionRow(buttons).allowedMentions(allowedMentions).queue();
    }
    @Override
    public void respond(@NotNull MessageEmbed embed, @NotNull List<Button> buttons) {
        eventMessage.replyEmbeds(embed).setActionRow(buttons).allowedMentions(allowedMentions).queue();
    }
}
