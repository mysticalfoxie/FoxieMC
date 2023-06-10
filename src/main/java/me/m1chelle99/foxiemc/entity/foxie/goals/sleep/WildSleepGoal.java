package me.m1chelle99.foxiemc.entity.foxie.goals.sleep;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;

public class WildSleepGoal extends AbstractSleepGoal {
	public WildSleepGoal(Foxie foxie) {
		super(foxie);
	}

	@Override
	public boolean canUse() {
		if (!super.canUse()) return false;
		if (this.foxie.ownerControl.isTame()) return false;
		return this.canSleepAtTimeOfDay();
	}

	@Override
	public boolean canContinueToUse() {
		if (!super.canContinueToUse()) return false;
		return this.canSleepAtTimeOfDay();
	}

	private boolean canSleepAtTimeOfDay() {
		var time = this.foxie.level.getDayTime();
		if (time > 22_000) return false;
		if (time > 15_000) return true;
		if (time > 8_000) return false;
		return time > 4_000;
	}

}
