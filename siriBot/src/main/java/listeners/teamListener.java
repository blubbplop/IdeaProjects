package listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class teamListener extends ListenerAdapter{

    List<VoiceChannel> active = new ArrayList<>();

    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {

        HashMap<VoiceChannel, Guild> teamchans = commands.cmdTeam.getTeamchannels();
        HashMap<User, VoiceChannel> teams = commands.cmdTeam.getTeams();
        Guild g = event.getGuild();
        User user = event.getMember().getUser();
        VoiceChannel vc = teams.get(user);

        if (teams.get(user) != event.getChannelJoined() && teams.containsKey(user)) {
            if (teamchans.containsKey(teams.get(user)))
                g.getController().moveVoiceMember(event.getMember(), vc).queue();
            else {
                System.out.println("[ERROR] Der Channel existiert nicht mehr.");
                active.remove(vc);
                teamchans.remove(vc);
                teams.remove(user);
            }
        }
    }

    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {

        HashMap<VoiceChannel, Guild> teamchans = commands.cmdTeam.getTeamchannels();
        HashMap<User, VoiceChannel> teams = commands.cmdTeam.getTeams();
        Guild g = event.getGuild();
        User user = event.getMember().getUser();
        VoiceChannel vc = teams.get(user);

        if (teams.get(user) != event.getChannelJoined() && teams.containsKey(user)) {
            if (teamchans.containsKey(teams.get(user)))
                g.getController().moveVoiceMember(event.getMember(), vc).queue();
            else {
                System.out.println("[ERROR] Der Channel existiert nicht mehr.");
                active.remove(vc);
                teamchans.remove(vc);
                teams.remove(user);
            }

        }

        vc = event.getChannelLeft();

        if (active.contains(vc) && vc.getMembers().size() == 0) {
            active.remove(vc);
            vc.delete().queue();
        }
    }

    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {

        VoiceChannel vc = event.getChannelLeft();

        if (active.contains(vc) && vc.getMembers().size() == 0) {
            active.remove(vc);
            vc.delete().queue();
        }

    }

    public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
        HashMap<VoiceChannel, Guild> teamchans = commands.cmdTeam.getTeamchannels();

        if (teamchans.containsKey(event.getChannel())) {
            commands.cmdTeam.unsetTeamChan(event.getChannel());
        }
    }

}
