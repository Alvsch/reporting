package me.alvsch.reporting.Inventories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.alvsch.reporting.Main;
import me.alvsch.reporting.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InventoryHandler {

    private static ItemStack placeholder;

    public static void init() {
        placeholder = Utils.createItem(Material.RED_STAINED_GLASS_PANE, 1, " ");

    }

    public static void reportMenu(Player player, OfflinePlayer offlinePlayer) {


        Inventory inv = Bukkit.createInventory(null, 27, Utils.color("&cReport " + offlinePlayer.getName()));

        for(int i = 0; i < 9; i++) {
            inv.setItem(i, placeholder);
        }
        for(int i = 18; i < inv.getSize(); i++) {
            inv.setItem(i, placeholder);
        }

        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        skullMeta.setOwningPlayer(offlinePlayer);
        item.setItemMeta(skullMeta);
        inv.setItem(4, item);

        item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cChat");
        item.setItemMeta(meta);
        inv.setItem(10, item);

        item = new ItemStack(Material.DIAMOND_SWORD, 1);
        meta = item.getItemMeta();
        meta.setDisplayName("§cHacking");
        item.setItemMeta(meta);
        inv.setItem(13, item);

        item = new ItemStack(Material.ANVIL, 1);
        meta = item.getItemMeta();
        meta.setDisplayName("§cOther");
        item.setItemMeta(meta);
        inv.setItem(16, item);


        player.openInventory(inv);

    }

    public static void claimReportMenu(Player player, ItemStack report) {
        Inventory inv = Bukkit.createInventory(null, 27, "§cClaimed Report");

        inv.setItem(0, report);

        player.openInventory(inv);

    }

    public static void viewReportsMenu(Player player, int page, Main plugin) {
        Inventory inv = Bukkit.createInventory(null, 54, Utils.color("&cReports"));

        for(int i = 0; i < 9; i++) {
            inv.setItem(i, placeholder);
        }
        for(int i = 45; i < inv.getSize(); i++) {
            inv.setItem(i, placeholder);
        }
        inv.setItem(4, Utils.createItem(Material.PAPER, 1, String.valueOf(page)));
        inv.setItem(51, Utils.createItem(Material.ARROW, 1, "&fNext Page"));

        if(page != 0) {
            inv.setItem(47, Utils.createItem(Material.ARROW, 1, "&fPrevious Page"));
        }


        List<ItemStack> items = new ArrayList<>();
        JsonObject reports = plugin.data.get("reports").getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : reports.entrySet()) {
            JsonObject report = entry.getValue().getAsJsonObject();
            String name = report.get("name").toString();
            String reason = report.get("reason").toString();
            String reporter = report.get("reporter").toString();

            ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwningPlayer(Bukkit.getPlayer(UUID.fromString(entry.getKey().split("--")[0])));

            List<String> lore = new ArrayList<>();
            lore.add("§7Name: " + name);
            lore.add("§7Reason: " + reason);
            lore.add("");
            lore.add("§7Reported By: " + reporter);
            lore.add("§7Report ID: " + entry.getKey());
            lore.add("§f§lLeftClick to claim (WIP)");
            lore.add("§f§lRightClick to remove");
            meta.setLore(lore);
            meta.setDisplayName(Utils.color("&cReport"));

            item.setItemMeta(meta);
            items.add(item);
        }
        int amount = 36;
        if (items.size() / 36 < page) {
            return;
        }
        for(ItemStack i : items.subList(amount*page, Math.min(items.size(), amount*page+amount))) {
            inv.addItem(i);
        }

        player.closeInventory();
        player.openInventory(inv);


    }
}
