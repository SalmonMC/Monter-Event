package me.AmazeMC.mobevent.managers;

import me.AmazeMC.mobevent.MobEvent;
import me.AmazeMC.mobevent.utils.CuboidUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class MonsterManager {

    private BukkitTask task;

    /**
     * Starts the cooldown
     */
    public void start() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!MobEvent.getInstance().getEventManager().hasStarted()) {
                    cancel();
                    return;
                }
                for (Entity ent : MobEvent.getInstance().getWaveManager().getEntities()) {
                    Location minLoc = CuboidUtils.getLocation("mobevent.region-locations.min");
                    Location maxLoc = CuboidUtils.getLocation("mobevent.region-locations.max");

                    if (!CuboidUtils.isInCuboid(ent.getLocation(), minLoc, maxLoc)) {
                        ent.teleport(MobEvent.getInstance().getWaveManager().getRandomMobSpawnpoint());
                    }
                }
            }
        }.runTaskTimerAsynchronously(MobEvent.getInstance(), 0, 20);
    }

    /**
     * Stops the scheduler
     */
    public void stop() {
        if (task != null) {
            task.cancel();
        }
    }
}

