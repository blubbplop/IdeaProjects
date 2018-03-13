package listeners;

import core.commandHandlerPrivate;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import core.commandHandler;

public class messageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getMessage().getContentRaw().startsWith("-") && event.getMessage().getAuthor().getId() != event.getJDA().getSelfUser().getId()) {
            commandHandler.handleCommand(commandHandler.parser.parse(event.getMessage().getContentRaw(), event));
        }

    }


    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

        if(event.getMessage().getContentRaw().startsWith("-") && event.getMessage().getAuthor().getId()!= event.getJDA().getSelfUser().getId()) {
            commandHandlerPrivate.handleCommandPrivate(commandHandlerPrivate.parser.parsePrivate(event.getMessage().getContentRaw(), event));
        }

    }

}
