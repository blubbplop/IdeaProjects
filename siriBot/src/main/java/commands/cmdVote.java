package commands;

import core.permsCore;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class cmdVote implements Command, Serializable{


    private static TextChannel channel;

    private static HashMap<Guild, Poll> polls = new HashMap<>();

    private static final String[] EMOTI = {":one:", ":two:", ":three:", ":four:", ":five:", ":six:", ":seven:", ":eight:", ":nine:", ":keycap_ten:"};

    private class Poll implements Serializable {

        private String creator;
        private String heading;
        private List<String> answers;
        private HashMap<String, Integer> votes;

        private Poll(Member creator, String heading, List<String> answers) {

            this.creator = creator.getUser().getId();
            this.heading = heading;
            this.answers = answers;
            this.votes = new HashMap<>();

        }

        private Member getCreator(Guild guild) {
            return guild.getMember(guild.getJDA().getUserById(creator));
        }

    }

    private static void message(String content) {
        EmbedBuilder eb = new EmbedBuilder().setDescription(content);
        channel.sendMessage(eb.build()).queue();
    }

    private static void message(String content, Color color) {
        EmbedBuilder eb = new EmbedBuilder().setColor(color).setDescription(content);
        channel.sendMessage(eb.build()).queue();
    }

    private EmbedBuilder getParsedPoll(Poll poll, Guild guild) {

        StringBuilder ansSTR = new StringBuilder();
        final AtomicInteger count = new AtomicInteger();

        poll.answers.forEach(s -> {
            long votescount = poll.votes.keySet().stream().filter(k -> poll.votes.get(k).equals(count.get() + 1)).count();
            ansSTR.append(EMOTI[count.get()] + "  -  " + s + "  -  Votes: `" + votescount + "` \n");
            count.addAndGet(1);
        });

        return new EmbedBuilder()
                .setAuthor(poll.getCreator(guild).getEffectiveName() + "'s Abstimmung.", null, poll.getCreator(guild).getUser().getAvatarUrl())
                .setDescription(":pencil:    " + poll.heading + "\n\n" + ansSTR.toString())
                .setFooter("Gebe '" + STATIC.PREFIX + "vote v <nummer>' um abzustimmen ein!", null)
                .setColor(Color.CYAN);

    }

    private void createPoll(String[] args, MessageReceivedEvent event) {

        if(polls.containsKey(event.getGuild())) {

            message("Es gibt schon eine Abstimmung auf diesem Server!", Color.RED);
            return;
        }

        String argsSTRG = String.join(" ", new ArrayList<>(Arrays.asList(args).subList(1,args.length)));
        List<String> content = Arrays.asList(argsSTRG.split("\\|"));
        String heading = content.get(0);
        List<String> answers = new ArrayList<>(content.subList(1, content.size()));

        Poll poll = new Poll(event.getMember() , heading, answers);
        polls.put(event.getGuild(), poll);

        channel.sendMessage(getParsedPoll(poll, event.getGuild()).build()).queue();

    }

    private void votePoll(String[] args, MessageReceivedEvent event) {

        if(!polls.containsKey(event.getGuild())) {

            message("Es wurde keine Abstimmung zum abstimmen erstellt!", Color.RED);
            return;
        }

        Poll poll = polls.get(event.getGuild());

        int vote;

        try {
            vote = Integer.parseInt(args[1]);
            if (vote > poll.answers.size())
                throw new Exception();
        } catch (Exception e) {
            message("Bitte geben Sie eine gültige Zahl zum Antworten ein!", Color.RED);
            return;
        }

        if (poll.votes.containsKey(event.getAuthor().getId())) {
            message("Du kannst nur **einmal** für eine Abstimmung abstimmen!", Color.RED);
            return;
        }

        poll.votes.put(event.getAuthor().getId(), vote);
        polls.replace(event.getGuild(), poll);
        event.getMessage().delete().queue();

    }

    private void voteStats(MessageReceivedEvent event) {

        if(!polls.containsKey(event.getGuild())) {

            message("Es wurde keine Abstimmung erstellt!", Color.RED);
            return;
        }

        channel.sendMessage(getParsedPoll(polls.get(event.getGuild()), event.getGuild()).build()).queue();

    }

    private void closeVote(MessageReceivedEvent event) {

        if(!polls.containsKey(event.getGuild())) {

            message("Es wurde keine Abstimmung zum schließen erstellt!", Color.RED);
            return;
        }

        Guild g = event.getGuild();
        Poll poll = polls.get(g);

        if (!poll.getCreator(g).equals(event.getMember()) || permsCore.check(event) == 0 || permsCore.check(event) == 1) {
            message("Nur der Ersteller der Abstimmung (" + poll.getCreator(g).getAsMention() + ") oder eine befugte Person kann die Abstimmung schließen.", Color.RED);
            return;
        }

        polls.remove(g);
        channel.sendMessage(getParsedPoll(poll, g).build()).queue();
        message("Abstimmung von " + event.getAuthor().getAsMention() + " geschlossen.", new Color(0xFF7000));

    }

    private void savePoll (Guild guild) throws IOException {

        if (!polls.containsKey(guild))
            return;

        String saveFile = "SERVER_SETTINGS/" + guild.getId() + "/vote.dat";
        Poll poll = polls.get(guild);

        FileOutputStream fos = new FileOutputStream(saveFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(poll);

        oos.close();

    }

    private static Poll getPoll(Guild guild) throws IOException, ClassNotFoundException {

        if (polls.containsKey(guild))
            return null;

        String saveFile = "SERVER_SETTINGS/" + guild.getId() + "/vote.dat";

        FileInputStream fis = new FileInputStream(saveFile);
        ObjectInputStream ois = new ObjectInputStream(fis);

        Poll out = (Poll) ois.readObject();

        ois.close();
        return out;

    }

    public static void loadPolls(JDA jda) {

        jda.getGuilds().forEach(g -> {
            File f = new File("SERVER_SETTINGS/" + g.getId() + "/vote.dat");
            if (f.exists()) {
                try {
                    polls.put(g, getPoll(g));
                } catch (IOException e) {
                    e.printStackTrace( );
                } catch (ClassNotFoundException e) {
                    e.printStackTrace( );
                }
            }
        });

    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        channel = event.getTextChannel();

        if (args.length < 0) {
            message(help(), Color.RED);
            return;
        }

        switch (args[0]) {

            case "create":
                createPoll(args, event);
                break;

            case "v":
                votePoll(args, event);
                break;

            case "stats":
                voteStats(event);
                break;

            case "close":
                closeVote(event);
                break;
            default:
                help();
                return;

        }

        polls.forEach((g, poll) -> {
            File path = new File("SERVER_SETTINGS/" + g.getId() + "/");
            if (!path.exists())
                path.mkdirs();

            try {
                savePoll(g);
            } catch (IOException e) {
                e.printStackTrace( );
            }
        });

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "Hilfe zu Befehl'" + STATIC.PREFIX + "vote':\n  - vote create <TITEL>|<ANTWORT 1>|<ANTWORT 2>| ...  (Abstimmung erstellen)\n  - vote v <NUMMER>  (Antwort abgeben)\n  - vote stats  (Abstimmungs Statistiken)\n  - vote close  (Abstimmung schließen)";
    }
}
