package me.alvsch.reporting.commands;

import me.alvsch.reporting.Inventories.InventoryHandler;
import me.alvsch.reporting.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModerateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(Utils.color("You can't use this command as console"));
            return true;
        }
        Player player = (Player) sender;

        if(!(args.length >= 1)) {
            player.sendMessage(Utils.color("&bdo /mod <player>"));
            return true;
        }

        OfflinePlayer offlinePlayer = Bukkit.getPlayer(args[0]);
        if(offlinePlayer == null) {
            player.sendMessage(Utils.color("&cThat Player have not played here before!"));
            return true;
        }
        if (offlinePlayer.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(Utils.color("&cYou can't moderate yourself"));
            return true;
        }

        InventoryHandler.punishMenu(player, offlinePlayer);

        return true;
    }
}
