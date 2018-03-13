package commands;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.util.Objects;


public class cmdMsg implements CommandPrivate{



    @Override
    public boolean calledPrivate(String[] args, PrivateMessageReceivedEvent event) {
        return false;
    }

    @Override
    public void actionPrivate(String[] args, PrivateMessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;

        try {
            String msg = "";
            for (int i = 1; i<=(args.length-1); i++) {
                msg = msg + " " + args[i];
            }
            msg.trim();




        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void executedPrivate(boolean success, PrivateMessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }

    private void send(String[] args, String msg, PrivateMessageReceivedEvent event) {
        event.getJDA().getPrivateChannels().

    }
}
