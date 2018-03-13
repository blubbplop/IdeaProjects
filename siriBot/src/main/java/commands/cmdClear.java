package commands;

import core.permsCore;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class cmdClear implements Command {

    EmbedBuilder error = new EmbedBuilder().setColor(Color.RED);


    private int getAmount(String string) {

        try {
            if (string.equals("all"))
                return 0;
            else
                return Integer.parseInt(string);

        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        if (permsCore.check(event) == 0) {
            return;
        }


        if (args.length == 0) {

            event.getTextChannel( ).sendMessage(error.setDescription(help( )).build( )).queue( );
            return;

        } else  if (getAmount(args[0]) == 1) {

            try {
                event.getMessage().delete().queue();
                event.getMessage().delete().queue();

                Message msg = event.getTextChannel().sendMessage(
                        new EmbedBuilder().setColor(Color.GREEN).setDescription("Eine Nachricht gelöscht!").build()
                ).complete();

                new Timer().schedule(new TimerTask( ) {
                    @Override
                    public void run() {
                        msg.delete().queue();
                    }
                }, 3000);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if ((getAmount(args[0]) > 1) && (getAmount(args[0]) <= 100)) {

            try {
                MessageHistory history = new MessageHistory(event.getTextChannel());
                List<Message> msgs;

                event.getMessage().delete().queue();

                msgs = history.retrievePast(getAmount(args[0])).complete();
                event.getTextChannel().deleteMessages(msgs).queue();

                Message msg = event.getTextChannel().sendMessage(
                        new EmbedBuilder().setColor(Color.GREEN).setDescription(args[0] + " Nachrichten gelöscht!").build()
                ).complete();

                new Timer().schedule(new TimerTask( ) {
                    @Override
                    public void run() {
                        msg.delete().queue();
                    }
                }, 3000);


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            event.getTextChannel().sendMessage(
                    error.setDescription("Bitte geben sie eine ganzzahlige Anzahl zwischen 2 und 100 ein!").build()
            ).queue();

        }



    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "Hilfe für Befehl '" + STATIC.PREFIX + "clear':\n  - clear <ANZAHL>  (löscht eine bestimmte Anzahl an Nachrichten)";
    }
}
