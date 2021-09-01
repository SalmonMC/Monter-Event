package me.AmazeMC.mobevent.customentities;

import me.AmazeMC.mobevent.library.ItemBuilder;
import me.AmazeMC.mobevent.utils.ChatUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CraftZombie {

    private Zombie zombie;

    private String name;

    private double health;

    private int strength = 1;
    private int speed = 1;

    private Material mainHand;
    private Material offHand;

    private Material helmet;
    private Material chestplate;
    private Material leggings;
    private Material boots;

    public CraftZombie(Location loc) {
        this.zombie = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
    }

    public CraftZombie setName(String name) {
        this.name = ChatUtils.format(name);
        return this;
    }

    public CraftZombie setHealth(double health) {
        this.health = health;
        return this;
    }

    public CraftZombie setStrength(int strength) {
        this.strength = strength;
        return this;
    }

    public CraftZombie setMainHand(Material m) {
        this.mainHand = m;
        return this;
    }

    public CraftZombie setOffhand(Material m) {
        this.offHand = m;
        return this;
    }

    public CraftZombie setHelmet(Material m) {
        this.helmet = m;
        return this;
    }

    public CraftZombie setChestplate(Material m) {
        this.chestplate = m;
        return this;
    }

    public CraftZombie setLeggings(Material m) {
        this.leggings = m;
        return this;
    }

    public CraftZombie setBoots(Material m) {
        this.boots = m;
        return this;
    }

    public CraftZombie setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public Zombie build() {
        if (name != null) {
            zombie.setCustomNameVisible(true);
            zombie.setCustomName(name);
        }

        if (health > 0) {
            zombie.setMaxHealth(health);
            zombie.setHealth(health);
        }

        if (mainHand != null) {
            zombie.getEquipment().setItemInMainHand(new ItemBuilder(mainHand).build());
        }

        if (offHand != null) {
            zombie.getEquipment().setItemInOffHand(new ItemBuilder(offHand).build());
        }

        if (helmet != null) {
            zombie.getEquipment().setHelmet(new ItemBuilder(helmet).build());
        }

        if (chestplate != null) {
            zombie.getEquipment().setChestplate(new ItemBuilder(chestplate).build());
        }

        if (leggings != null) {
            zombie.getEquipment().setLeggings(new ItemBuilder(leggings).build());
        }

        if (boots != null) {
            zombie.getEquipment().setBoots(new ItemBuilder(boots).build());
        }

        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speed));
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, strength));

        return zombie;
    }
}
