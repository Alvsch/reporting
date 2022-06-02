package me.alvsch.reporting.events;

import me.alvsch.reporting.Inventories.InventoryHandler;
import me.alvsch.reporting.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerEvents implements Listener {

    Main plugin = Main.getPlugin();

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        if(event.getView().getTitle().equals("§cReports")) {
            if (event.getCurrentItem() == null) {
                return;
            }

            if(event.getCurrentItem().getType().equals(Material.ARROW)) {
                ItemMeta page_meta = event.getClickedInventory().getItem(4).getItemMeta();
                int page = Integer.parseInt(page_meta.getDisplayName());

                if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§fNext Page")) {
                    System.out.print("next");
                    InventoryHandler.viewReportsMenu(player, page + 1, plugin);

                }
                if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§fPrevious Page")) {
                    InventoryHandler.viewReportsMenu(player, page - 1, plugin);

                }
                return;
            }

            if (event.getClick().equals(ClickType.RIGHT)) {
                String uuid = event.getCurrentItem().getItemMeta().getLore().get(4).split(" ")[2];

                plugin.data.get("reports").getAsJsonObject().remove(uuid);
                event.getClickedInventory().setItem(event.getRawSlot(), null);

            }
            if(event.getClick().equals(ClickType.LEFT)) {
                if(event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
                    InventoryHandler.claimReportMenu(player, event.getCurrentItem());
                }

            }
            return;
        }
        if(event.getView().getTitle().contains("§cReport")) {
            if (event.getCurrentItem() == null) {
                return;
            }
        event.getCurrentItem().getItemMeta().getDisplayName();
            player.performCommand("reporting:report " +
                    event.getClickedInventory().getItem(4).getItemMeta().getDisplayName().split("'s")[0] +
                    " " + event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§f", ""));
            player.closeInventory();
            return;
        }
        event.setCancelled(false);



    }

}
