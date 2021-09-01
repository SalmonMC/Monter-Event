package me.AmazeMC.mobevent.listeners;

import me.AmazeMC.mobevent.MobEvent;
import me.AmazeMC.mobevent.filemanager.messages.Messages;
import me.AmazeMC.mobevent.utils.ChatUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class GUIListener implements Listener {

    @EventHandler
    public void onClickGui(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getCurrentItem() == null) return;

        if (e.getView().getTitle().equalsIgnoreCase(MobEvent.getInstance().getEventGUIFile().getFile().getString("event_inventory.name"))
            || e.getView().getTitle().equalsIgnoreCase(MobEvent.getInstance().getEventGUIFile().getFile().getString("next_wave_inventory.name"))) {
            e.setCancelled(true);

            if (!(e.getWhoClicked() instanceof Player)) {
                e.getWhoClicked().sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NO_PLAYER.get()));
                return;
            }

            Player p = (Player) e.getWhoClicked();

            String persistentData = (String) e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(MobEvent.getInstance(), "action"), PersistentDataType.STRING);
            if (persistentData != null) {
                if (persistentData.equalsIgnoreCase("join_queue")) {
                    p.performCommand("event join");
                }
                else if (persistentData.equalsIgnoreCase("leave_queue")) {
                    p.performCommand("event leave");
                }
                else if (persistentData.equalsIgnoreCase("ready_next_wave")) {
                    MobEvent.getInstance().getEventManager().ready(p);
                }
                else if (persistentData.equalsIgnoreCase("leave_event")) {
                    MobEvent.getInstance().getEventManager().kickPlayer(p, true);
                    p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.EVENT_QUIT_SELF.get()));
                }

                p.closeInventory();
                return;
            }
        }
    }

    @EventHandler
    public void onCloseInventoryEvent(InventoryCloseEvent e) {
        if (e.getInventory() == null) return;

        if (e.getView().getTitle().equalsIgnoreCase(MobEvent.getInstance().getEventGUIFile().getFile().getString("next_wave_inventory.name"))) {
            if (!(e.getPlayer() instanceof Player)) {
                return;
            }

            Player p = (Player) e.getPlayer();

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (MobEvent.getInstance().getEventManager().inQueue(p)) {
                            p.openInventory(e.getInventory());
                        }
                    }
                }.runTaskLater(MobEvent.getInstance(), 1);
        }
    }
}
