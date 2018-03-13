package core;

import commands.CommandPrivate;

import java.util.HashMap;

public class commandHandlerPrivate {

    public static final commandParserPrivate parser = new commandParserPrivate();
    public static HashMap<String, CommandPrivate> commands = new HashMap<>();


    public static void handleCommandPrivate(commandParserPrivate.commandContainerPrivate cmd) {

        if (commands.containsKey(cmd.invoke)) {

            boolean safe = commands.get(cmd.invoke).calledPrivate(cmd.args, cmd.event);

            if (!safe) {
                commands.get(cmd.invoke).actionPrivate(cmd.args, cmd.event);
                commands.get(cmd.invoke).executedPrivate(safe, cmd.event);
            } else {
                commands.get(cmd.invoke).executedPrivate(safe, cmd.event);
            }

        }

    }

}
