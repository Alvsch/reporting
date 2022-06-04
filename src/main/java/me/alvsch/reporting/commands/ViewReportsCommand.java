package me.alvsch.reporting.commands;

import com.google.gson.JsonObject;
import me.alvsch.reporting.Inventories.InventoryHandler;
import me.alvsch.reporting.Main;
import me.alvsch.reporting.utils.JsonUtils;
import me.alvsch.reporting.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ViewReportsCommand implements CommandExecutor {

    Main plugin = Main.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(Utils.color("You can't use this command as console"));
            return true;
        }
        Player player = (Player) sender;

        InventoryHandler.viewReportsMenu(player, 0, plugin);
        if(!JsonUtils.exists(JsonUtils.getProperty(plugin.data, "playertop").getAsJsonObject(), player.getUniqueId().toString())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("dismissed", 0);
            jsonObject.addProperty("punihsed", 0);

            JsonUtils.add(JsonUtils.getProperty(plugin.data, "playertop").getAsJsonObject(), player.getUniqueId().toString(), jsonObject);
        }

        return true;
    }
}