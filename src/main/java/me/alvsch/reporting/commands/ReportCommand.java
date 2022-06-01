package me.alvsch.reporting.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.alvsch.reporting.Inventories.InventoryHandler;
import me.alvsch.reporting.Main;
import me.alvsch.reporting.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ReportCommand implements CommandExecutor {

    Main plugin = Main.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(Utils.color("Just ban them, you're the console"));
            return true;
        }
        Player player = (Player) sender;

        if(!(args.length >= 1)) {
            player.sendMessage(Utils.color("&bdo /report <player> [reason]"));
            return true;
        }

        OfflinePlayer offlinePlayer = Bukkit.getPlayer(args[0]);
        if(offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
            player.sendMessage(Utils.color("That player has never joined before"));
            return true;
        }
        if(args.length == 1) {
            InventoryHandler.reportMenu(player, offlinePlayer);
            return true;
        }

        StringBuilder reason = new StringBuilder();
        for(int i = 1; i < args.length; i++) {
            reason.append(args[i]);
            reason.append(" ");
        }

        JsonParser parser = new JsonParser();
        JsonObject object = new JsonObject();
        object.add("name", parser.parse(offlinePlayer.getName()));
        object.add("reason", parser.parse(reason.toString()));
        object.add("reporter", parser.parse(player.getName()));

        JsonObject jsonObject = plugin.data.get("reports").getAsJsonObject();
        //TODO: fix issue with one player only able to save report of one person at a time
        jsonObject.add(player.getUniqueId().toString(), object);

        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!(p.hasPermission("reporting.viewreports"))) {
                continue;
            }
            p.sendMessage(Utils.color("&e&l&n-- PLAYER REPORTED --"));
            p.sendMessage(Utils.color("&ePlayer: " + offlinePlayer.getName()));
            p.sendMessage(Utils.color("&eReason: " + reason));
            p.sendMessage(Utils.color("&eOnline: &a" + offlinePlayer.isOnline()));
            p.sendMessage(Utils.color("&eReported By: " + player.getName()));
            p.sendMessage(Utils.color("&e&l&n-- REPORT END --"));

        }

        player.sendMessage(Utils.color("&aThank you for your report a team member will take care of it shortly."));

        return true;
    }
}
