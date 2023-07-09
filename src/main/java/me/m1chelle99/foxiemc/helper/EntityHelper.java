package me.m1chelle99.foxiemc.helper;


import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public final class EntityHelper {
    public static int getRandomTicksWithin(Mob mob, float min, float max) {
        var min_ticks = Math.round(min * 20);
        var max_ticks = Math.round(max * 20);
        return mob.getRandom().nextInt(min_ticks, max_ticks);
    }

    public static boolean playersInDistance(Mob mob, float distance) {
        var area = mob
            .getBoundingBox()
            .inflate(distance, distance / 2, distance);

        var selector = EntitySelector.NO_SPECTATORS;
        var list = mob.level.getEntitiesOfClass(Player.class, area, selector);

        return !list.isEmpty();
    }
}
