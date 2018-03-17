package listeners;

import net.dv8tion.jda.core.entities.Guild;
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
        VoiceChannel vc = event.getChannelJoined();
        Guild g = event.getGuild();

        if (teamchans.containsKey(vc)) {

            VoiceChannel nvc = (VoiceChannel) g.getController().createVoiceChannel(vc.getName() + " [TEMP] ")
                    .setBitrate(vc.getBitrate())
                    .setUserlimit(vc.getUserLimit())
                    .complete();

            if (vc.getParent() != null)
                nvc.getManager().setParent(vc.getParent()).queue();

            g.getController().modifyVoiceChannelPositions().selectPosition(nvc).moveTo(vc.getPosition() + 1).queue();
            g.getController().moveVoiceMember(event.getMember(), nvc).queue();
            active.add(nvc);
        }
    }

    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {

        HashMap<VoiceChannel, Guild> autochans = commands.cmdAutochannel.getAutochannels();
        Guild g = event.getGuild();
        VoiceChannel vc = event.getChannelJoined();

        if (autochans.containsKey(vc)) {

            VoiceChannel nvc = (VoiceChannel) g.getController().createVoiceChannel(vc.getName() + " [TEMP] ")
                    .setBitrate(vc.getBitrate())
                    .setUserlimit(vc.getUserLimit())
                    .complete();

            if (vc.getParent() != null)
                nvc.getManager().setParent(vc.getParent()).queue();

            g.getController().modifyVoiceChannelPositions().selectPosition(nvc).moveTo(vc.getPosition() + 1).queue();
            g.getController().moveVoiceMember(event.getMember(), nvc).queue();
            active.add(nvc);
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
        HashMap<VoiceChannel, Guild> autochans = commands.cmdAutochannel.getAutochannels();

        if (autochans.containsKey(event.getChannel())) {
            commands.cmdAutochannel.unsetChan(event.getChannel());
        }
    }

}
