package me.AmazeMC.mobevent.managers;

import me.AmazeMC.mobevent.MobEvent;
import me.AmazeMC.mobevent.filemanager.messages.Messages;
import me.AmazeMC.mobevent.library.ItemBuilder;
import me.AmazeMC.mobevent.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class RewardManager {

    private ArrayList<String> rewards = new ArrayList<>();

    /**
     * Adds a reward to the list
     * @param item
     */
    public void addReward(String item) {
        rewards.add(item);
    }

    /**
     * Gives a player their rewards
     * @param p
     */
    public void giveReward(Player p) {
        boolean droppedItemMessage = false;

        p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.REWARDS_GIVE.get()));

        for (String item : rewards) {
            String[] meta = item.split(";");

            if (meta[0].equalsIgnoreCase("cmd")) {
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), ChatUtils.format(meta[1]).replace("%player%", p.getName()));
                p.sendMessage(ChatUtils.format(Messages.REWARDS_GIVE_LIST.get())
                        .replace("%item%", meta[2])
                        .replace("%amount%",  ""));
            } else {
                HashMap<Integer, ItemStack> dropItemList = p.getInventory().addItem(new ItemBuilder(Material.getMaterial(meta[0].toUpperCase())).setAmount(Integer.parseInt(meta[1])).build());
                p.sendMessage(ChatUtils.format(Messages.REWARDS_GIVE_LIST.get())
                        .replace("%item%", meta[0])
                        .replace("%amount%", meta[1] + " "));

                for (ItemStack dropItem : dropItemList.values()) {
                    p.getWorld().dropItemNaturally(p.getLocation(), dropItem);
                    if (droppedItemMessage == false) {
                        p.sendMessage(ChatUtils.format(MobEvent.getInstance()._PREFIX + Messages.DROPPED_ITEMS.get()));
                        droppedItemMessage = true;
                    }
                }
            }
        }
    }

    /**
     * Clears the rewards
     */
    public void clear() {
        rewards.clear();
    }
}
