package me.AmazeMC.mobevent.listeners;

import me.AmazeMC.mobevent.MobEvent;
import me.AmazeMC.mobevent.filemanager.messages.Messages;
import me.AmazeMC.mobevent.utils.ChatUtils;
import me.AmazeMC.mobevent.utils.Perms;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class EventListener implements Listener {

    @EventHandler
    public void onPerformCommandsEvent(PlayerCommandPreprocessEvent e) {
        if (MobEvent.getInstance().getQueueManager().exists(e.getPlayer())) {
            if (!Perms.isAuthorized(e.getPlayer(), "event.bypass.command")) {
                e.getPlayer().sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.COMMANDS_NOT_ALLOWED.get())
                        .replace("%command%", e.getMessage()));
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (MobEvent.getInstance().getEventManager().exists(p)) {
                MobEvent.getInstance().getEventManager().executeDeath(p);
                p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.PLAYER_DIED.get()));

                for (Player eP : MobEvent.getInstance().getEventManager().getList().keySet()) {
                    eP.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.PLAYER_DIED_OTHERS.get())
                            .replace("%player%", p.getName()));
                }

                if (MobEvent.getInstance().getConfig().getBoolean("event.drop-items-on-death") == false) {
                    e.getDrops().clear();
                }
            }
        }
    }
}
