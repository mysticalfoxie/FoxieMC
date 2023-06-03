package me.m1chelle99.foxiemc.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.Level;

public class Foxie extends Fox {
    public static final String ID = "foxie";

    public Foxie(EntityType<? extends Fox> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder getFoxieAttributes() {
        return Mob
                .createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 4.0F)
                .add(Attributes.MOVEMENT_SPEED, 1.2F);
    }
}
