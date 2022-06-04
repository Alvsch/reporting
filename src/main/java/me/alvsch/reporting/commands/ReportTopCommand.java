package me.alvsch.reporting.commands;

import me.alvsch.reporting.Inventories.InventoryHandler;
import me.alvsch.reporting.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportTopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(Utils.color("You can't use this command as console"));
            return true;
        }
        Player player = (Player) sender;

        InventoryHandler.reportTopMenu(player, 0);

        return true;
    }
}
