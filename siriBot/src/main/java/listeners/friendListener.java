package listeners;

import net.dv8tion.jda.client.events.relationship.FriendRequestReceivedEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.STATIC;

import java.awt.*;

public class friendListener extends ListenerAdapter{

    public void onFriendRequestReceived(FriendRequestReceivedEvent event) {

        event.getFriendRequest().accept();

        event.getUser().openPrivateChannel().complete().sendMessage(
                new EmbedBuilder().setColor(Color.GREEN).setDescription("Hallo " + event.getUser().toString() + "! Willkommen beim siriBot! Übersicht der Commands gibt es mit *" + STATIC.PREFIX + "help'.").build()
        ).queue();

    }

}
