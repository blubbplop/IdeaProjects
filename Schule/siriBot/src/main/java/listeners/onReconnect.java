package listeners;

import core.Main;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class onReconnect extends ListenerAdapter{

    Main main = new Main();

    public Main getMain() {
        return main;
    }

}
