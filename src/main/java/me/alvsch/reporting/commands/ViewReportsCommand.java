package me.alvsch.reporting.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.alvsch.reporting.Main;
import me.alvsch.reporting.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ViewReportsCommand implements CommandExecutor {

    Main plugin = Main.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(Utils.color("You can't use this command as console"));
            return true;
        }
        Player player = (Player) sender;

        Inventory inv = Bukkit.createInventory(null, 54, Utils.color("&cReports"));

        JsonObject reports = plugin.data.get("reports").getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : reports.entrySet()) {
            JsonObject report = entry.getValue().getAsJsonObject();
            String name = report.get("name").toString();
            String reason = report.get("reason").toString();
            String reporter = report.get("reporter").toString();

            ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwningPlayer(Bukkit.getPlayer(UUID.fromString(entry.getKey())));

            List<String> lore = new ArrayList<>();
            lore.add("§7Name: " + name);
            lore.add("§7Reason: " + reason);
            lore.add("");
            lore.add("§7Reported By: " + reporter);
            lore.add("§7Reporter UUID: " + entry.getKey());
            lore.add("§f§lLeftClick to claim (WIP)");
            lore.add("§f§lRightClick to remove");
            meta.setLore(lore);
            meta.setDisplayName(Utils.color("&cReport"));

            item.setItemMeta(meta);
            inv.addItem(item);
        }
        player.openInventory(inv);


        return true;
    }
}