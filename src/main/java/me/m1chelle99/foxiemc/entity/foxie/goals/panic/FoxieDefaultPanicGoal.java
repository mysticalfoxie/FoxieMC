package me.m1chelle99.foxiemc.entity.foxie.goals.panic;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.helper.EntityHelper;

public class FoxieDefaultPanicGoal extends FoxieAbstractPanicGoal {

    public FoxieDefaultPanicGoal(Foxie foxie) {
        super(foxie);
    }

    public boolean canUse() {
        return super.canUse()
                && !this._foxie.isOnFire()
                && this._foxie.getLastHurtByMob() == null;
    }

    @Override
    public void setNewTarget() {
        this._target = this._foxie.getRandomTargetWithin(3);
    }

    @Override
    public void setCooldown() {
        this._cooldown = EntityHelper.getRandomTicksWithin(this._foxie, 3, 6);
    }
}
