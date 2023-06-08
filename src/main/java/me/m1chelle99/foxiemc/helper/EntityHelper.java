package me.m1chelle99.foxiemc.helper;


import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public final class EntityHelper {
    public static int getRandomTicksWithin(Mob mob, float min_seconds, float max_seconds) {
        var min_ticks = Math.round(min_seconds * 20);
        var max_ticks = Math.round(max_seconds * 20);
        return mob.getRandom().nextInt(min_ticks, max_ticks);
    }

    public static boolean playersInDistance(Mob mob, float distance) {
        var area = mob.getBoundingBox().inflate(distance, distance / 2, distance);
        var list = mob.level.getEntitiesOfClass(Player.class, area, EntitySelector.NO_SPECTATORS);
        return !list.isEmpty();
    }
}
