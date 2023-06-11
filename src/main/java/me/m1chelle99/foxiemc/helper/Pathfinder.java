package me.m1chelle99.foxiemc.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public final class Pathfinder {
	public static BlockPos getClosestPathWhere(
		@NotNull Mob entity,
		int xzRange,
		int yRange,
		Predicate<BlockPos> predicate
	) {
		var current = entity.blockPosition();
		var navigation = entity.getNavigation();
		var evaluator = navigation.getNodeEvaluator();
		var level = entity.level;

		BlockPos target = null;
		double targetRange = Double.MAX_VALUE;

		var cx = current.getX();
		var cy = current.getY();
		var cz = current.getZ();

		for (var y = cy - yRange; y < cy + yRange; y++)
			for (var x = cx - xzRange; x < cx + xzRange; x++)
				for (var z = cz - xzRange; z < cz + xzRange; z++) {
					var i = new BlockPos(x, y, z);

					if (!navigation.isStableDestination(i)) continue;
					if (!predicate.test(i)) continue;

					var dist_manhattan = current.distManhattan(i);
					if (dist_manhattan > targetRange) continue;
					if (dist_manhattan == Math.floor(targetRange)) {
						var dist_sqrt = current.distToCenterSqr(x, y, z);
						if (dist_sqrt > targetRange) continue;
						targetRange = dist_sqrt;
					}

					var type = evaluator.getBlockPathType(level, x, y, z);
					if (entity.getPathfindingMalus(type) < 0) continue;

					target = i;
				}

		return target;
	}

	public static BlockPos getPathInLookDirection(
		@NotNull Mob entity,
		int xzRange,
		int yRange,
		int randomModifier
	) {
		var position = RandomPos.generateRandomDirectionWithinRadians(
			entity.getRandom(),
			xzRange, yRange, 0,
			entity.getRotationVector().x,
			entity.getRotationVector().y,
			Mth.HALF_PI);

		if (position == null)
			return null;

		var min = randomModifier * -1;
		var random = entity.getRandom();
		var x = position.getX() + random.nextInt(min, randomModifier);
		var z = position.getZ() + random.nextInt(min, randomModifier);

		return new BlockPos(x, position.getY(), z);
	}

	public static BlockPos getRandomPositionWithin(
		@NotNull PathfinderMob entity,
		int xzRange,
		int yRange,
		int retries
	) {
		for (int i = 0; i < retries; i++) {
			var target = DefaultRandomPos.getPos(entity, xzRange, yRange);
			if (target != null)
				return new BlockPos(target);
		}

		return null;
	}

	public static BlockPos getRandomPositionAway(
		@NotNull PathfinderMob mob,
		@NotNull LivingEntity attacker,
		int xzRange,
		int yRange,
		int retries
	) {
		return Pathfinder.getRandomPositionAway(
			mob,
			attacker.blockPosition(),
			xzRange,
			yRange,
			retries
		);
	}

	public static BlockPos getRandomPositionAway(
		@NotNull PathfinderMob mob,
		@NotNull BlockPos pos,
		int xzRange,
		int yRange,
		int retries
	) {
		var vec = Vec3.atBottomCenterOf(pos);
		for (int i = 0; i < retries; i++) {
			var target = DefaultRandomPos.getPosAway(mob, xzRange, yRange, vec);
			if (target != null)
				return new BlockPos(target);
		}

		return null;
	}
}
