package me.m1chelle99.foxiemc.helper;


import net.minecraft.world.entity.Mob;

public class EntityHelper {
    public int getRandomTicksWithin(Mob mob, float min_seconds, float max_seconds) {
        var min_ticks = Math.round(min_seconds * 20);
        var max_ticks = Math.round(max_seconds * 20);
        return mob.getRandom().nextInt(min_ticks, max_ticks);
    }
}
