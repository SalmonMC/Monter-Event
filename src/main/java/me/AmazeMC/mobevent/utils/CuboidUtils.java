package me.AmazeMC.mobevent.utils;

import me.AmazeMC.mobevent.MobEvent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class CuboidUtils {

    /**
     * Checks if location is in cuboid
     * @param loc
     * @param minLoc
     * @param maxLoc
     * @return
     */
    public static boolean isInCuboid(Location loc, Location minLoc, Location maxLoc){
        double maxX = Math.max(minLoc.getX(), maxLoc.getX());
        double maxY = Math.max(minLoc.getY(), maxLoc.getY());
        double maxZ = Math.max(minLoc.getZ(), maxLoc.getZ());
        Vector max = new Vector(maxX, maxY, maxZ);

        double minX = Math.min(minLoc.getX(), maxLoc.getX());
        double minY = Math.min(minLoc.getY(), maxLoc.getY());
        double minZ = Math.min(minLoc.getZ(), maxLoc.getZ());
        Vector min = new Vector(minX, minY,minZ);

        return loc.toVector().isInAABB(min,max);
    }

    public static Location getLocation(String path) {
        World w = MobEvent.getInstance().getServer().getWorld(MobEvent.getInstance().getDataFile().getFile().getString(path + ".world"));
        double x = MobEvent.getInstance().getDataFile().getFile().getDouble(path + ".x");
        double y = MobEvent.getInstance().getDataFile().getFile().getDouble(path + ".y");
        double z = MobEvent.getInstance().getDataFile().getFile().getDouble(path + ".z");

        Location loc = new Location(w, x, y, z);

        return loc;
    }
}
