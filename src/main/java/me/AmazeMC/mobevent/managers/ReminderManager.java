package me.AmazeMC.mobevent.managers;

import me.AmazeMC.mobevent.MobEvent;
import me.AmazeMC.mobevent.filemanager.messages.Messages;
import me.AmazeMC.mobevent.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ReminderManager {

    private BukkitTask task;

    /**
     * Starts the cooldown
     */
    public void startReminder() {
        if (MobEvent.getInstance().getConfig().getBoolean("event.announce-reminder")) {
            task = new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.broadcastMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.REMINDER_ANNOUNCE.get()));
                }
            }.runTaskTimerAsynchronously(MobEvent.getInstance(), (20 * 60) * MobEvent.getInstance().getConfig().getInt("event.announce-reminder-every"), (20 * 60) * MobEvent.getInstance().getConfig().getInt("event.announce-reminder-every"));
        }
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
    public void endReminder() {
        if (task != null) {
            task.cancel();
        }
    }
}
