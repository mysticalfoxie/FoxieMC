package me.m1chelle99.foxiemc.entity.foxie.goals;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import me.m1chelle99.foxiemc.helper.EntityHelper;
import me.m1chelle99.foxiemc.helper.PerformanceAnalysis;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FoxieSeekShelterGoal extends Goal {
	private final Foxie foxie;
	private BlockPos target;

	public FoxieSeekShelterGoal(Foxie foxie) {
		this.foxie = foxie;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (!this.foxie.aiControl.canSeekShelter()) return false;
		if (!this.foxie.level.isRaining()) return false;
		if (!this.foxie.level.isThundering()) return false;
		var position = EntityHelper.getRoundedBlockPos(this.foxie);
		return this.foxie.level.canSeeSky(position);
	}

	@Override
	public boolean canContinueToUse() {
		return this.canUse();
	}

	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void start() {
		var activity = FoxieConstants.ACTIVITY_SEEK_SHELTER;
		this.foxie.aiControl.startActivity(activity);
	}

	@Override
	public void stop() {
		var seekShelter = FoxieConstants.ACTIVITY_SEEK_SHELTER;
		if (this.foxie.aiControl.hasActivity(seekShelter))
			this.foxie.aiControl.startActivity(FoxieConstants.ACTIVITY_NONE);
		this.target = null;
	}

	@Override
	public void tick() {
		if (!this.foxie.getNavigation().isDone()) return;
		this.setNewTargetPosition();
		if (this.target != null) {
			var vector = new Vec3(
				this.target.getX(),
				this.target.getY(),
				this.target.getZ()
			);
			var mod = FoxieConstants.SEEK_SHELTER_MOVEMENT_SPEED_MULTIPLIER;
			this.foxie.runTo(vector, mod);
		}
	}

	public void setNewTargetPosition() {
		this.target = this.findNewShelter();
		if (target != null) return;
		this.target = EntityHelper.getRandomPositionInLookAngle(this.foxie, 10);
		if (target != null) return;
		var target = DefaultRandomPos.getPos(this.foxie, 10, 4);
		if (target == null) return;
		this.target = new BlockPos(target);
	}

	public BlockPos findNewShelter() {
		var current = this.foxie.blockPosition();
		var blocks = PerformanceAnalysis.execMonitored(
			"getReachableBlocks",
			(foo) -> EntityHelper.getReachableBlocks(this.foxie, 6));

		BlockPos destination = null;
		int destination_range = Integer.MAX_VALUE;

		for (BlockPos block : blocks) {
			var range = current.distManhattan(block);
			if (destination != null && range > destination_range)
				continue;

			destination = block;
			destination_range = range;
		}

		return destination;
	}
}
