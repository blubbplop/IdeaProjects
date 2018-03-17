package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class cmdTeam implements Command{

    public List<VoiceChannel> active = new ArrayList<>();

    private static HashMap<VoiceChannel, Guild> teamchannels = new HashMap<>();

    public static HashMap<VoiceChannel, Guild> getTeamchannels() {
        return teamchannels;
    }

    private static HashMap<User, VoiceChannel> teams = new HashMap<>();

    public static HashMap<User, VoiceChannel> getTeams() {
        return teams;
    }

    public static VoiceChannel getVchan(String id, Guild g) {
        return g.getVoiceChannelById(id);
    }

    public Member getMember(String name, Guild g) {
        return g.getMemberById(name);
    }

    public static User getUser(String name, Guild g) {
        return g.getMemberById(name).getUser();
    }

    private static Guild getGuild(String id, JDA jda) {
        return jda.getGuildById(id);
    }

    private void error(TextChannel tc, String content) {
        tc.sendMessage(new EmbedBuilder().setColor(Color.RED).setDescription(content).build()).queue();
    }

    private void message(TextChannel tc, String content) {
        tc.sendMessage(new EmbedBuilder().setColor(Color.GREEN).setDescription(content).build()).queue();
    }

    private void setTeamChan(String name, Guild g, TextChannel tc) {
        name = name.trim();

        if (name.equals(""))
            error(tc, "Bitte geben Sie einen gültigen Teamnamen ein!");
        else if (teams.containsKey(name))
            error(tc, "Dieses Team gibt es schon.");
        else {
            VoiceChannel nvc = (VoiceChannel) g.getController().createVoiceChannel(name).setName(name).complete();
            g.getController().modifyVoiceChannelPositions().selectPosition(nvc).moveTo(9).queue();
            teamchannels.put(nvc, g);
            active.add(nvc);
            save();
            message(tc, "Das Team " + name + " spielt nun um den Sieg mit.");
        }
    }

    private void unsetTeamChan(String name, Guild g, TextChannel tc) {
        if (name.equals(""))
            error(tc, "Bitte geben Sie eine korrekten Teamnamen ein!");
        else if (!teams.containsKey(name))
            error(tc, "Dieses Team gibt es gar nicht.");
        else {
            g.getVoiceChannelById(name).delete().queue();
            teamchannels.remove(g.getVoiceChannelById(name), g);
            save();
            message(tc, "Das Team " + name + " hat sich aufgelöst.");
        }
    }

    private void addTeamMember(String name, String teamname, Guild g, TextChannel tc) {
        User user = getUser(name, g);

        if (user == null)
            error(tc, "Bitte geben sie den korekkten Namen ein!");
        else if (teams.containsKey(user))
            error(tc, user.getAsMention() + " ist schon in einem Team");
        else {
            teams.put(user, getVchan(teamname, g));
            message(tc, user.getAsMention() + " ist nun im Team " + teamname + "!");
        }
    }

    private void removeTeamMember(String name, String teamname, Guild g, TextChannel tc) {
        User user = getUser(name, g);

        if (user == null)
            error(tc, "Bitte geben sie den korekkten Namen ein!");
        else if (!teams.containsKey(user))
            error(tc, user.getAsMention() + " ist in keinem Team");
        else {
            teams.remove(user, getVchan(teamname, g));
            message(tc, user.getAsMention() + " ist nun nicht mehr im Team " + teamname + "!");
        }
    }

    public static void unsetTeamChan(VoiceChannel vc) {
        teamchannels.remove(vc);
        save();
    }

    private void listTeamChans(Guild g, TextChannel tc) {
        StringBuilder sb = new StringBuilder().append("**TEAMS:\n\n**");
        teamchannels.keySet().stream()
                .filter(vc -> teamchannels.get(vc).equals(g))
                .forEach(vc -> sb.append(String.format(":white_small_square:  ´%s` *(%s)*\n", vc.getName(), vc.getId())));
        tc.sendMessage(new EmbedBuilder().setDescription(sb.toString()).build()).queue();
    }

    private void listTeamMembers(String teamname, Guild g, TextChannel tc) {
        StringBuilder sb = new StringBuilder().append("**TEAMS:\n\n**");
        teams.keySet().stream()
                .filter(vc -> teams.get(vc).equals(g))
                .forEach(vc -> sb.append(String.format(":white_small_square:  ´%s` *(%s)*\n", vc.getName(), vc.getId())));
        tc.sendMessage(new EmbedBuilder().setDescription(sb.toString()).build()).queue();
    }

    private static void save() {
        File path = new File("SERVER_SETTINGS");
        if (!path.exists( ))
            path.mkdir( );

        HashMap<String, String> out = new HashMap<>( );

        teamchannels.forEach((vc, guild) -> out.put(vc.getId( ), guild.getId( )));

        try {

            FileOutputStream fos = new FileOutputStream("SERVER_SETTINGS/teamchannels.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(out);
            oos.close();

        } catch (IOException e) {
            e.printStackTrace( );
        }

    }


    public static void load(JDA jda) {
        File file = new File("SERVER_SETTINGS/teamchannels.dat");
        if (file.exists())
            try {

                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                HashMap<String, String> out = (HashMap<String, String>) ois.readObject();
                ois.close();

                out.forEach((vid, gid) -> {
                    Guild guild = getGuild(gid, jda);
                    teamchannels.put(getVchan(vid, guild), guild);
                });

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace( );
            }
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        Guild g = event.getGuild();
        TextChannel tc = event.getTextChannel();

        if (args.length < 1) {
            error(tc, help());
            return;
        }

        if (core.permsCore.check(event) < 1)
            return;

        switch (args[0]) {

            case "list":
                listTeamChans(g, tc);
                break;

            case "listmembers":
                listTeamMembers(args[1], g, tc);
                break;

            case "set":
                if (args.length < 2)
                    error(tc, help());
                else
                    setTeamChan(args[1], g, tc);
                break;
            case "add":
                if (args.length < 3)
                    error(tc, help());
                else
                    addTeamMember(args[1], args[2], g, tc);
                break;

            case "remove":
                if (args.length < 3)
                    error(tc, help());
                else
                    removeTeamMember(args[1], args[2], g, tc);
                break;
            case "unset":
                if (args.length < 2)
                    error(tc, help());
                else
                    unsetTeamChan(args[1], g, tc);
                break;

            default:
                error(tc, help());

        }

    }


    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
