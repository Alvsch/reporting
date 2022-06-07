package me.alvsch.reporting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.alvsch.reporting.Inventories.InventoryHandler;
import me.alvsch.reporting.commands.*;
import me.alvsch.reporting.events.PlayerEvents;
import me.alvsch.reporting.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public final class Main extends JavaPlugin {

    private static Main plugin;
    private static int report_id;

    public HashMap<Player, ItemStack> claimed_reports = new HashMap<>();
    public JsonObject data;

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        try {
            data = loadDataFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        report_id = data.get("id").getAsInt();

        InventoryHandler.init();

        getCommand("report").setExecutor(new ReportCommand());
        getCommand("viewreports").setExecutor(new ViewReportsCommand());
        getCommand("claimedreport").setExecutor(new ClaimedReportCommand());
        getCommand("moderate").setExecutor(new ModerateCommand());
        getCommand("reporttop").setExecutor(new ReportTopCommand());

        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        JsonParser parser = new JsonParser();
        data.add("id", parser.parse(String.valueOf(report_id)));

        try {
            saveDataFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private void saveDataFile() throws IOException {
        File dataFile = new File(getDataFolder(), "data.json");
        FileWriter fileWriter = new FileWriter(dataFile, false);
        fileWriter.write(data.toString());
        fileWriter.close();

    }
    private JsonObject loadDataFile() throws FileNotFoundException  {
        // load data.json (generate one if not there)
        // console and IO, instance
        File dataFile = new File(getDataFolder(), "data.json");
        if (!dataFile.exists()) {
            Utils.loadData(this, "data.json");
        }

        Scanner scanner = new Scanner(dataFile);
        StringBuilder data = new StringBuilder();
        while (scanner.hasNextLine()) {
            data.append(scanner.nextLine());
        }
        if(data.toString().isEmpty()) {
            data.append("{");
            data.append("\"id\": 0,");
            data.append("\"playertop\": {},");
            data.append("\"reports\": {}");
            data.append("}");
        }
        JsonParser parser = new JsonParser();
        return (JsonObject) parser.parse(data.toString());
    }

    public static Main getPlugin() {
        return plugin;
    }
    public static int getReportId() {
        return report_id;
    }


}
