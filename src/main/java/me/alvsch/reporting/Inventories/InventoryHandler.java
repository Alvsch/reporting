package me.alvsch.reporting.Inventories;

import me.alvsch.reporting.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class InventoryHandler {

    public static void reportMenu(Player player, OfflinePlayer offlinePlayer, String[] args) {


        Inventory inv = Bukkit.createInventory(null, 27, Utils.color("&cReport " + offlinePlayer.getName()));

        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        for(int i = 0; i < 8; i++) {
            inv.setItem(i, item);
        }
        for(int i = 17; i < inv.getSize(); i++) {
            inv.setItem(i, item);
        }

        item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        skullMeta.setOwningPlayer(offlinePlayer);
        item.setItemMeta(skullMeta);
        inv.setItem(6, item);


        player.openInventory(inv);

    }

}
