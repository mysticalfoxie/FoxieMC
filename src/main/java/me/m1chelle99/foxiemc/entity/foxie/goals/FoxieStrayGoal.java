package me.m1chelle99.foxiemc.entity.foxie.goals;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieActivities;
import me.m1chelle99.foxiemc.helper.Pathfinder;
import net.minecraft.world.entity.ai.goal.Goal;

public class FoxieStrayGoal extends Goal {
    private final Foxie _foxie;

    public FoxieStrayGoal(Foxie foxie) {
        this._foxie = foxie;
    }

    @Override
    public boolean canUse() {
        if (!this._foxie.getNavigation().isDone()) return false;
        return this._foxie.aiControl.canStray();
    }

    @Override
    public boolean canContinueToUse() {
        return this._foxie.aiControl.canStray();
    }

    @Override
    public void start() {
        this._foxie.aiControl.startActivity(FoxieActivities.Stray);
    }

    @Override
    public void stop() {
        this._foxie.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this._foxie.getNavigation().isInProgress())
            return;
        if (this._foxie.getRandom().nextFloat() > .035F)
            return;

        var random = Pathfinder.getRandomPositionWithin(this._foxie, 12, 5, 3);
        if (random == null)
            return;

        this._foxie.runTo(random, 1.0F);
    }
}
