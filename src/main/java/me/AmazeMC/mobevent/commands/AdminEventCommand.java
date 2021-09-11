package me.AmazeMC.mobevent.commands;

import me.AmazeMC.mobevent.MobEvent;
import me.AmazeMC.mobevent.filemanager.FileManager;
import me.AmazeMC.mobevent.filemanager.messages.Messages;
import me.AmazeMC.mobevent.utils.ChatUtils;
import me.AmazeMC.mobevent.utils.Perms;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminEventCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("adminevent")) {
            if (sender instanceof Player) {
                if (MobEvent.getInstance().getSetupManager().exists((Player) sender)) {
                    sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.PLAYER_SETUP_MODE.get()));
                    return true;
                }
            }

            if (args.length == 0) {
                if (!Perms.isAuthorized(sender, "event.admin.help")) {
                    sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NO_PERMISSION.get()));
                    return true;
                }

                sendHelpMessage(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (!Perms.isAuthorized(sender, "event.reload")) {
                    sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NO_PERMISSION.get()));
                    return true;
                }
                FileManager.reloadAll();

                for (Player p : MobEvent.getInstance().getEventManager().getList().keySet()) {
                    p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + tMessages.EVENT_RELOADED.get()));
                }

                for (Player p : MobEvent.getInstance().getQueueManager().getList()) {
                    p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.EVENT_RELOADED_QUEUE.get()));
                }

                MobEvent.getInstance().getQueueManager().clear();
                MobEvent.getInstance().getEventManager().forceStop(true);
                MobEvent.getInstance().getWaveManager().reset();
                MobEvent.getInstance().getMonsterManager().stop();

                sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.RELOADED_FILES.get()));
            }
            else if (args[0].equalsIgnoreCase("setup")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NO_PLAYER.get()));
                    return true;
                }

                Player p = (Player) sender;

                if (!Perms.isAuthorized(p, "event.setup")) {
                    p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NO_PERMISSION.get()));
                    return true;
                }

                MobEvent.getInstance().getSetupManager().startSetup(p);
                p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.STARTED_SETUP.get()));
                p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NEXT_PHASE_PLAYER.get()));
            }
            else if (args[0].equalsIgnoreCase("forcestop")) {
                if (!Perms.isAuthorized(sender, "event.forcestop")) {
                    sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NO_PERMISSION.get()));
                    return true;
                }

                if (MobEvent.getInstance().getEventManager().hasStarted() == false) {
                    sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.EVENT_FORCESTOP_FAILED.get()));
                    return true;
                }

                MobEvent.getInstance().getEventManager().forceStop(true);
                MobEvent.getInstance().getWaveManager().reset();
                sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.FORCESTOP.get()));
            }
            else if (args[0].equalsIgnoreCase("forcestart")) {
                if (!Perms.isAuthorized(sender, "event.forcestart")) {
                    sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NO_PERMISSION.get()));
                    return true;
                }

                if (MobEvent.getInstance().getEventManager().hasStarted()) {
                    sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.EVENT_FORCESTART_FAILED.get()));
                    return true;
                }

                if (MobEvent.getInstance().getQueueManager().getCount() <= 0) {
                    sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.FORCESTART_NOT_FULL.get()));
                    return true;
                }

                MobEvent.getInstance().getEventManager().forceStart();
                MobEvent.getInstance().getReminderManager().endReminder();
                sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.FORCESTART.get()));
            }
            else if (args[0].equalsIgnoreCase("startcooldown")) {
                if (!Perms.isAuthorized(sender, "event.startcooldown")) {
                    sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NO_PERMISSION.get()));
                    return true;
                }

                if (MobEvent.getInstance().getEventManager().hasStarted()) {
                    sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.EVENT_FORCESTART_FAILED.get()));
                    return true;
                }

                if (MobEvent.getInstance().getQueueManager().getCount() >= MobEvent.getInstance().getConfig().getInt("event.max-players")) {
                    sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.QUEUE_FULL_COOLDOWN.get()));
                    return true;
                }

                if (MobEvent.getInstance().getCooldownManager().onCooldown()) {
                    sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.ALREADY_COOLDOWN.get()));
                    return true;
                }

                MobEvent.getInstance().getCooldownManager().startCooldown();
                MobEvent.getInstance().getReminderManager().endReminder();
                sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.STARTED_COOLDOWN.get()));
            }
            else if (args[0].equalsIgnoreCase("endcooldown")) {
                if (!Perms.isAuthorized(sender, "event.endcooldown")) {
                    sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NO_PERMISSION.get()));
                    return true;
                }

                if (!MobEvent.getInstance().getCooldownManager().onCooldown()) {
                    sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NO_COOLDOWN.get()));
                    return true;
                }

                MobEvent.getInstance().getCooldownManager().endCooldown();
                sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.ENDED_COOLDOWN.get()));

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
            }
            else {
                if (!Perms.isAuthorized(sender, "event.admin.help")) {
                    sender.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.NO_PERMISSION.get()));
                    return true;
                }

                sendHelpMessage(sender);
            }
            return true;
        }
        return true;
    }

    public void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatUtils.format(""));
        sender.sendMessage(ChatUtils.format("&eMobEvent &7help menu:"));
        sender.sendMessage(ChatUtils.format("&7&m---------------"));
        sender.sendMessage(ChatUtils.format("&e/adminevent reload &7- Reloads all the config files."));
        sender.sendMessage(ChatUtils.format("&e/adminevent setup &7- Starts the setup for the event."));
        sender.sendMessage(ChatUtils.format("&e/adminevent forcestop &7- Stops the event forcefully."));
        sender.sendMessage(ChatUtils.format("&e/adminevent forcestart &7- Starts the event forcefully."));
        sender.sendMessage(ChatUtils.format("&e/adminevent startcooldown &7- Starts the cooldown for the event."));
        sender.sendMessage(ChatUtils.format("&e/adminevent endcooldown &7- Ends the cooldown for the event."));
        sender.sendMessage(ChatUtils.format("&7&m---------------"));
        sender.sendMessage(ChatUtils.format(""));
    }
}
