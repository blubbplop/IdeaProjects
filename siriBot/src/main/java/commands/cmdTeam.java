package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class cmdTeam implements Command{

    public static String categoryID = "423573865906700298";

    public List<String> teamnames = new ArrayList<>();

    private static HashMap<VoiceChannel, Guild> teamchannels = new HashMap<>();

    public static HashMap<VoiceChannel, Guild> getTeamchannels() {
        return teamchannels;
    }

    private static HashMap<VoiceChannel, User> channelusers = new HashMap<>();

    public static HashMap<VoiceChannel, User> getChannelusers() {
        return channelusers;
    }

    private static HashMap<User, VoiceChannel> teammembers = new HashMap<>();

    public static HashMap<User, VoiceChannel> getTeammembers() {
        return teammembers;
    }

    private static HashMap<String, VoiceChannel> teams = new HashMap<>();

    public static HashMap<String, VoiceChannel> getTeamnames() {
        return teams;
    }

    private static HashMap<String, String> teamids = new HashMap<>();

    public static HashMap<String, String> getTeamids() {
        return teamids;
    }

    public static VoiceChannel getVChanByName(String name, Guild g) {
        String id = teamids.get(name);
        return g.getVoiceChannelById(id);
    }

    public Member getMemberByName(String name, Guild g) {
        return g.getMemberById(name);
    }

    public static User getUser(String name, MessageReceivedEvent event) {
        return event.getAuthor();
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

    public void unsetTeam(VoiceChannel vc) {
        teamchannels.remove(vc);
    }

    private void setTeamChan(String name, Guild g, TextChannel tc) {
        name = name.trim();
        if (name.equals(""))
            error(tc, "Bitte geben Sie einen gültigen Teamnamen ein!");
        else if (teamnames.contains(name))
            error(tc, "Dieses Team gibt es schon.");
        else {
            VoiceChannel nvc = (VoiceChannel) g.getController().createVoiceChannel(name).setName(name + "  [TEAM " + (teamchannels.size()+1) + "]").setParent(g.getCategoryById(categoryID)).complete();
            teamchannels.put(nvc, g);
            teamnames.add(name);
            teams.put(name, nvc);
            teamids.put(name, nvc.getId());
            message(tc, "Das Team " + name + " spielt nun um den Sieg mit.");
        }
    }

    private void unsetTeamChan(String name, Guild g, TextChannel tc) {
        if (name.equals(""))
            error(tc, "Bitte geben Sie eine korrekten Teamnamen ein!");
        else if (!teamids.containsKey(name) || !teamnames.contains(name) || !teams.containsKey(name))
            error(tc, "Dieses Team gibt es gar nicht.");
        else {
            teams.remove(name);
            teamnames.remove(name);
            teamids.remove(name);
            teamchannels.remove(getVChanByName(name, g));
            message(tc, "Das Team '" + name + "' hat sich aufgelöst. Alle Mitglieder wurden entfernt.");
        }
    }

    private void addTeamMember(User user, String teamname, Guild g, TextChannel tc) {
        if (user == null)
            error(tc, "Bitte geben sie den korrekten Namen ein!");
        else if (teammembers.containsKey(user))
            error(tc, user.getAsMention() + " ist schon in einem Team");
        else {
            teammembers.put(user, getVChanByName(teamname, g));
            channelusers.put(getVChanByName(teamname, g), user);
            message(tc, user.getAsMention() + " ist nun im Team " + teamname + "!");
        }
    }

    private void listTeamChans(Guild g, TextChannel tc) {
        StringBuilder sb = new StringBuilder().append("**TEAMS:\n\n**");
        teamchannels.keySet().stream()
                .filter(vc -> teamchannels.get(vc).equals(g))
                .forEach(vc -> sb.append(String.format(":white_small_square:  ´%s` *(%s)*\n", vc.getName(), vc.getId())));
        tc.sendMessage(new EmbedBuilder().setDescription(sb.toString()).build()).queue();
    }

    private void removeTeamMember(User user, String teamname, Guild g, TextChannel tc) {
        if (user == null)
            error(tc, "Bitte geben sie den korekkten Namen ein!");
        else if (!teammembers.containsValue(user))
            error(tc, user.getAsMention() + " ist in keinem Team");
        else {
            teammembers.remove(user);
            channelusers.remove(getVChanByName(teamname, g), user);
            message(tc, user.getAsMention() + " ist nun nicht mehr im Team " + teamname + "!");
        }
    }

    private void setCategory(String id, MessageReceivedEvent event) {
        if (core.permsCore.check(event) < 2)
            return;
        categoryID = id;
    }

    private void save (Guild guild) throws IOException {

        if (!teamchannels.containsValue(guild))
            return;

        File path = new File("SERVER_SETTINGS/" + guild.getId());
        if (!path.exists())
            path.mkdir();

        HashMap<String, String> out1 = new HashMap<>( );
        String saveTeamchannels = "SERVER_SETTINGS/" + guild.getId() + "/teamchannels.dat";
        teamchannels.forEach((vc, g) -> out1.put(vc.getId( ), guild.getId( )));

        FileOutputStream fos1 = new FileOutputStream(saveTeamchannels);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(out1);
        oos1.close();

        HashMap<String, String> out2 = new HashMap<>( );
        String saveChannelusers = "SERVER_SETTINGS/" + guild.getId() + "/channelusers.dat";
        channelusers.forEach((vc, user) -> out2.put(vc.getId(), user.getId()));

        FileOutputStream fos2 = new FileOutputStream(saveTeamchannels);
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(out2);
        oos2.close();

        HashMap<String, String> out3 = new HashMap<>( );
        String saveTeammembers = "SERVER_SETTINGS/" + guild.getId() + "/teammembers.dat";
        teammembers.forEach((user, vc) -> out3.put(user.getId(), vc.getId()));

        FileOutputStream fos3 = new FileOutputStream(saveTeamchannels);
        ObjectOutputStream oos3 = new ObjectOutputStream(fos3);
        oos3.writeObject(out3);
        oos3.close();

        HashMap<String, String> out4 = new HashMap<>( );
        String saveTeams = "SERVER_SETTINGS/" + guild.getId() + "/teams.dat";
        teams.forEach((name, vc) -> out4.put(name, vc.getId()));

        FileOutputStream fos4 = new FileOutputStream(saveTeamchannels);
        ObjectOutputStream oos4 = new ObjectOutputStream(fos4);
        oos4.writeObject(out4);
        oos4.close();

        String saveCategory = "SERVER_SETTINGS/" + guild.getId() + "/category.dat";
        FileOutputStream fos5 = new FileOutputStream(saveCategory);
        ObjectOutputStream oos5 = new ObjectOutputStream(fos4);
        oos5.writeObject(categoryID);
        oos5.close();
    }


    public static void load(Guild g) {

        File file = new File("SERVER_SETTINGS/" + g.getId() + "/category.dat");
        if (file.exists())
            try {

                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                String id = (String) ois.readObject();
                ois.close();

                categoryID = id;

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
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

        if (categoryID.equals("")) {
            error(event.getTextChannel( ), "Sie müssen auf diesem Server als erstes eine Kategorie für die Teams festlegen. Das machen sie mit '" + STATIC.PREFIX + "team setcategory <ID>'!");
            return;
        }

        switch (args[0]) {

            case "setcategory":
                setCategory(args[1], event);
                break;
            case "list":
                listTeamChans(g, tc);
                break;
            case "add":
            case "set":
                if (args.length < 2)
                    error(tc, help());
                else
                    setTeamChan(args[1], g, tc);
                break;
            case "setmember":
            case "addmember":
                if (args.length < 2)
                    error(tc, help());
                else
                    addTeamMember(event.getAuthor(), args[1], g, tc);
                break;
            case "unsetmember":
            case "removemember":
                if (args.length < 2)
                    error(tc, help());
                else
                    removeTeamMember(event.getAuthor(), args[1], g, tc);
                break;
            case "remove":
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
        return "Hilfe zum Befehl '" + STATIC.PREFIX + "team':\n  - set <NAME>   (Erstellt ein Team)\n  - add <TEAMNUMMER>   (Fügt dich zu einem Team hinzu)\n  - remove <TEAMNUMMER>   (Löscht dich aus einem Team)\n  - unset <NAME>   (Löscht ein Team)";
    }
}
