package me.m1chelle99.foxiemc.entity.foxie.goals.hunt;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;

public class FoxieSearchItemsGoal extends FoxieAbstractSearchForFoodGoal {
    public FoxieSearchItemsGoal(Foxie foxie) {
        super(foxie);
    }

    @Override
    public boolean canUse() {
        return false;
//        if (!super.canUse()) return false;
//        if (_prey != null) return false;
//        if (_foodItem != null) return false;
//        if ()
    }
}
