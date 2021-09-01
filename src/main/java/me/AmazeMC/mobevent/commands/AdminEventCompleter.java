package me.AmazeMC.mobevent.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class AdminEventCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> tabList = new ArrayList<>();
        tabList.add("setup");
        tabList.add("forcestart");
        tabList.add("forcestop");
        tabList.add("startcooldown");
        tabList.add("endcooldown");
        tabList.add("reload");
        return tabList;
    }
}
