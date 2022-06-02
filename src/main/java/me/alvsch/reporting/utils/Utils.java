package me.alvsch.reporting.utils;

import com.google.common.io.ByteStreams;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    public static File loadData(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResource(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }

    public static ItemStack createItem(Material material, int amount, String display_name, String... lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.color(display_name));
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;

    }
    public static ItemStack removeLore(ItemStack item, int... lore_number) {
        if(lore_number.length < 1) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        for(int i : lore_number) {
            lore.remove(i);
        }

        return item;
    }

    public static void fillRows(ItemStack item, Inventory inv, int start, int end) {
        for(int i = start - 1; i < end; i++) {
            inv.setItem(i, item);
        }

    }

}
