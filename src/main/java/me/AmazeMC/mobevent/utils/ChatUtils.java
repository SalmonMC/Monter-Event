package me.AmazeMC.mobevent.utils;

import me.AmazeMC.mobevent.MobEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtils {

    /**
     * Formats string with color
     *
     * @param s - String to format
     * @return String
     */
    public static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Sends title or chat messages depends on config option
     * @param p
     * @param s
     */
    public static void sendTitleOrChat(Player p, String s, boolean withSubtitle, int fadeIn, int stay, int fadeOut) {
        if (MobEvent.getInstance().getConfig().getBoolean("event.use-titles")) {
            if (withSubtitle) {
                p.sendTitle(ChatUtils.format(MobEvent.getInstance().getConfig().getString("event.title-header")), ChatUtils.format(s), fadeIn, stay, fadeOut);
            } else {
                p.sendTitle(ChatUtils.format(s), "", 10, 70, 20);
            }
        } else {
            p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + s));
        }
    }
}
