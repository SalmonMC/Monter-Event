package me.AmazeMC.mobevent.utils;

import me.AmazeMC.mobevent.MobEvent;
import me.AmazeMC.mobevent.library.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class GUIUtils {

    /**
     * Gets the GUI from the config
     * @param id
     * @return
     */
    public static Inventory getGUI(String id) {
        String invName = MobEvent.getInstance().getEventGUIFile().getFile().getString(id + ".name");
        int invSize = MobEvent.getInstance().getEventGUIFile().getFile().getInt(id + ".size");
        Inventory inv = Bukkit.getServer().createInventory(null, invSize, invName);
        loadItems(id, inv);
        return inv;
    }

    /**
     * Gets the GUI from the config
     * @param id
     * @return
     */
    public static Inventory getGUI(String id, Player p) {
        String invName = MobEvent.getInstance().getEventGUIFile().getFile().getString(id + ".name");
        int invSize = MobEvent.getInstance().getEventGUIFile().getFile().getInt(id + ".size");
        Inventory inv = Bukkit.getServer().createInventory(p, invSize, invName);
        loadItems(id, inv);
        return inv;
    }

    /**
     * Loads all items from the config
     * @param id
     * @param inv
     */
    private static void loadItems(String id, Inventory inv) {
        // Blanks
        for (int i = 0; i < inv.getSize(); i++) {
            if (MobEvent.getInstance().getEventGUIFile().getFile().get(id + ".slots.blank_slots") != null) {
                inv.setItem(i, readItem(id, "blank_slots", inv));
            } else {
                break;
            }
        }

        // Items
        for (int i = 0; i < inv.getSize(); i++) {
            if (MobEvent.getInstance().getEventGUIFile().getFile().get(id + ".slots." + i) != null) {
                inv.setItem(MobEvent.getInstance().getEventGUIFile().getFile().getInt(id + ".slots." + i + ".slot"), readItem(id, String.valueOf(i), inv));
            }
        }
    }

    /**
     * Builds the item from the config
     * @param id
     * @param slot
     * @return
     */
    private static ItemStack readItem(String id, String slot, Inventory inv) {
        Material item;
        String owner = null;

        // Material & Skull
        if (MobEvent.getInstance().getEventGUIFile().getFile().getString(id + ".slots." + slot + ".item").contains(":")) {
            String[] itemMeta = MobEvent.getInstance().getEventGUIFile().getFile().getString(id + ".slots." + slot + ".item").split(":");
            item = Material.LEGACY_SKULL_ITEM;
        } else {
            item = Material.getMaterial(MobEvent.getInstance().getEventGUIFile().getFile().getString(id + ".slots." + slot + ".item"));
        }

        ItemBuilder builder = new ItemBuilder(item);

        // Amount
        if (MobEvent.getInstance().getEventGUIFile().getFile().get(id + ".slots." + slot + ".amount") != null) {
            builder.setAmount(MobEvent.getInstance().getEventGUIFile().getFile().getInt(id + ".slots." + slot + ".amount"));
        }

        // Display name
        if (MobEvent.getInstance().getEventGUIFile().getFile().get(id + ".slots." + slot + ".name") != null) {
            builder.setName(ChatUtils.format(MobEvent.getInstance().getEventGUIFile().getFile().getString(id + ".slots." + slot + ".name")));
        }

        // Lore
        if (MobEvent.getInstance().getEventGUIFile().getFile().get(id + ".slots." + slot + ".lore") != null) {
            builder.setLore(MobEvent.getInstance().getEventGUIFile().getFile().getStringList(id + ".slots." + slot + ".lore"));
        }

        // Glow
        if (MobEvent.getInstance().getEventGUIFile().getFile().get(id + ".slots." + slot + ".glow") != null) {
            builder.setGlow(MobEvent.getInstance().getEventGUIFile().getFile().getBoolean(id + ".slots." + slot + ".glow"));
        }

        // Action
        if (MobEvent.getInstance().getEventGUIFile().getFile().get(id + ".slots." + slot + ".action") != null) {
            builder.setPersistentData("action", MobEvent.getInstance().getEventGUIFile().getFile().getString(id + ".slots." + slot + ".action"));

            Player p = (Player) inv.getHolder();
            if (p != null) {
                // Glow rule
                if (MobEvent.getInstance().getQueueManager().exists(p)) {
                    if (MobEvent.getInstance().getEventGUIFile().getFile().getString(id + ".slots." + slot + ".action").equalsIgnoreCase("join_queue")) {
                        builder.setGlow(true);
                    }
                }

                // Queue skull rule
                if (MobEvent.getInstance().getQueueManager().getCount() > 0 || MobEvent.getInstance().getEventManager().getCount() > 0) {
                    if (MobEvent.getInstance().getEventGUIFile().getFile().getString(id + ".slots." + slot + ".action").contains("queue_")) {
                        String[] itemMeta = MobEvent.getInstance().getEventGUIFile().getFile().getString(id + ".slots." + slot + ".action").split("_");
                        int index = Integer.parseInt(itemMeta[1]) - 1;

                        if (MobEvent.getInstance().getEventManager().hasStarted()) {
                            int j = 0;
                            for (Player eP : MobEvent.getInstance().getEventManager().getList().keySet()) {
                                if (j == index) {
                                    owner = eP.getName();
                                }

                                j++;
                            }
                        }
                        else if (MobEvent.getInstance().getQueueManager().getCount() >= Integer.parseInt(itemMeta[1])) {
                            owner = MobEvent.getInstance().getQueueManager().get(index).getName();

                            if (MobEvent.getInstance().getEventGUIFile().getFile().get(id + ".slots." + slot + ".takenName") != null) {
                                builder.setName(MobEvent.getInstance().getEventGUIFile().getFile().getString(id + ".slots." + slot + ".takenName")
                                        .replace("%player%", MobEvent.getInstance().getQueueManager().get(index).getName()));
                            }
                            if (MobEvent.getInstance().getEventGUIFile().getFile().get(id + ".slots." + slot + ".takenLore") != null) {
                                ArrayList<String> rLore = new ArrayList<String>();
                                for (String line : MobEvent.getInstance().getEventGUIFile().getFile().getStringList(id + ".slots." + slot + ".takenLore")) {
                                    line = line.replace("%player%", MobEvent.getInstance().getQueueManager().get(index).getName());
                                    rLore.add(line);
                                }
                                builder.setLore(rLore);
                            }
                        }
                    }
                }
            }
        }

        if (owner != null) {
            builder.setOwner(owner);
            return builder.build(true);
        } else {
            return builder.build();
        }
    }
}
