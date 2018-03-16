package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class cmdFun implements Command {

    public static void message(String msg, MessageReceivedEvent event) {

        event.getTextChannel().sendMessage(
                new EmbedBuilder().setColor(Color.PINK).setDescription(msg).build()
        ).queue();

    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        switch (args[0]) {

            case "penis":
                message(":cherries: :eggplant:", event);
                break;
            case "arsch":
                message(":peach:", event);
                break;
            case "<3":
                message(":heart:", event);
                break;
            case "jude":
                message(":star_of_david:", event);
                break;
            case "deutschland":
                message(":flag_de:", event);
                break;
            case "chickendinner":
                message(":chicken:", event);
                break;
            case "haha":
                message(":joy:", event);
                break;
            case "shrug":
                message("¯\\_(ツ)_/¯", event);
                break;

        }


    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
