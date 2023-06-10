package me.m1chelle99.foxiemc.entity.foxie.goals;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import me.m1chelle99.foxiemc.helper.Pathfinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class AvoidFluidGoal extends Goal {

	private final Foxie foxie;
	private final boolean shouldPanic = false;
	private byte lastActivity = FoxieConstants.ACTIVITY_NONE;

	public AvoidFluidGoal(Foxie foxie) {
		this.foxie = foxie;
	}

	@Override
	public boolean canUse() {
		var position = this.foxie.blockPosition();
		var level = this.foxie.level;
		var fluid = level.getFluidState(position);
		var fluidBelow = level.getFluidState(position.below());
		if (fluid.isEmpty() || fluidBelow.isEmpty())
			return false;

		if (fluid.is(Fluids.LAVA) || fluid.is(Fluids.FLOWING_LAVA))
			return true;

		return this.foxie.aiControl.canAvoidWater(); // Or custom fluids
	}

	@Override
	public boolean canContinueToUse() {
		return this.canUse();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void start() {
		this.foxie.aiControl.startActivity(FoxieConstants.ACTIVITY_AVOID_FLUID);
		this.lastActivity = this.foxie.aiControl.getActivity();

		var target = this.getTarget();
		if (target == null)
			return;

		var mod = FoxieConstants.AVOID_FLUID_MOVEMENT_SPEED_MULTIPLIER;
		this.foxie.runTo(target, mod);
	}

	@Override
	public void stop() {
		var activity = this.shouldPanic
			? FoxieConstants.ACTIVITY_PANIC
			: this.lastActivity;

		this.foxie.aiControl.startActivity(activity);
	}

	private BlockPos getTarget() {
		var target = Pathfinder.getClosestPathWhere(this.foxie, 7, 2, block ->
			this.foxie.level.isFluidAtPosition(block, FluidState::isEmpty));

		if (target != null)
			return target;

		target = Pathfinder.getPathInLookDirection(this.foxie, 7, 2);

		if (target != null)
			return target;

		var vector = DefaultRandomPos.getPos(this.foxie, 7, 2);

		if (vector != null)
			target = new BlockPos(vector);

		return target;
	}
}
