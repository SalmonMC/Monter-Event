package me.AmazeMC.mobevent.managers;

import me.AmazeMC.mobevent.MobEvent;
import me.AmazeMC.mobevent.filemanager.messages.Messages;
import me.AmazeMC.mobevent.library.ConfigReader;
import me.AmazeMC.mobevent.utils.ChatUtils;
import me.AmazeMC.mobevent.utils.GUIUtils;
import me.AmazeMC.mobevent.utils.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class WaveManager {

    private ArrayList<Entity> entities = new ArrayList<>();

    private int currentWave = 0;
    private double entitiesCount = 0;
    private double entitiesCountLive = 0;

    private int timeToStart = 0;

    /**
     * Starts the first wave
     */
    public void start(int wave) {
        if (MobEvent.getInstance().getWavesFile().getFile().get("mobevent.waves." + wave + ".time_to_start") != null) {
            timeToStart = MobEvent.getInstance().getWavesFile().getFile().getInt("mobevent.waves." + wave + ".time_to_start");
        }

        // Little time to start the wave
        new BukkitRunnable() {
            @Override
            public void run() {
                if (timeToStart <= 0) {
                    startWave(wave);

                    if (MobEvent.getInstance().getWavesFile().getFile().get("mobevent.waves." + wave + ".wave_spawn_messages") != null) {
                        if (MobEvent.getInstance().getWavesFile().getFile().getBoolean("mobevent.waves." + wave + ".wave_spawn_messages")) {
                            for (Player p : MobEvent.getInstance().getEventManager().getList().keySet()) {
                                ChatUtils.sendTitleOrChat(p, ChatUtils.format(Messages.WAVE_SPAWNED.get())
                                        .replace("%time_seconds%", String.valueOf(timeToStart))
                                        .replace("%time_minutes%", String.valueOf(timeToStart / 60))
                                        .replace("%wave%", String.valueOf(wave)), true, 10, 35, 10);
                            }
                        }
                    }

                    this.cancel();
                    return;
                }

                if (MobEvent.getInstance().getWavesFile().getFile().get("mobevent.waves." + wave + ".wave_spawn_messages") != null) {
                    if (MobEvent.getInstance().getWavesFile().getFile().getBoolean("mobevent.waves." + wave + ".wave_spawn_messages")) {
                        if (timeToStart <= MobEvent.getInstance().getConfig().getInt("event.spawn-wave-start-counting-down-from")) {
                            for (Player p : MobEvent.getInstance().getEventManager().getList().keySet()) {
                                ChatUtils.sendTitleOrChat(p, ChatUtils.format( Messages.WAVE_SPAWNING.get())
                                        .replace("%time_seconds%", String.valueOf(timeToStart))
                                        .replace("%time_minutes%", String.valueOf(timeToStart / 60)), true, 10, 35, 10);
                            }
                        }
                    }
                }

                timeToStart--;
            }
        }.runTaskTimer(MobEvent.getInstance(), 0, 20);
    }

    /**
     * Spawns the specified wave
     * @param wave
     */
    public void startWave(int wave) {
        for (String w : MobEvent.getInstance().getWavesFile().getFile().getStringList("mobevent.waves." +  wave + ".entities")) {
            String[] waveMeta = w.split(":");

            String mobType = waveMeta[0];
            int mobAmount = Integer.parseInt(waveMeta[1]);
            int mobSpawnTime = Integer.parseInt(waveMeta[2]);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (MobEvent.getInstance().getEventManager().hasStarted()) {
                        for (int i = 0; i < mobAmount; i++) {
                            Entity ent = Bukkit.getServer().getWorld(getRandomMobSpawnpoint().getWorld().getName()).spawnEntity(getRandomMobSpawnpoint(), EntityType.fromName(mobType));
                            //                Entity ent = new CraftZombie(getRandomMobSpawnpoint())
                            //                        .setName("&cHarold")
                            //                        .setHelmet(Material.DIAMOND_HELMET)
                            //                        .setChestplate(Material.GOLDEN_CHESTPLATE)
                            //                        .setLeggings(Material.IRON_LEGGINGS)
                            //                        .setBoots(Material.LEATHER_BOOTS)
                            //                        .setMainHand(Material.WOODEN_HOE)
                            //                        .setOffhand(Material.SHIELD)
                            //                        .setHealth(100.0)
                            //                        .setSpeed(5)
                            //                        .setStrength(10)
                            //                        .build();

                            ent.setMetadata("isEventMob", new FixedMetadataValue(MobEvent.getInstance(), true));

                            ent.setCustomNameVisible(true);
                            ent.setPersistent(true);

                            if (ent.getType().getName().equalsIgnoreCase("PIGLIN_BRUTE")) {
                                PiglinBrute brute = (PiglinBrute) ent;
                                brute.setImmuneToZombification(true);
                            }
                            else if (ent.getType().getName().equalsIgnoreCase("HOGLIN")) {
                                Hoglin hoglin = (Hoglin) ent;
                                hoglin.setImmuneToZombification(true);
                            }

                            Creature creature = (Creature) ent;
                            creature.setTarget(MobEvent.getInstance().getEventManager().getRandomPlayer());
                            creature.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100.0);

                            entitiesCount++;
                            entities.add(ent);
                        }
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskLater(MobEvent.getInstance(), 20 * mobSpawnTime);
        }
    }

    /**
     * Gets one of the random mob spawnpoints
     * @return
     */
    public Location getRandomMobSpawnpoint() {
        int spawnpointsSize = MobEvent.getInstance().getDataFile().getFile().getConfigurationSection("mobevent.mob-spawnpoints").getKeys(false).size();
        int possibleSpawnpoints = (spawnpointsSize == 1) ? 0 : NumberUtils.randomizeInt(spawnpointsSize);
        return ConfigReader.getLocation(MobEvent.getInstance().getDataFile().getFile(), "mobevent.mob-spawnpoints." + possibleSpawnpoints);
    }

    /**
     * Gets the entities from the waves
     * @return
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    /**
     * Gets the current wave count
     * @return
     */
    public int getCurrentWave() {
        return currentWave;
    }

    /**
     * Checks if there's a next wave
     * @return
     */
    public boolean hasNextWave() {
        int fakeWave = currentWave;
        return MobEvent.getInstance().getWavesFile().getFile().contains("mobevent.waves." + ++fakeWave);
    }

    /**
     * Starts the next wave
     */
    public void startNextWave() {
        start(++currentWave);
    }

    /**
     * Opens the next wave GUI
     * @param p
     */
    public void openNextWaveGui(Player p) {
        p.openInventory(GUIUtils.getGUI("next_wave_inventory", p));
    }

    /**
     * Gets the completion percentage
     * @return
     */
    public double getCompletion() {
        return MobEvent.getInstance().getWavesFile().getFile().getInt("mobevent.waves." +  currentWave + ".completion");
    }

    /**
     * Total entities of the wave
     * @return
     */
    public double getEntitiesCount() {
        return entitiesCount;
    }

    /**
     * Decrements an entity of the wave
     */
    public void incrementEntitiesCountLive() {
        entitiesCountLive++;
    }

    /**
     * Gets the live entities count
     * @return
     */
    public double getEntitiesCountLive() {
        return entitiesCountLive;
    }

    /**
     * Resets the whole class
     */
    public void reset() {
        currentWave = 0;
        entitiesCount = 0;
        entitiesCountLive = 0;
        for (Entity ent : entities) {
            ent.remove();
        }
        entities.clear();
        MobEvent.getInstance().getEventManager().getQueueList().clear();
        MobEvent.getInstance().getRewardManager().clear();
    }

    /**
     * Resets the whole class without clearing mobs
     */
    public void resetWithoutClearingMobs() {
        entitiesCount = 0;
        entitiesCountLive = 0;
        entities.clear();
        MobEvent.getInstance().getEventManager().getQueueList().clear();
    }

    /**
     * Executes when a wave is cleared
     */
    public void clearedCurrentWave() {
        if (MobEvent.getInstance().getWavesFile().getFile().get("mobevent.waves." + currentWave + ".rewards") !=  null) {
            List<String> rewards = MobEvent.getInstance().getWavesFile().getFile().getStringList("mobevent.waves." + currentWave + ".rewards");

            for (String reward : rewards) {
                MobEvent.getInstance().getRewardManager().addReward(reward);
            }
        }
    }
}
