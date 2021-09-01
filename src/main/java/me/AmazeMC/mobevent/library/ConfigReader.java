package me.AmazeMC.mobevent.library;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigReader {

    /**
     * Reads locations from configs
     * @param config
     * @param path
     * @return
     */
    public static Location getLocation(FileConfiguration config, String path) {
        World w = Bukkit.getServer().getWorld(config.getString(path + ".world"));
        double x = config.getDouble(path + ".x");
        double y = config.getDouble(path + ".y");
        double z = config.getDouble(path + ".z");
        double pitch = config.getDouble(path + ".pitch");
        double yaw = config.getDouble(path + ".yaw");

        return new Location(w, x, y, z, (float) pitch, (float) yaw);
    }
}
