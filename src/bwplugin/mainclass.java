package bwplugin;

import org.bukkit.plugin.java.JavaPlugin;

import bwplugin.commands.tgttos;
import bwplugin.events.playerjoin;

@SuppressWarnings("unused")
public class mainclass extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new playerjoin(), this);
        this.getCommand("tgttos").setExecutor(new tgttos());
    }
}
