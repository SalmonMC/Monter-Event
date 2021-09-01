package me.AmazeMC.mobevent.filemanager.messages;

import me.AmazeMC.mobevent.MobEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessagesFile extends YamlConfiguration {

    private String fileName = "messages.yml";

    private File file;
    private FileConfiguration fileConfig;

    /**
     * Initialise constructor
     */
    public MessagesFile() {
        file = new File(MobEvent.getInstance().getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                try {
                    MobEvent.getInstance().saveResource(fileName, true);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fileConfig = YamlConfiguration.loadConfiguration(file);

        for (Messages msg : Messages.values()) msg.set(fileConfig);
    }

    /**
     * Gets the config file
     * @return
     */
    public FileConfiguration getFile() {
        return fileConfig;
    }

    /**
     * Saves the config file
     */
    public void saveConfig() {
        try {
            fileConfig.save(file);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reloads the config file
     */
    public void reloadFile() {
        fileConfig = loadConfiguration(file);
    }
}
