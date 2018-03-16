package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import util.STATIC;

import java.awt.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class cmdMsg implements CommandPrivate, Serializable {


    private HashMap<User, PrivateChannel> privatechans = new HashMap<>();

    public HashMap<User, PrivateChannel> getPrivatechans() {
        return privatechans;
    }



    @Override
    public boolean calledPrivate(String[] args, PrivateMessageReceivedEvent event) {
        return false;
    }

    @Override
    public void actionPrivate(String[] args, PrivateMessageReceivedEvent event) {

    }

    @Override
    public void executedPrivate(boolean success, PrivateMessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
