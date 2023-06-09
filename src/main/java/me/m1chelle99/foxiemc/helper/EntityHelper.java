package me.m1chelle99.foxiemc.helper;


import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;

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

    public static BlockPos getRandomPositionInLookAngle(Mob entity, float distance) {
        var float_x = entity.getRandom().nextFloat(distance / -2F, distance / 2F);
        var float_z = Math.sqrt(Math.pow(distance, 2) - Math.pow(float_x, 2));
        var int_x = Math.round(float_x);
        var int_z = (int) Math.round(float_z);
        var int_y = entity.level.getHeight(Heightmap.Types.WORLD_SURFACE, int_x, int_z);
        var pos = new BlockPos(int_x, int_y, int_z);
        var path = entity.getNavigation().createPath(pos, (int) distance + 1);
        if (path == null) return null;
        if (path.canReach()) return null;
        return pos;
    }

    public static BlockPos getRoundedBlockPos(Entity entity) {
        var x = Math.round(entity.getX());
        var y = Math.round(entity.getY());
        var z = Math.round(entity.getZ());
        return new BlockPos(x, y, z);
    }

    public static ArrayList<BlockPos> getReachableBlocks(Mob entity, int range) {
        var navigation = entity.getNavigation();
        var node_evaluator = navigation.getNodeEvaluator();
        var destinations = new ArrayList<BlockPos>();

        var entity_x = Math.round(entity.getX());
        var entity_y = Math.round(entity.getY());
        var entity_z = Math.round(entity.getZ());

        for (var x = entity_x - range; x < entity_x + range; x++)
            for (var y = entity_y - range; y < entity_y + range; y++)
                for (var z = entity_z - range; z < entity_z + range; z++) {
                    var position = new BlockPos(x, y, z);

                    if (entity.level.canSeeSky(position)) continue;
                    if (!navigation.isStableDestination(position)) continue;
                    var type = node_evaluator.getBlockPathType(entity.level, (int) x, (int) y, (int) z);
                    if (entity.getPathfindingMalus(type) < 0) continue;

                    destinations.add(position);
                }

        return destinations;
    }
}
