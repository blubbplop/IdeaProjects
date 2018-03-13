package listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.core.events.emote.EmoteRemovedEvent;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class guildListener extends ListenerAdapter{

    public void onGuildJoin(GuildJoinEvent event) {

        event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                new EmbedBuilder().setColor(Color.GREEN).setDescription("Herzlich Willkommen!").build()
        ).queue();

    }

    public void onGuildLeave(GuildLeaveEvent event) {

        event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                new EmbedBuilder().setColor(Color.GREEN).setDescription("Tschüssi!").build()
        ).queue();

    }

    public void onGuildBan(GuildBanEvent event) {

        event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                new EmbedBuilder().setColor(Color.RED).setDescription(event.getUser().getName() + " wurde vom Server gebannt!").build()
        ).queue();

    }

    public void onGuildUnban(GuildUnbanEvent event) {

        event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                new EmbedBuilder().setColor(Color.RED).setDescription(event.getUser().getName() + " wurde vom Server entbannt!").build()
        ).queue();

    }

    public void onEmoteAdded(EmoteAddedEvent event) {

        event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                new EmbedBuilder().setColor(Color.CYAN).setDescription("NEUES EMOTE! " + event.getEmote().toString() + " AUSRASTEEEEN!!!").build()
        ).queue();

    }

    public void onEmoteRemoved(EmoteRemovedEvent event) {

        event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                new EmbedBuilder().setColor(Color.CYAN).setDescription("Das Emote " + event.getEmote().toString() + " ist nun nicht mehr verfügbar.").build()
        ).queue();

    }

    public void onGuildUpdateName(GuildUpdateNameEvent event) {

        event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                new EmbedBuilder().setColor(Color.CYAN).setDescription("Der Diascord-Server " + event.getOldName() + " heißt nun " + event.getGuild().getName() + "!").build()
        ).queue();

    }

}
