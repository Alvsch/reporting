package me.alvsch.reporting.events;

import me.alvsch.reporting.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerEvents implements Listener {

    Main plugin = Main.getPlugin();

    @EventHandler
    public void playerDropItem(InventoryClickEvent event) {

        if(!event.getView().getTitle().equals("§cReports") && !event.getView().getTitle().contains("§cReport")) {
            return;
        }

        event.setCancelled(true);

        if (event.getClick().equals(ClickType.RIGHT)) {
            if (event.getCurrentItem() == null) {
                return;
            }
            String uuid = event.getCurrentItem().getItemMeta().getLore().get(4).split(" ")[2];

            plugin.data.get("reports").getAsJsonObject().remove(uuid);
            event.getClickedInventory().setItem(event.getRawSlot(), null);

        }

    }

}
