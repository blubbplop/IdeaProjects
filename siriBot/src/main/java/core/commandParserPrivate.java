package core;

import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import util.STATIC;

import java.util.ArrayList;

public class commandParserPrivate {

    public commandContainerPrivate parsePrivate(String raw, PrivateMessageReceivedEvent event) {

        String beheaded = raw.replaceFirst("\\" + STATIC.PREFIX, "");
        String[] splitBeheaded = beheaded.split(" ");
        String invoke = splitBeheaded[0];
        ArrayList<String> split = new ArrayList<>();
        for (String s : splitBeheaded) {
            split.add(s);
        }
        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);

        return new commandContainerPrivate(raw, beheaded, splitBeheaded, invoke, args, event);
    }


    public class commandContainerPrivate {

        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final PrivateMessageReceivedEvent event;

        public commandContainerPrivate(String rw, String beheaded, String[] splitBeheaded, String invoke, String[] args, PrivateMessageReceivedEvent event) {
            this.raw = rw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.event = event;
        }

    }
}
