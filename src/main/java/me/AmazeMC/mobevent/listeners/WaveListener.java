package me.AmazeMC.mobevent.listeners;

import me.AmazeMC.mobevent.MobEvent;
import me.AmazeMC.mobevent.filemanager.messages.Messages;
import me.AmazeMC.mobevent.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class WaveListener implements Listener {

    @EventHandler
    public void onEventEntityDeathEvent(EntityDeathEvent e) {
        if (e.getEntity().getMetadata("isEventMob").size() > 0) {
            if (Boolean.parseBoolean(e.getEntity().getMetadata("isEventMob").get(0).value().toString())) {
                MobEvent.getInstance().getWaveManager().incrementEntitiesCountLive();

                double entitiesCount = MobEvent.getInstance().getWaveManager().getEntitiesCount();
                double completion = MobEvent.getInstance().getWaveManager().getCompletion() / 100;
                double toKillCount = entitiesCount * completion;

                if (MobEvent.getInstance().getWaveManager().getEntitiesCountLive() >= toKillCount) {
                    MobEvent.getInstance().getWaveManager().clearedCurrentWave();

                    if (MobEvent.getInstance().getWaveManager().hasNextWave()) {
                        for (Player p : MobEvent.getInstance().getEventManager().getList().keySet()) {
                            p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.CLEARED_WAVE.get())
                                    .replace("%wave%", String.valueOf((MobEvent.getInstance().getWaveManager().getCurrentWave() + 1))));
                            MobEvent.getInstance().getEventManager().addQueue(p);
                            MobEvent.getInstance().getWaveManager().openNextWaveGui(p);
                        }

                        new BukkitRunnable() {
                            int timer = MobEvent.getInstance().getConfig().getInt("event.wave-deciding-time");
                            @Override
                            public void run() {
                                if (MobEvent.getInstance().getEventManager().getQueueList().size() <= 0) {
                                    this.cancel();
                                }

                                else if (timer > 0 && timer <= 5) {
                                    for (Player p : MobEvent.getInstance().getEventManager().getQueueList()) {
                                        p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.WAVE_DECIDING_TIME.get()).replace("%time_seconds%", String.valueOf(timer)));
                                    }
                                }

                                else if (timer <= 0) {
                                    MobEvent.getInstance().getEventManager().readyAll();
                                    this.cancel();
                                }

                                timer--;
                            }
                        }.runTaskTimer(MobEvent.getInstance(), 0, 20);
                    } else {
                        for (Player p : MobEvent.getInstance().getEventManager().getList().keySet()) {
                            ChatUtils.sendTitleOrChat(p, ChatUtils.format(Messages.CLEARED_ALL_WAVES.get()), true, 10, 50, 10);
                            p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.TELEPORTED_BACK.get()));
                        }
                        if (MobEvent.getInstance().getConfig().getBoolean("event.announce-event-finished-in-chat")) {
                            Bukkit.getServer().broadcastMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.WAVE_CLEARED_GLOBAL.get())
                                    .replace("%wave%", String.valueOf((MobEvent.getInstance().getWaveManager().getCurrentWave() + 1))));
                        }
                        MobEvent.getInstance().getEventManager().forceStop(true);
                        MobEvent.getInstance().getWaveManager().reset();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEventEntityDamageEntityEvent(EntityTargetEvent e) {
        if (e.getTarget() == null) return;

        if (e.getTarget().getMetadata("isEventMob").size() > 0) {
            if (Boolean.parseBoolean(e.getTarget().getMetadata("isEventMob").get(0).value().toString())) {
                e.setCancelled(true);

                Creature creature = (Creature) e.getEntity();
//                creature.setTarget(MobEvent.getInstance().getEventManager().getRandomPlayer());
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onCreatureSpawnEvent(CreatureSpawnEvent e) {
//        if (e.getEntity().getWorld().getName().equalsIgnoreCase(MobEvent.getInstance().getConfig().getString("event.override-worldguard-world"))) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getServer().getWorld(e.getEntity().getWorld().getName()).spawnEntity(e.getLocation(), e.getEntityType());
                }
            }.runTaskLater(MobEvent.getInstance(),  1);
        }
//    }
}
