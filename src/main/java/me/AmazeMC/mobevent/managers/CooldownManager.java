package me.AmazeMC.mobevent.managers;

import me.AmazeMC.mobevent.MobEvent;
import me.AmazeMC.mobevent.filemanager.messages.Messages;
import me.AmazeMC.mobevent.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CooldownManager {

    private boolean hasCooldown = false;
    private int timer = 0;
    private BukkitTask task;

    /**
     * Starts the cooldown
     */
    public void startCooldown() {
        hasCooldown = true;
        timer = MobEvent.getInstance().getConfig().getInt("event.event-cooldown");

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!hasCooldown) {
                    this.cancel();
                }

                if (timer <= 0) {
                    endCooldown();

                    // Event is full and can start
                    if (MobEvent.getInstance().getQueueManager().getCount() >= MobEvent.getInstance().getConfig().getInt("event.max-players")) {
                        MobEvent.getInstance().getEventManager().start(MobEvent.getInstance().getConfig().getInt("event.time-start-event"));

                        // Sends a starting message to everyone in the event
                        for (Player eP : MobEvent.getInstance().getQueueManager().getList()) {
                            eP.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.EVENT_STARTING.get())
                                    .replace("%time_seconds%", String.valueOf(MobEvent.getInstance().getConfig().getInt("event.time-start-event")))
                                    .replace("%time_minutes%", String.valueOf(MobEvent.getInstance().getConfig().getInt("event.time-start-event") / 60)));
                        }
                    } else {
                        MobEvent.getInstance().getReminderManager().startReminder();
                    }

                    if (MobEvent.getInstance().getConfig().getBoolean("announce-end-cooldown-in-chat")) {
                        Bukkit.broadcastMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.ANNOUNCE_END_COOLDOWN.get()));
                    }

                    this.cancel();
                }

                timer--;
            }
        }.runTaskTimerAsynchronously(MobEvent.getInstance(), 0, 20 * 60);
    }

    /**
     * Gets the cooldown timer
     * @return
     */
    public int getTimer() {
        return timer;
    }

    /**
     * Gets the cooldown schedulers
     * @return
     */
    public BukkitTask getTask() {
        return task;
    }

    /**
     * Ends the cooldown
     */
    public void endCooldown() {
        hasCooldown = false;

        if (task != null) {
            task.cancel();
        }
    }

    /**
     * Checks if the event is on cooldown
     * @return
     */
    public boolean onCooldown() {
        return hasCooldown;
    }
}
