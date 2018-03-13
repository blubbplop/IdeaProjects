package commands;

import core.permsCore;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class cmdSay implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        if (permsCore.check(event)) {
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
        return null;
    }

    private void send(String msg, MessageReceivedEvent event) {
        event.getTextChannel().sendMessage(msg).queue();
    }

}
