package listeners;

import core.commandHandlerPrivate;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.channel.priv.PrivateChannelCreateEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.STATIC;

import java.awt.*;

public class privateListener extends ListenerAdapter{

    public void onPrivateChannelCreate(PrivateChannelCreateEvent event) {

        if(!event.getPrivateChannel().hasLatestMessage()) {
            event.getPrivateChannel().sendMessage(
                    new EmbedBuilder().setColor(Color.GREEN).setDescription("Hallo " + event.getUser().getName() + "! Willkommen beim siriBot! Übersicht der Commands gibt es mit *" + STATIC.PREFIX + "help'.").build()
            ).queue();
        }

    }

    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

        if(event.getMessage().getContentRaw().startsWith(STATIC.PREFIX) && event.getMessage().getAuthor().getId()!= event.getJDA().getSelfUser().getId()) {
            commandHandlerPrivate.handleCommandPrivate(commandHandlerPrivate.parser.parsePrivate(event.getMessage().getContentRaw(), event));
        }

    }

}
