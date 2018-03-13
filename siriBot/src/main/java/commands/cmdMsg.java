package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import util.STATIC;

import java.awt.*;

import java.util.ArrayList;
import java.util.List;


public class cmdMsg implements CommandPrivate{

    private static List<PrivateChannel> privatechannels = new ArrayList<>();

    private static List<PrivateChannel> getPrivatechannels() {
        return privatechannels;
    }

    private static PrivateChannel getPchan(String id, PrivateMessageReceivedEvent event) {
        return event.getJDA().getPrivateChannelById(id);
    }

    private void error(PrivateChannel pc, String content) {
        pc.sendMessage(new EmbedBuilder().setColor(Color.RED).setDescription(content).build()).queue();
    }

    private void message(PrivateChannel pc, String content) {
        pc.sendMessage(new EmbedBuilder().setColor(Color.GREEN).setDescription(content).build()).queue();
    }

    private void setPchan(String id, PrivateMessageReceivedEvent event, PrivateChannel pc) {

        PrivateChannel channel = getPchan(id, event);

        if (channel == null)
            error(pc, "Bitte geben sie eine korrekte ID ein!");
        else if (!privatechannels.contains(channel))
            getPchan(id, event).getUser().openPrivateChannel().complete();
        else
            message(pc, "Dieser Discord Nutzer hat mich nicht als Freund eingespeichert. Sie können ihm solage keine Direktnachricht senden.");

    }

    public static String addMessages(String[] array) {
        String message = "";
        for (int i = 1; i <= array.length-1; i++) {
            message += array[i] + " ";
        }
        return message;
    }

    @Override
    public boolean calledPrivate(String[] args, PrivateMessageReceivedEvent event) {
        return false;
    }

    @Override
    public void actionPrivate(String[] args, PrivateMessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;

        try {

            if (args.length < 2) {
                System.out.println("Fehler bei Command msg");
                return;
            }

            event.getJDA().getUserById(args[0]).openPrivateChannel().queue((channel) -> {
                channel.sendMessage(
                        new EmbedBuilder().setColor(Color.BLUE).setDescription(" :calling: Neue Nachricht von " + event.getAuthor().getName() + ": " + addMessages(args)).build()
                ).queue();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void executedPrivate(boolean success, PrivateMessageReceivedEvent event) {

        System.out.println("[INFO] Command '" + STATIC.PREFIX + "msg' wurde ausgeführt!");

    }

    @Override
    public String help() {
        return null;
    }

}
