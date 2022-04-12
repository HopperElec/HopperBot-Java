package uk.co.hopperelec.HopperBot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public record HopperBotUtils(JDA jda, HopperBotConfig config) {
    private static final Logger logger = LoggerFactory.getLogger(HopperBotUtils.class);
    private static HopperBotUtils instance;

    public void logToGuild(String message, Guild guild) {
        final HopperBotServerConfig guildConfig = config.getServerConfig(guild.getIdLong());
        final long channelId = guildConfig.getLogChannel();
        final TextChannel logChannel = guild.getTextChannelById(channelId);
        if (logChannel == null) {
            logger.error("The log channel for {} ({}) could not be found!", guildConfig.getName(), channelId);
        } else {
            logChannel.sendMessage(message).queue();
        }
    }

    public void log(String message, Guild guild, HopperBotFeatures hopperBotFeature) {
        message = config.getLogFormat().replaceAll("\\{message}", message).replaceAll("\\{feature}", hopperBotFeature.name());
        logger.info(message);
        if (guild == null) {
            for (Guild guildIter : jda.getGuilds()) {
                logToGuild(message, guildIter);
            }
        } else {
            logToGuild(message, guild);
        }
    }

    public Map<String, JsonNode> getFeatureConfig(Guild guild, HopperBotFeatures feature) {
        return config().getServerConfig(guild.getIdLong()).getFeatureConfig(feature);
    }

    public EmbedBuilder getEmbedBase() {
        return new EmbedBuilder().setFooter("Made by hopperelec#3060").setColor(0xe31313);
    }

    public void tempReply(Message message, String reply) {
        message.reply(reply).queue(replyMsg -> {
            replyMsg.delete().queueAfter(10, TimeUnit.SECONDS);
            message.delete().queueAfter(10, TimeUnit.SECONDS);
        });
    }

    static void createInstance(JDA jda, HopperBotConfig config) {
        if (instance == null) {
            instance = new HopperBotUtils(jda, config);
        } else {
            logger.error("Attempt made to make an instance of HopperBotUtils but one already exists");
        }
    }

    public static HopperBotUtils getInstance() {
        return instance;
    }
}
