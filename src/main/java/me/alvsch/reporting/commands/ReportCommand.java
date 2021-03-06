package me.alvsch.reporting.commands;

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

public class ReportCommand implements CommandExecutor {

    Main plugin = Main.getPlugin();
    int report_id = Main.getReportId();

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

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null || !target.hasPlayedBefore()) {
            player.sendMessage(Utils.color("&cThat Player is not Online or hasn't Played here before!"));
            return true;
        }
        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(Utils.color("&cYou can't report yourself"));
            return true;
        }

        if(args.length == 1) {
            InventoryHandler.reportMenu(player, target);
            return true;
        }

        StringBuilder reason = new StringBuilder();
        reason.append("\"");
        for(int i = 1; i < args.length; i++) {
            reason.append(args[i]);
            reason.append(" ");
        }
        reason.append("\"");

        String uncolored_reason = reason.toString().replace(" \"", "\"");
        if(uncolored_reason.startsWith("§")) {
            uncolored_reason = uncolored_reason.substring(2);
        }

        JsonParser parser = new JsonParser();
        JsonObject object = new JsonObject();
        object.add("name", parser.parse(target.getName()));
        object.add("reason", parser.parse(uncolored_reason));
        object.add("reporter", parser.parse(player.getName()));

        JsonObject jsonObject = plugin.data.get("reports").getAsJsonObject();
        jsonObject.add(target.getUniqueId() + "--" + report_id, object);
        report_id += 1;

        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!(p.hasPermission("reporting.viewreports"))) {
                continue;
            }
            p.sendMessage(Utils.color("&e&l-- PLAYER REPORTED --"));
            p.sendMessage(Utils.color("&ePlayer: " + target.getName()));
            p.sendMessage(Utils.color("&eReason: " + uncolored_reason));
            p.sendMessage(Utils.color("&eReported By: " + player.getName()));
            p.sendMessage(Utils.color("&e&l-- REPORT END --"));

        }

        player.sendMessage(Utils.color("&aThank you for your report a team member will take care of it shortly."));

        return true;
    }
}
