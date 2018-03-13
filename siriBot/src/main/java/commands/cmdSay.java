package commands;

import core.permsCore;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.*;

public class cmdSay implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        if (permsCore.check(event) == 1) {
            return;
        }

        if (args.length == 0) {
            event.getTextChannel( ).sendMessage(
                    new EmbedBuilder( ).setColor(Color.RED).setDescription(help( )).build( )
            ).queue( );
            return;
        }

        String out = ":postal_horn: ";
        for (String s : args) {
            out += s + " ";
        }

        send(out, event);

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "Hilfe zu Befehl'" + STATIC.PREFIX + "say':\n  - say <NACHRICHT>  (Lass den Bot etwas für dich sagen... wenn du dich nicht traust.)";
    }

    private void send(String msg, MessageReceivedEvent event) {
        event.getTextChannel().sendMessage(
                new EmbedBuilder().setColor(Color.BLACK).setDescription(msg).build()
        ).queue();
    }

}
