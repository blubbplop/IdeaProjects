package commands;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public interface CommandPrivate {

    boolean calledPrivate(String[] args, PrivateMessageReceivedEvent event);
    void actionPrivate(String[] args, PrivateMessageReceivedEvent event);
    void executedPrivate(boolean success, PrivateMessageReceivedEvent event);
    String help();

}
