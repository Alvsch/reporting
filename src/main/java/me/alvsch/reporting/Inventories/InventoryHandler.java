package me.alvsch.reporting.Inventories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.tools.javac.jvm.Items;
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

import java.util.*;

public class InventoryHandler {

    private static Main plugin = Main.getPlugin();

    private static ItemStack placeholder;

    public static void init() {
        placeholder = Utils.createItem(Material.RED_STAINED_GLASS_PANE, 1, " ");

    }

    public static void reportMenu(Player player, OfflinePlayer offlinePlayer) {


        Inventory inv = Bukkit.createInventory(null, 27, Utils.color("&cReport " + offlinePlayer.getName()));

        Utils.fillRows(placeholder, inv, 1, 9);
        Utils.fillRows(placeholder, inv, 19, 27);

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
        plugin.claimed_reports.remove(player);
        plugin.claimed_reports.put(player, report);
        String uuid = report.getItemMeta().getLore().get(4).split(" ")[2];

        plugin.data.get("reports").getAsJsonObject().remove(uuid);

        Inventory inv = Bukkit.createInventory(null, 27, "§cClaimed Report");

        Utils.fillRows(placeholder, inv, 1, 9);
        Utils.fillRows(placeholder, inv, 19,27);

        report.setType(Material.PAPER);
        report = Utils.removeLore(report, 4, 5);
        inv.setItem(4, report);

        inv.setItem(11, Utils.createItem(Material.ENDER_PEARL, 1, "&bTeleport To Victim"));
        inv.setItem(13, Utils.createItem(Material.EMERALD_BLOCK, 1, "&fBan / Mute Menu"));
        inv.setItem(15, Utils.createItem(Material.REDSTONE_BLOCK, 1, "&cDismiss"));

        player.closeInventory();
        player.openInventory(inv);

    }

    public static void punishMenu(Player player, OfflinePlayer target) {
        Inventory inv = Bukkit.createInventory(null, 36, "§cPunish " + target.getName());

        Utils.fillRows(placeholder, inv, 1, 9);
        Utils.fillRows(placeholder, inv , 28, 36);

        inv.setItem(9, Utils.createItem(Material.PAPER, 1, "Insult", "15m mute"));
        inv.setItem(10, Utils.createItem(Material.PAPER, 1, "Racist Words", "15m mute"));
        inv.setItem(11, Utils.createItem(Material.PAPER, 1, "Swearing", "15m mute"));
        inv.setItem(12, Utils.createItem(Material.PAPER, 1, "Spamming", "10m mute"));
        inv.setItem(13, Utils.createItem(Material.DIAMOND_SWORD, 1, "KillAura", "30d ban"));
        inv.setItem(14, Utils.createItem(Material.DIAMOND_SWORD, 1, "AntiKnockBack", "30d ban"));
        inv.setItem(15, Utils.createItem(Material.DIAMOND_SWORD, 1, "Flying", "25d ban"));
        inv.setItem(16, Utils.createItem(Material.DIAMOND_SWORD, 1, "Insult", "30d ban"));
        inv.setItem(17, Utils.createItem(Material.ANVIL, 1, "Illegal Builds", "10d ban"));
        inv.setItem(18, Utils.createItem(Material.ANVIL, 1, "Other", "5d ban"));
        inv.setItem(19, Utils.createItem(Material.ANVIL, 1, "Other", "10d ban"));
        inv.setItem(20, Utils.createItem(Material.ANVIL, 1, "Other", "15d ban"));
        inv.setItem(21, Utils.createItem(Material.ANVIL, 1, "Other", "20d ban"));
        inv.setItem(22, Utils.createItem(Material.ANVIL, 1, "Other", "25d ban"));
        inv.setItem(23, Utils.createItem(Material.ANVIL, 1, "Other", "30d ban"));

        player.closeInventory();
        player.openInventory(inv);

    }

    public static void viewReportsMenu(Player player, int page, Main plugin) {
        Inventory inv = Bukkit.createInventory(null, 54, Utils.color("&cReports"));

        Utils.fillRows(placeholder, inv, 1, 9);
        Utils.fillRows(placeholder, inv, 46, 54);
        inv.setItem(4, Utils.createItem(Material.PAPER, 1, String.valueOf(page)));
        inv.setItem(51, Utils.createItem(Material.ARROW, 1, "&fNext Page"));

        if(page != 0) {
            inv.setItem(47, Utils.createItem(Material.ARROW, 1, "&fPrevious Page"));
        }
        inv.setItem(49, Utils.createItem(Material.BUCKET, 1, "&fClaimed Report", "§fOpens Your currently claimed report"));


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
            lore.add("§f§lLeftClick to claim");
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

    public static void reportsTopMenu(Player player) {

        Inventory inv = Bukkit.createInventory(null, 54, "§cReports Top");

        Utils.fillRows(placeholder, inv, 1, 9);
        Utils.fillRows(placeholder, inv, 46, 54);

        JsonObject data = plugin.data.get("playertop").getAsJsonObject();
        TreeMap<OfflinePlayer, Integer> toplist = new TreeMap<>();
        for(Map.Entry<String, JsonElement> entry : data.entrySet()) {
            JsonObject top = entry.getValue().getAsJsonObject();
            int dismissed = top.get("dismissed").getAsInt();
            int punished = top.get("punished").getAsInt();
            int total = dismissed + punished;

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey()));

            toplist.put(offlinePlayer, total);

        }

        Map<OfflinePlayer, Integer> sorted_toplist =  Utils.sortByValues(toplist);

        for(Map.Entry<OfflinePlayer, Integer> entry : sorted_toplist.entrySet()) {



        }


        player.closeInventory();
        player.openInventory(inv);

    }

}
