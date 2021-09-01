package me.AmazeMC.mobevent.library;

import me.AmazeMC.mobevent.MobEvent;
import me.AmazeMC.mobevent.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    protected Material material;
    protected int amount = 1;
    protected short durability;
    protected String owner;

    protected String persistentDataKey;
    protected String persistentDataValue;

    protected String name;
    protected List<String> lore = new ArrayList<>();

    protected boolean hasGlow = false;

    public ItemBuilder(Material material) {
        this.material = material;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        this.durability = durability;
        return this;
    }

    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder setLore(String... lines) {
        for(String line : lines) {
            lore.add(ChatUtils.format(line)
                    .replace("%status%", MobEvent.getInstance().getEventManager().getStatus())
                    .replace("%in_queue%", String.valueOf(MobEvent.getInstance().getQueueManager().getCount()))
                    .replace("%max_queue%", String.valueOf(MobEvent.getInstance().getConfig().getInt("event.max-players"))));
        }
        return this;
    }

    public ItemBuilder setLore(List<String> lines) {
        for(String line : lines) {
            lore.add(ChatUtils.format(line)
                    .replace("%status%", MobEvent.getInstance().getEventManager().getStatus())
                    .replace("%in_queue%", String.valueOf(MobEvent.getInstance().getQueueManager().getCount()))
                    .replace("%max_queue%", String.valueOf(MobEvent.getInstance().getConfig().getInt("event.max-players"))));
        }
        return this;
    }

    public ItemBuilder replaceInLore(String regular, String replacement) {
        for(int i = 0; i < lore.size(); i++) {
            lore.set(i, ChatUtils.format(lore.get(i).replace(regular, replacement)));
        }
        return this;
    }

    public ItemBuilder addLore(String line) {
        lore.add(line);
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder setGlow(boolean value) {
        this.hasGlow = value;
        return this;
    }

    public ItemBuilder setPersistentData(String key, String value) {
        this.persistentDataKey = key;
        this.persistentDataValue = value;
        return this;
    }

    public ItemBuilder setOwner(String ownerName) {
        this.owner = ownerName;
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, amount);

        if (durability > 0) {
            itemStack.setDurability(durability);
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        if(name != null) {
            itemMeta.setDisplayName(ChatUtils.format(name)
                    .replace("%status%", MobEvent.getInstance().getEventManager().getStatus())
                    .replace("%in_queue%", String.valueOf(MobEvent.getInstance().getQueueManager().getCount()))
                    .replace("%max_queue%", String.valueOf(MobEvent.getInstance().getConfig().getInt("event.max-players"))));
        }

        if(lore.size() > 0) {
            itemMeta.setLore(lore);
        }

        if (hasGlow) {
            Glow glow = new Glow(new NamespacedKey(MobEvent.getInstance(), "0"));
            itemMeta.addEnchant(glow, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (persistentDataKey != null) {
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(MobEvent.getInstance(), persistentDataKey), PersistentDataType.STRING, persistentDataValue);
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public ItemStack build(boolean isSkull) {
        ItemStack itemStack = new ItemStack(Material.LEGACY_SKULL_ITEM, amount, (short) SkullType.PLAYER.ordinal());

        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();

        if (owner != null) {
            itemMeta.setOwner(owner);
        }

        if (name != null) {
            itemMeta.setDisplayName(ChatUtils.format(name)
                    .replace("%status%", MobEvent.getInstance().getEventManager().getStatus())
                    .replace("%in_queue%", String.valueOf(MobEvent.getInstance().getQueueManager().getCount()))
                    .replace("%max_queue%", String.valueOf(MobEvent.getInstance().getConfig().getInt("event.max-players"))));
        }

        if(lore.size() > 0) {
            itemMeta.setLore(lore);
        }

        if (hasGlow) {
            Glow glow = new Glow(new NamespacedKey(MobEvent.getInstance(), "0"));
            itemMeta.addEnchant(glow, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (persistentDataKey != null) {
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(MobEvent.getInstance(), persistentDataKey), PersistentDataType.STRING, persistentDataValue);
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
