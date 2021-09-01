package me.AmazeMC.mobevent.commands;

import me.AmazeMC.mobevent.MobEvent;
import me.AmazeMC.mobevent.filemanager.messages.Messages;
import me.AmazeMC.mobevent.utils.ChatUtils;
import me.AmazeMC.mobevent.utils.GUIUtils;
import me.AmazeMC.mobevent.utils.Perms;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventCommand implements CommandExecutor {

    MobEvent plugin = MobEvent.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NO_PLAYER.get()));
            return true;
        }

        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("event")) {
            if (MobEvent.getInstance().getSetupManager().exists(p)) {
                p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.PLAYER_SETUP_MODE.get()));
                return true;
            }

            if (!Perms.isAuthorized(p, "event.use")) {
                p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NO_PERMISSION.get()));
                return true;
            }

            if (args.length == 0) {
                p.openInventory(GUIUtils.getGUI("event_inventory", p));
                return true;
            }

            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("join")) {
                    // Checks if you're already in an event
                    if (plugin.getEventManager().exists(p)) {
                        p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.EVENT_ALREADY_JOIN.get()));
                        return true;
                    }

                    // Checks if event is full
                    if (plugin.getQueueManager().getCount() >= plugin.getConfig().getInt("event.max-players")) {
                        p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.EVENT_FULL.get())
                                .replace("%max_players%", plugin.getConfig().getString("event.max-players")));
                        return true;
                    }

                    // Checks if you're already in the event
                    if (plugin.getQueueManager().exists(p)) {
                        p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.EVENT_ALREADY_JOIN.get()));
                        return true;
                    }

                    plugin.getQueueManager().join(p);
                    p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.EVENT_JOIN.get()));

                    if (MobEvent.getInstance().getConfig().getBoolean("event.broadcast-join-message")) {
                        Bukkit.getServer().broadcastMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.BROADCAST_JOIN_MESSAGE.get())
                                .replace("%player%", p.getName()));
                    }

                    // Event is full and can start
                    if (plugin.getQueueManager().getCount() >= plugin.getConfig().getInt("event.max-players")
                        && !plugin.getCooldownManager().onCooldown()) {
                        plugin.getEventManager().start(plugin.getConfig().getInt("event.time-start-event"));
                        MobEvent.getInstance().getReminderManager().endReminder();

                        // Sends a starting message to everyone in the event
                        for (Player eP : plugin.getQueueManager().getList()) {
                            ChatUtils.sendTitleOrChat(eP, ChatUtils.format(Messages.EVENT_STARTING.get())
                                    .replace("%time_seconds%", String.valueOf(plugin.getConfig().getInt("event.time-start-event")))
                                    .replace("%time_minutes%", String.valueOf(plugin.getConfig().getInt("event.time-start-event") / 60)), true, 10, 70, 10);
                        }
                        return true;
                    }
                    return true;
                }

                if (args[0].equalsIgnoreCase("leave")) {
                    // Checks if you're already in the event
                    if (!plugin.getQueueManager().exists(p)) {
                        p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.EVENT_ALREADY_LEAVE.get()));
                        return true;
                    }

                    plugin.getQueueManager().leave(p);
                    p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.EVENT_LEAVE.get()));
                    return true;
                } else {
                    p.performCommand("event");
                }
            }
        }
        return true;
    }
}
