package me.m1chelle99.foxiemc.entity.foxie.goals.panic;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;

public class FoxieDefaultPanicGoal extends FoxieAbstractPanicGoal {

    public FoxieDefaultPanicGoal(Foxie foxie) {
        super(foxie);
    }

    @Override
    public void setNewTarget() {
        this.target = this.foxie.getRandomTargetWithin(3);
    }

    @Override
    public void setCooldown() {
        this.cooldown = this.foxie.getRandomTicksWithin(3, 6);
    }
}
