package me.AmazeMC.mobevent.listeners;

import me.AmazeMC.mobevent.MobEvent;
import me.AmazeMC.mobevent.managers.SetupManager;
import me.AmazeMC.mobevent.filemanager.data.DataFile;
import me.AmazeMC.mobevent.filemanager.messages.Messages;
import me.AmazeMC.mobevent.utils.ChatUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SetupListener implements Listener {

    private int i = 0;

    @EventHandler
    public void onLeaveEvent(PlayerQuitEvent e) {
        SetupManager setupManager = MobEvent.getInstance().getSetupManager();

        if (setupManager.exists(e.getPlayer())) {
            setupManager.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerSetupEvent(PlayerInteractEvent e) {
        SetupManager setupManager = MobEvent.getInstance().getSetupManager();

        if (!setupManager.exists(e.getPlayer())) return;

        e.setCancelled(true);

        // Player phase
        if (setupManager.getPhase(e.getPlayer()) == 0) {
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                addSpawnpoint("player-spawnpoints", i++, e.getClickedBlock().getLocation(), e.getPlayer());
                return;
            }
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
                if (i == 0) {
                    e.getPlayer().sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NO_SPAWN_SELECTED.get()));
                    return;
                } else {
                    i = 0;
                    setupManager.nextPhase(e.getPlayer());
                    e.getPlayer().sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NEXT_PHASE_MOB.get()));
                }
                return;
            }
            return;
        }

        // Mob phase
        else if (setupManager.getPhase(e.getPlayer()) == 1) {
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                addSpawnpoint("mob-spawnpoints", i++, e.getClickedBlock().getLocation(), e.getPlayer());
                return;
            }
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
                if (i == 0) {
                    e.getPlayer().sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NO_SPAWN_SELECTED.get()));
                    return;
                } else {
                    i = 0;
                    setupManager.remove(e.getPlayer());
                    e.getPlayer().sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.FINISHED_SETUP.get()));
                }
                return;
            }

        }
    }

    private void addSpawnpoint(String path, int i, Location loc, Player p) {
        DataFile dataFile = MobEvent.getInstance().getDataFile();

        dataFile.getFile().set("mobevent." + path + "." + i + ".world", loc.getWorld().getName());
        dataFile.getFile().set("mobevent." + path + "." + i + ".x", loc.getX());
        dataFile.getFile().set("mobevent." + path + "." + i + ".y", loc.getY());
        dataFile.getFile().set("mobevent." + path + "." + i + ".z", loc.getZ());
        dataFile.getFile().set("mobevent." + path + "." + i + ".pitch", loc.getPitch());
        dataFile.getFile().set("mobevent." + path + "." + i + ".yaw", loc.getYaw());
        dataFile.saveConfig();

        p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.SPAWN_SELECTED.get())
                .replace("%x%", String.valueOf(loc.getX()))
                .replace("%y%", String.valueOf(loc.getY()))
                .replace("%z%", String.valueOf(loc.getZ()))
                .replace("%pitch%", String.valueOf(loc.getPitch()))
                .replace("%yaw%", String.valueOf(loc.getYaw()))
                .replace("%world%", loc.getWorld().getName()));
    }
}
