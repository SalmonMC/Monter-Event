package me.AmazeMC.mobevent.managers;

import me.AmazeMC.mobevent.MobEvent;
import me.AmazeMC.mobevent.filemanager.messages.Messages;
import me.AmazeMC.mobevent.library.ConfigReader;
import me.AmazeMC.mobevent.utils.ChatUtils;
import me.AmazeMC.mobevent.utils.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class EventManager {

    private HashMap<Player, Location> event = new HashMap<Player, Location>();
    private ArrayList<Player> queue = new ArrayList<Player>();

    private boolean isStarted = false;

    /**
     * Get the size of the event
     * @return
     */
    public int getCount() {
        return event.size();
    }

    /**
     * Gets the event list
     * @return
     */
    public HashMap<Player, Location> getList() {
        return event;
    }

    /**
     * Adds player into the event
     * @param p
     */
    public void put(Player p, Location loc) {
        if (event.containsKey(p)) return;
        event.put(p, loc);
    }

    /**
     * Removes player out of the event
     * @param p
     */
    public void remove(Player p) {
        if (!event.containsKey(p)) return;
        event.remove(p);
    }

    /**
     * Checks if the exists in the event
     * @param p
     * @return boolean
     */
    public boolean exists(Player p) {
        return event.containsKey(p);
    }

    /**
     * Checks if the event has started
     * @return
     */
    public boolean hasStarted() {
        return isStarted;
    }

    /**
     * Kicks out the player of the event completely
     * @param p
     */
    public void kickPlayer(Player p, boolean giveRewards) {
        teleportPlayerBack(p, giveRewards);
        remove(p);

        if (queue.contains(p)) {
            queue.remove(p);

            for (Player eP : event.keySet()) {
                eP.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.WAVE_QUIT.get())
                        .replace("%player%", p.getName())
                        .replace("%in_event%", String.valueOf(event.size() - queue.size()))
                        .replace("%max_event%", String.valueOf(event.size())));
            }
        }

        if (event.isEmpty()) {
            if (MobEvent.getInstance().getConfig().getBoolean("event.announce-event-finished-in-chat")) {
                Bukkit.getServer().broadcastMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.WAVE_CLEARED_GLOBAL.get())
                        .replace("%wave%", String.valueOf((MobEvent.getInstance().getWaveManager().getCurrentWave() + 1))));
            }

            MobEvent.getInstance().getWaveManager().reset();
            forceStop(giveRewards);
        }
        else if (MobEvent.getInstance().getEventManager().getQueueList().size() <= 0) {
            MobEvent.getInstance().getWaveManager().resetWithoutClearingMobs();
            MobEvent.getInstance().getWaveManager().startNextWave();
        }
    }

    /**
     * Executes when a player dies
     * @param p
     */
    public void executeDeath(Player p) {
        remove(p);

        if (event.isEmpty()) {
            if (MobEvent.getInstance().getConfig().getBoolean("event.announce-event-finished-in-chat")) {
                Bukkit.getServer().broadcastMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.WAVE_CLEARED_GLOBAL.get())
                        .replace("%wave%", String.valueOf((MobEvent.getInstance().getWaveManager().getCurrentWave() + 1))));
            }

            MobEvent.getInstance().getWaveManager().reset();
            forceStop(false);
        }
    }

    /**
     * Start the event with preparation time
     * @param time
     */
    public void start(final int time) {
        new BukkitRunnable() {
            int timer = time;
            public void run() {
                if (MobEvent.getInstance().getQueueManager().getCount() < MobEvent.getInstance().getConfig().getInt("event.max-players")) {
                    for (Player p : MobEvent.getInstance().getQueueManager().getList()) {
                        p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.SOMEONE_LEFT_QUEUE.get()));
                    }
                    MobEvent.getInstance().getReminderManager().endReminder();
                    this.cancel();
                    return;
                }

                if (isStarted) {
                    this.cancel();
                    return;
                }

                if (timer <= 0) {
                    for (Player p : MobEvent.getInstance().getQueueManager().getList()) {
//                        p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.EVENT_STARTED.get())
//                                .replace("%time_seconds%", String.valueOf(timer)));

                        ChatUtils.sendTitleOrChat(p, Messages.EVENT_STARTED.get()
                                .replace("%time_seconds%", String.valueOf(timer)), true, 10, 70, 10);
                    }
                    forceStart();
                    this.cancel();
                    return;
                }

                if (timer <= MobEvent.getInstance().getConfig().getInt("event.start-event-start-count-down-from")) {
                    for (Player p : MobEvent.getInstance().getQueueManager().getList()) {
                        ChatUtils.sendTitleOrChat(p, Messages.EVENT_STARTING_COUNTDOWN.get()
                                .replace("%time_seconds%", String.valueOf(timer))
                                .replace("%time_minutes%", String.valueOf(timer / 60)), true, 10, 70, 10);
                    }
                }

                timer--;
            }
        }.runTaskTimer(MobEvent.getInstance(), 0, 20);
    }

    /**
     * Force starts the event without preparation time
     */
    public void forceStart() {
        this.isStarted = true;
        MobEvent.getInstance().getCooldownManager().endCooldown();

        // Teleports players to spawnpoints
        for (Player p : MobEvent.getInstance().getQueueManager().getList()) {
            event.put(p, p.getLocation());

            int spawnpointsSize = MobEvent.getInstance().getDataFile().getFile().getConfigurationSection("mobevent.player-spawnpoints").getKeys(false).size();
            int possibleSpawnpoints = (spawnpointsSize == 1) ? 0 : NumberUtils.randomizeInt(spawnpointsSize);
            p.teleport(ConfigReader.getLocation(MobEvent.getInstance().getDataFile().getFile(), "mobevent.player-spawnpoints." + possibleSpawnpoints));
        }

        MobEvent.getInstance().getQueueManager().clear();
        MobEvent.getInstance().getWaveManager().start(0);
    }

    /**
     * Force stops the event and clears the event list
     */
    public void forceStop(boolean giveRewards) {
        this.isStarted = false;
        MobEvent.getInstance().getCooldownManager().startCooldown();
        teleportPlayersBack(giveRewards);
        event.clear();
    }

    /**
     * Teleports all the players from the event back to their original location
     */
    public void teleportPlayersBack(boolean giveRewards) {
        for (Player p : event.keySet()) {
            teleportPlayerBack(p, giveRewards);
        }
    }

    /**
     * Teleports the player back to their original location
     * @param p
     */
    public void teleportPlayerBack(Player p, boolean giveRewards) {
        if (!event.containsKey(p)) return;
        p.teleport(event.get(p));

        if (giveRewards) {
            MobEvent.getInstance().getRewardManager().giveReward(p);
        }
    }

    /**
     * Returns a random player from the event
     * @return
     */
    public Player getRandomPlayer() {
        int randomNumber = NumberUtils.randomizeInt(event.size());
        int i = 0;

        if (event.size() > 0) {
            for (Player p : event.keySet()) {
                if (i == randomNumber) {
                    return p;
                }
                i++;
            }
        }
        return null;
    }

    /**
     * Adds a player to the next wave queue
     * @param p
     */
    public void addQueue(Player p) {
        if (queue.contains(p)) return;
        queue.add(p);
    }

    /**
     * Removes a player for the next wave
     * @param p
     */
    public void removeQueue(Player p) {
        if (!queue.contains(p)) return;
        queue.remove(p);
    }


    /**
     * Ready a player up for the next wave
     * @param p
     */
    public void ready(Player p) {
        queue.remove(p);
        p.closeInventory();

        for (Player eP : event.keySet()) {
            eP.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.WAVE_CONTINUE.get())
                    .replace("%player%", p.getName())
                    .replace("%in_event%", String.valueOf((event.size() - queue.size())))
                    .replace("%max_event%", String.valueOf(event.size())));
        }

        if (MobEvent.getInstance().getEventManager().getQueueList().size() <= 0) {
            MobEvent.getInstance().getWaveManager().resetWithoutClearingMobs();
            MobEvent.getInstance().getWaveManager().startNextWave();
        }
    }

    /**
     * Readies everyone in the queue
     */
    public void readyAll() {
        for (Player p : event.keySet()) {
            if (inQueue(p)) {
                ready(p);
            }
        }
    }

    /**
     * Gets the ready queue list
     * @return
     */
    public ArrayList<Player> getQueueList() {
        return queue;
    }

    /**
     * Checks if the player is in the next wave queue
     * @param p
     * @return
     */
    public boolean inQueue(Player p) {
        return queue.contains(p);
    }

    /**
     * Returns the status of the vent
     * @return
     */
    public String getStatus() {
        if (MobEvent.getInstance().getCooldownManager().onCooldown()) {
            return ChatUtils.format(Messages.STATUS_COOLDOWN.get())
                    .replace("%time_second_cooldown%", String.valueOf(MobEvent.getInstance().getCooldownManager().getTimer() * 60))
                    .replace("%time_minutes_cooldown%", String.valueOf(MobEvent.getInstance().getCooldownManager().getTimer()));
        }
        else if (isStarted) {
            return ChatUtils.format(Messages.STATUS_STARTED.get());
        } else {
            return ChatUtils.format(Messages.STATUS_NOT_STARTED.get());
        }
    }
}
