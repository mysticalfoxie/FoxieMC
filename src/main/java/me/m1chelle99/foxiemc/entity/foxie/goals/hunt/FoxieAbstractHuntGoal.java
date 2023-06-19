package me.m1chelle99.foxiemc.entity.foxie.goals.hunt;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieActivities;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class FoxieAbstractHuntGoal extends Goal {

    protected final Foxie _foxie;

    public FoxieAbstractHuntGoal(Foxie foxie) {

        this._foxie = foxie;
    }

    @Override
    public boolean canUse() {
        if (!this._foxie.aiControl.canHunt()) return false;
        if (!this._foxie.hungerControl.isHungry()) return false;
        return !this._foxie.getNavigation().isDone();
    }

    @Override
    public boolean canContinueToUse() {
        if (!this._foxie.aiControl.canHunt()) return false;
        if (!this._foxie.hungerControl.isHungry()) return false;
    }

    @Override
    public void start() {
        this._foxie.aiControl.startActivity(FoxieActivities.SearchForFood);
    }
}
