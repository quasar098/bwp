package templatepackage.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import templatepackage.events.playerjoin;


public class tgttos implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        playerjoin.setTGTTOS(!playerjoin.checkTGTTOS());
        for (Player player : commandSender.getServer().getOnlinePlayers()) {
            if (playerjoin.checkTGTTOS()) {
                player.sendMessage(ChatColor.GREEN + "TGTTOS Enabled");
            }
            else {
                player.sendMessage(ChatColor.RED + "TGTTOS Disabled");
            }
        }
        return true;
    }
}
