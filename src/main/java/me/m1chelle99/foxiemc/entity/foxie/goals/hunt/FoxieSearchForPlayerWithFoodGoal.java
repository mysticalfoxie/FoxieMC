package me.m1chelle99.foxiemc.entity.foxie.goals.hunt;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;

public class FoxieSearchForPlayerWithFoodGoal extends FoxieAbstractHuntGoal {
    public FoxieSearchForPlayerWithFoodGoal(Foxie foxie) {
        super(foxie);
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) return false;
        if (_prey != null) return false;
        if (_foodItem != null) return false;
        if (_berries != null) return false;
        if (_player != null) return false;

    }
}
