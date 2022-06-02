package me.alvsch.reporting.commands;

import me.alvsch.reporting.Inventories.InventoryHandler;
import me.alvsch.reporting.Main;
import me.alvsch.reporting.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClaimedReportCommand implements CommandExecutor {

    Main plugin = Main.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Utils.color("You can't use this command as console"));
            return true;
        }
        Player player = (Player) sender;
        ItemStack report = plugin.claimed_reports.get(player);
        if(report == null) {
            player.sendMessage(Utils.color("&cYou have not claimed a report yet"));
            return true;
        }

        InventoryHandler.claimReportMenu(player, report);

        return true;
    }
}
