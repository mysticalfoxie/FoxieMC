package me.m1chelle99.foxiemc.entity.foxie.goals.sleep;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;

public class TamedSleepGoal extends AbstractSleepGoal {
    private int _randomSleepCooldown = 0;

    public TamedSleepGoal(Foxie foxie) {
        super(foxie);
    }

    public boolean canUse() {
        if (!super.canUse()) return false;
        if (!this.foxie.ownerControl.isTame()) return false;

        var owner = this.foxie.getOwner();
        if (owner == null) return false;
        if (owner.isSleeping()) return true;
        if (this._randomSleepCooldown > 0) return false;
        if (!owner.isAlive()) return false;
        if (owner.getLastDamageSource() != null) return false;
        if (owner.moveDist > 0) return false;
        return this.foxie.getRandom().nextBoolean();
    }

    public void start() {
        super.start();
        this._randomSleepCooldown = 3_000;
    }
}
