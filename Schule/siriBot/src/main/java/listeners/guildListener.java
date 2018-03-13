package listeners;

import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class guildListener extends ListenerAdapter{

    public void onGuildJoin(GuildJoinEvent event) {

        event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                "Herzlich Willkommen!"
        ).queue();

    }

    public void onGuildLeave(GuildLeaveEvent event) {

        event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                "Tschüssi!"
        ).queue();

    }

    public void onGuildBan(GuildBanEvent event) {

        event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                event.getUser().getName() + " wurde vom Server gebannt!"
        ).queue();

    }

    public void onGuildUnban(GuildUnbanEvent event) {

        event.getGuild().getTextChannelsByName("general", true).get(0).sendMessage(
                event.getUser().getName() + " wurde vom Server entbannt!"
        ).queue();

    }


}
