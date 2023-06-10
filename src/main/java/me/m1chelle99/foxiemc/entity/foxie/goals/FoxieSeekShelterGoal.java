package me.m1chelle99.foxiemc.entity.foxie.goals;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import me.m1chelle99.foxiemc.helper.Pathfinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;

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
		if (!this.isRainingOrThundering()) return false;
		var position = this.foxie.blockPosition();
		return this.foxie.level.canSeeSky(position);
	}

	private boolean isRainingOrThundering() {
		return this.foxie.level.isRaining()
			|| this.foxie.level.isThundering();
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
		this.foxie.getNavigation().stop();
		if (this.foxie.aiControl.hasActivity(seekShelter))
			this.foxie.aiControl.startActivity(FoxieConstants.ACTIVITY_NONE);
		this.target = null;
	}

	@Override
	public void tick() {
		if (!this.foxie.getNavigation().isDone()) return;
		this.setNewTargetPosition();
		if (this.target != null) {
			var mod = FoxieConstants.SEEK_SHELTER_MOVEMENT_SPEED_MULTIPLIER;
			this.foxie.runTo(this.target, mod);
		}
	}

	public void setNewTargetPosition() {
		var range = 25;

		this.target = Pathfinder.getClosestPathWhere(this.foxie, range, 5, b ->
			!this.foxie.level.canSeeSky(b));

		if (target != null)
			return;

		this.target = Pathfinder.getPathInLookDirection(this.foxie, range, 5);

		if (target != null)
			return;

		var target = DefaultRandomPos.getPos(this.foxie, range, 4);

		if (target == null)
			return;

		this.target = new BlockPos(target);
	}
}
