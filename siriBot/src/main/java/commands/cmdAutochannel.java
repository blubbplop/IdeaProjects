package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.Color;
import java.io.*;
import java.util.HashMap;

public class cmdAutochannel implements Command, Serializable{


    private static HashMap<VoiceChannel, Guild> autochannels = new HashMap<>();

    public static HashMap<VoiceChannel, Guild> getAutochannels() {
        return autochannels;
    }

    public static VoiceChannel getVchan(String id, Guild g){
        return g.getVoiceChannelById(id);
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

    private void setChan(String id, Guild g, TextChannel tc) {
        VoiceChannel vc = getVchan(id, g);

        if (vc == null)
            error(tc, "Bitte geben Sie eine korrekte Kanal ID ein!");
        else if (autochannels.containsKey(vc))
            error(tc, "Dieser Kanal ist noch ein Autochannel.");
        else {
            autochannels.put(vc, g);
            save();
            message(tc, "Der Kanal " + vc.getName( ) + " ist nun ein Autochannel.");
        }
    }

    private void unsetChan(String id, Guild g, TextChannel tc) {
        VoiceChannel vc = getVchan(id, g);

        if (vc == null)
            error(tc, "Bitte geben Sie eine korrekte Kanal ID ein!");
        else if (!autochannels.containsKey(vc))
            error(tc, "Dieser Kanal ist noch ein Autochannel.");
        else {
            autochannels.remove(vc, g);
            save();
            message(tc, "Der Kanal " + vc.getName( ) + " ist nun kein Autochannel mehr.");
        }
    }

    public static void unsetChan(VoiceChannel vc) {
        autochannels.remove(vc);
        save();
    }

    private void listChans(Guild g,TextChannel tc) {
        StringBuilder sb = new StringBuilder().append("**AUTOCHANNELS:\n\n**");
        autochannels.keySet().stream()
                .filter(vc -> autochannels.get(vc).equals(g))
                .forEach(vc -> sb.append(String.format(":white_small_square:  ´%s` *(%s)*\n", vc.getName(), vc.getId())));
        tc.sendMessage(new EmbedBuilder().setDescription(sb.toString()).build()).queue();
    }

    private static void save() {

        File path = new File("SERVER_SETTINGS");
        if (!path.exists( ))
            path.mkdir( );

        HashMap<String, String> out = new HashMap<>( );

        autochannels.forEach((vc, guild) -> out.put(vc.getId( ), guild.getId( )));

        try {

            FileOutputStream fos = new FileOutputStream("SERVER_SETTINGS/autochannels.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(out);
            oos.close();

        } catch (IOException e) {
            e.printStackTrace( );
        }
    }

    public static void load(JDA jda) {
        File file = new File("SERVER_SETTINGS/autochannels.dat");
        if (file.exists())
            try {

                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                HashMap<String, String> out = (HashMap<String, String>) ois.readObject();
                ois.close();

                out.forEach((vid, gid) -> {
                    Guild guild = getGuild(gid, jda);
                    autochannels.put(getVchan(vid, guild), guild);
                });

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

        if (core.permsCore.check(event) < 2)
            return;

        switch (args[0]) {

            case "list":
                listChans(g, tc);
                break;

            case "set":
            case "add":
                if (args.length < 2)
                    error(tc, help());
                else
                    setChan(args[1], g, tc);
                break;

            case "remove":
            case "unset":
                if (args.length < 2)
                    error(tc, help());
                else
                    unsetChan(args[1], g, tc);
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
        return "Hilfe zu Befehl'" + STATIC.PREFIX + "autochan':\n  - autochan add  (Sprachkanal als Autochannel setzen)\n  - autochan remove (Autochannel löschen)\n  - autochan list  (Autochannel auflisten)";
    }
}