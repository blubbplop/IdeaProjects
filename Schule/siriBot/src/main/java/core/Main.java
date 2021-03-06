package core;

import commands.cmdClear;
import commands.cmdMsg;
import commands.cmdPing;
import commands.cmdSay;
import listeners.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import util.SECRETS;
import util.STATIC;

import javax.security.auth.login.LoginException;

public class Main {

    public static JDABuilder builder;

    public static void main(String[] args) {

        builder = new JDABuilder(AccountType.BOT);

        builder.setToken(SECRETS.TOKEN);
        builder.setAutoReconnect(true);

        builder.setStatus(OnlineStatus.ONLINE);

        builder.setGame(Game.of(GameType.DEFAULT, "mit dicken Titten. | v. " + STATIC.VERSION));


        addListeners();
        addCommands();

        try {
            JDA jda = builder.buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public static void addCommands() {

        commandHandler.commands.put("ping", new cmdPing());
        commandHandler.commands.put("say", new cmdSay());
        commandHandler.commands.put("clear", new cmdClear());
        commandHandlerPrivate.commands.put("msg", new cmdMsg());

    }

    public static void addListeners() {
        builder.addEventListener(new readyListener());
        builder.addEventListener(new voiceListener());
        builder.addEventListener(new onReconnect());
        builder.addEventListener(new guildListener());
        builder.addEventListener(new messageListener());
    }

}
