package me.AmazeMC.mobevent.customentities;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

public class CraftEntity extends EntityCreature {

    public CraftEntity(EntityTypes<? extends EntityCreature> type, Location loc) {
        super(type, ((CraftWorld) loc.getWorld()).getHandle());
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
    }
}
