package listeners;

import net.dv8tion.jda.core.events.guild.update.GuildUpdateAfkTimeoutEvent;
import net.dv8tion.jda.core.events.guild.voice.*;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class voiceListener extends ListenerAdapter{

    SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {

        event.getGuild().getTextChannelsByName("voicelog", true).get(0).sendMessage(
                "[" + time.format(new Date()) + "] " + event.getVoiceState().getMember().getUser().getAsMention() + " ist dem Sprach-Kanal " + event.getChannelJoined().getName() + " beigetreten!"
        ).queue();

    }

    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {

        event.getGuild().getTextChannelsByName("voicelog", true).get(0).sendMessage(
                "[" + time.format(new Date()) + "] " + event.getVoiceState().getMember().getUser().getAsMention() + " ist von  " + event.getChannelLeft().getName() + " nach " + event.getChannelJoined().getName() + " gegangen!"
        ).queue();

    }

    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {

        event.getGuild().getTextChannelsByName("voicelog", true).get(0).sendMessage(
                "[" + time.format(new Date()) + "] " + event.getVoiceState().getMember().getUser().getAsMention() + " hat den Sprach-Kanal " + event.getChannelLeft().getName() + " verlassen!"
        ).queue();

    }

    public void onGuildVoiceGuildMute(GuildVoiceGuildMuteEvent event) {
        if (event.isGuildMuted()) {
            event.getGuild().getTextChannelsByName("voicelog", true).get(0).sendMessage(
                    "[" + time.format(new Date()) + "] " + event.getVoiceState().getMember().getUser().getAsMention() + " kann nicht mehr reden!"
            ).queue();
        }

    }

    public void onGuildVoiceGuildDeafen(GuildVoiceGuildDeafenEvent event) {
        if (event.isGuildDeafened()) {
            event.getGuild().getTextChannelsByName("voicelog", true).get(0).sendMessage(
                    "[" + time.format(new Date()) + "] " + event.getVoiceState().getMember().getUser().getAsMention() + " kann nichts mehr hören!"
            ).queue();
        }

    }

    public void onGuildUpdateAfkTimeout(GuildUpdateAfkTimeoutEvent event) {
        event.getGuild().getTextChannelsByName("voicelog", true).get(0).sendMessage(
                "[" + time.format(new Date()) + "] " + event.getOldAfkTimeout().name() + " ist wohl AFK."
        ).queue();

    }


}
