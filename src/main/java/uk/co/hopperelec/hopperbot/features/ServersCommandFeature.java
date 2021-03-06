package uk.co.hopperelec.hopperbot.features;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.co.hopperelec.hopperbot.HopperBotFeature;
import uk.co.hopperelec.hopperbot.HopperBotFeatures;
import uk.co.hopperelec.hopperbot.HopperBotGuildConfig;

public final class ServersCommandFeature extends HopperBotFeature {
    private MessageEmbed embed;

    public ServersCommandFeature(@NotNull JDABuilder builder) {
        super(builder,HopperBotFeatures.SERVER_LIST);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
        final EmbedBuilder embedBuilder = getEmbedBase();
        for (HopperBotGuildConfig guildConfig : getConfig().guilds().values()) {
            if (guildConfig.getInvite() != null) {
                embedBuilder.addField(guildConfig.name(),guildConfig.getInvite(),true);
            }
        }
        embed = embedBuilder.build();
        event.getJDA().upsertCommand("servers","Lists invites to all the public servers the bot is in").queue();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().matches("^(\\?)servers")) {
            event.getMessage().replyEmbeds(embed).queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("servers")) {
            event.replyEmbeds(embed).queue();
            logToGuild(event.getUser().getId()+" successfully used global slash command /servers",featureEnum,event.getGuild());
        }
    }
}
