package me.alvsch.reporting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.alvsch.reporting.commands.ReportCommand;
import me.alvsch.reporting.commands.ViewReportsCommand;
import me.alvsch.reporting.events.PlayerEvents;
import me.alvsch.reporting.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public final class Main extends JavaPlugin {

    private static Main plugin;

    public JsonObject data;


    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        getCommand("report").setExecutor(new ReportCommand());
        getCommand("viewreports").setExecutor(new ViewReportsCommand());

        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

        try {
            data = loadDataFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

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
    private JsonObject loadDataFile() throws FileNotFoundException {
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
            data.append("\"reports\": {}");
            data.append("}");
        }
        JsonParser parser = new JsonParser();
        return (JsonObject) parser.parse(data.toString());
    }

    public static Main getPlugin() {
        return plugin;
    }


}
