package me.m1chelle99.foxiemc.helper;


import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;

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

	public static BlockPos getRandomPositionInLookAngle(Mob mob, float range) {
		var fX = mob.getRandom().nextFloat(range / -2F, range / 2F);
		var fZ = Math.sqrt(Math.pow(range, 2) - Math.pow(fX, 2));
		var iX = Math.round(fX);
		var iZ = (int) Math.round(fZ);
		var iY = mob.level.getHeight(Heightmap.Types.WORLD_SURFACE, iX, iZ);
		var pos = new BlockPos(iX, iY, iZ);
		var path = mob.getNavigation().createPath(pos, (int) range + 1);
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

	// TODO: To improve performance merge that search with the pathfinder
	// If the navigable pos is farer away then the already found, closer
	// point we don't need to look it up even more. Would save memory and perf. 
	public static ArrayList<BlockPos> getReachableBlocks(Mob mob, int range) {
		var navigation = mob.getNavigation();
		var node_evaluator = navigation.getNodeEvaluator();
		var destinations = new ArrayList<BlockPos>();

		var entity_x = Math.round(mob.getX());
		var entity_y = Math.round(mob.getY());
		var entity_z = Math.round(mob.getZ());

		for (var x = entity_x - range; x < entity_x + range; x++)
			for (var y = entity_y - range; y < entity_y + range; y++)
				for (var z = entity_z - range; z < entity_z + range; z++) {
					var position = new BlockPos(x, y, z);

					if (mob.level.canSeeSky(position)) continue;
					if (!navigation.isStableDestination(position)) continue;
					var type = node_evaluator
						.getBlockPathType(mob.level, (int) x, (int) y, (int) z);
					if (mob.getPathfindingMalus(type) < 0) continue;

					destinations.add(position);
				}

		return destinations;
	}
}
