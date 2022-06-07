package me.alvsch.reporting.events;

import com.google.gson.JsonObject;
import me.alvsch.reporting.Inventories.InventoryHandler;
import me.alvsch.reporting.Main;
import me.alvsch.reporting.utils.JsonUtils;
import me.alvsch.reporting.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class PlayerEvents implements Listener {

    Main plugin = Main.getPlugin();


    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(!player.hasPermission("reporting.viewreports")) {
            return;
        }
        if(!JsonUtils.exists(plugin.data.get("playertop").getAsJsonObject(), player.getUniqueId().toString())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("dismissed", 0);
            jsonObject.addProperty("punished", 0);

            JsonUtils.add(JsonUtils.getProperty(plugin.data, "playertop").getAsJsonObject(), player.getUniqueId().toString(), jsonObject);
        }

    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if(event.getView().getTitle().equals("§cReports")) {
            if (item == null) {
                return;
            }

            if(item.getType().equals(Material.ARROW)) {
                ItemMeta page_meta = event.getClickedInventory().getItem(4).getItemMeta();
                int page = Integer.parseInt(page_meta.getDisplayName());

                if(item.getItemMeta().getDisplayName().equals("§fNext Page")) {
                    InventoryHandler.viewReportsMenu(player, page + 1, plugin);

                }
                if(item.getItemMeta().getDisplayName().equals("§fPrevious Page")) {
                    InventoryHandler.viewReportsMenu(player, page - 1, plugin);

                }
                return;
            }
            if(item.getType().equals(Material.BUCKET)) {
                player.performCommand("reporting:myreport");
            }

            if (event.getClick().equals(ClickType.RIGHT)) {
                String uuid = item.getItemMeta().getLore().get(4).split(" ")[2];

                plugin.data.get("reports").getAsJsonObject().remove(uuid);
                event.getClickedInventory().setItem(event.getRawSlot(), null);

            }
            if(event.getClick().equals(ClickType.LEFT)) {
                if(item.getType().equals(Material.PLAYER_HEAD)) {
                    InventoryHandler.claimReportMenu(player, item);
                }

            }
            return;
        }
        if(event.getView().getTitle().contains("§cReport ")) {
            if (item == null) {
                return;
            }
        item.getItemMeta().getDisplayName();
            player.performCommand("reporting:report " +
                    event.getClickedInventory().getItem(4).getItemMeta().getDisplayName().split("'s")[0] +
                    " " + item.getItemMeta().getDisplayName());
            player.closeInventory();
            return;
        }
        if(event.getView().getTitle().equals("§cClaimed Report")) {
            if (item == null) {
                return;
            }
            Material material = item.getType();
            UUID target = UUID.fromString(event.getClickedInventory().getItem(4).getItemMeta().getLore().get(4).split(" ")[2].split("--")[0]);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(target);
            if(material.equals(Material.ENDER_PEARL)) {
                if(offlinePlayer.isOnline()) {
                    player.teleport((Player) offlinePlayer);
                    player.performCommand(plugin.getConfig().getString("fly-on-command"));
                    player.performCommand(plugin.getConfig().getString("vanish-on-command"));

                    player.sendMessage(Utils.color("&6Successfully Teleported To " + offlinePlayer.getName()));
                } else {
                    player.sendMessage(Utils.color("&c" + offlinePlayer.getName() + " &cIs Not Online"));
                }
                player.closeInventory();
            }
            if(material.equals(Material.EMERALD_BLOCK)) {
                InventoryHandler.punishMenu(player, offlinePlayer, true);
            }
            if(material.equals(Material.REDSTONE_BLOCK)) {
                plugin.claimed_reports.remove(player);
                player.sendMessage(Utils.color("&cDismissed Report"));
                JsonObject data = JsonUtils.getProperty(plugin.data, "playertop").getAsJsonObject();
                JsonObject player_data = data.get(player.getUniqueId().toString()).getAsJsonObject();
                JsonUtils.addProperty(plugin.data, "dismissed", JsonUtils.getProperty(player_data, "dismissed").getAsInt() + 1);


                player.closeInventory();
            }


            return;
        }
        if(event.getView().getTitle().contains("§cPunish")) {
            List<String> lore = item.getItemMeta().getLore();
            String punish_length = lore.get(0).split(" ")[0];
            String punish_type = lore.get(0).split(" ")[1];
            String punish_reason = item.getItemMeta().getDisplayName();

            String tempban_command = plugin.getConfig().getString("tempban-command");
            String mute_command = plugin.getConfig().getString("mute-command");

            if(tempban_command == null || mute_command == null) {
                return;
            }
            tempban_command = tempban_command.replace("%PLAYER%", event.getView().getTitle().split(" ")[1]);
            tempban_command = tempban_command.replace("%TIME%", punish_length);
            tempban_command = tempban_command.replace("%REASON%", punish_reason);
            mute_command = mute_command.replace("%PLAYER%", event.getView().getTitle().split(" ")[1]);
            mute_command = mute_command.replace("%TIME%", punish_length);
            mute_command = mute_command.replace("%REASON%", punish_reason);

            if(punish_type.equalsIgnoreCase("ban")) {
                player.performCommand(tempban_command);

            }
            if(punish_type.equalsIgnoreCase("mute")) {
                player.performCommand(mute_command);

            }
            plugin.claimed_reports.remove(player);

            player.closeInventory();
            if(event.getView().getTitle().endsWith("-report-")) {
                JsonObject data = JsonUtils.getProperty(plugin.data, "playertop").getAsJsonObject();
                JsonObject player_data = data.get(player.getUniqueId().toString()).getAsJsonObject();
                JsonUtils.addProperty(plugin.data, "punished", JsonUtils.getProperty(player_data, "punished").getAsInt() + 1);

            }

            return;
        }
        if(event.getView().getTitle().equals("§cReports Top")) {

            if(item.getType().equals(Material.ARROW)) {
                ItemMeta page_meta = event.getClickedInventory().getItem(4).getItemMeta();
                int page = Integer.parseInt(page_meta.getDisplayName());

                if(item.getItemMeta().getDisplayName().equals("§fNext Page")) {
                    InventoryHandler.reportTopMenu(player, page + 1);

                }
                if(item.getItemMeta().getDisplayName().equals("§fPrevious Page")) {
                    InventoryHandler.reportTopMenu(player, page - 1);

                }
                return;
            }

            return;
        }
        event.setCancelled(false);

    }

}
