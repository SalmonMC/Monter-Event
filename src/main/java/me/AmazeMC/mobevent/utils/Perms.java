package me.AmazeMC.mobevent.utils;

import org.bukkit.command.CommandSender;

public class Perms {

    public static boolean isAuthorized(CommandSender sender, String permission) {
        return sender.hasPermission("event.*") || sender.hasPermission(permission);
    }
}
