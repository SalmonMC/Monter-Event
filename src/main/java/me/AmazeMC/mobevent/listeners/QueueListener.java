package me.AmazeMC.mobevent.listeners;

import me.AmazeMC.mobevent.MobEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QueueListener implements Listener {

    @EventHandler
    public void onLeaveEvent(PlayerQuitEvent e) {
        if (MobEvent.getInstance().getQueueManager().exists(e.getPlayer())) {
            MobEvent.getInstance().getQueueManager().leave(e.getPlayer());
        }
    }
}
