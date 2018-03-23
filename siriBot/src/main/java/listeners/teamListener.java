package listeners;

import commands.cmdTeam;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class teamListener extends ListenerAdapter{

    List<VoiceChannel> active = new ArrayList<>();

    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        HashMap<User, VoiceChannel> teammembers = commands.cmdTeam.getTeammembers();
        Guild g = event.getGuild();
        User user = event.getMember().getUser();

        if (teammembers.containsKey(user) && event.getChannelJoined() != teammembers.get(user))
            g.getController().moveVoiceMember(event.getMember(), teammembers.get(user));
            event.getGuild().getTextChannels().get(1).sendMessage(
                    new EmbedBuilder().setColor(Color.RED).setDescription("Solange du ein einem Team bist musst du in deinem Teamchannel bleiben!").build()
            ).queue();
    }

    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        HashMap<VoiceChannel, Guild> teamchannels = commands.cmdTeam.getTeamchannels();
        HashMap<User, VoiceChannel> teammembers = commands.cmdTeam.getTeammembers();
        Guild g = event.getGuild();
        User user = event.getMember().getUser();

        if (teammembers.containsKey(user) && event.getChannelJoined() != teammembers.get(user))
            g.getController().moveVoiceMember(event.getMember(), teammembers.get(user));
            event.getGuild().getTextChannels().get(1).sendMessage(
                new EmbedBuilder().setColor(Color.RED).setDescription("Solange du ein einem Team bist musst du in deinem Teamchannel bleiben!").build()
            ).queue();

        VoiceChannel vc = event.getChannelLeft();

        if (event.getChannelLeft().getMembers().size() == 0 && teamchannels.containsKey(event.getChannelLeft())) {
            vc.delete().queue();
        }
    }

    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        HashMap<VoiceChannel, Guild> teamchannels = commands.cmdTeam.getTeamchannels();
        VoiceChannel vc = event.getChannelLeft();

        if (event.getChannelLeft().getMembers().size() == 0 && teamchannels.containsKey(event.getChannelLeft())) {
            vc.delete().queue();
        }

    }

    public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
        HashMap<VoiceChannel, Guild> teamchans = commands.cmdTeam.getTeamchannels();
        Guild g = event.getGuild();

        if (teamchans.containsKey(event.getChannel())) {
            VoiceChannel nvc = (VoiceChannel) g.getController().createVoiceChannel(event.getChannel().getName()).setName(event.getChannel().getName() + "  [TEAM " + (teamchans.size()+1) + "]").setParent(g.getCategoryById(cmdTeam.categoryID)).complete();
        }
    }

}
