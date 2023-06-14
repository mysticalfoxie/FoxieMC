package me.m1chelle99.foxiemc.entity.foxie.goals.sleep;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.world.entity.ai.goal.Goal;

// More than this dev sleeps on a normal work day uwu
public class AbstractSleepGoal extends Goal {
    protected final Foxie _foxie;

    public AbstractSleepGoal(Foxie foxie) {
        this._foxie = foxie;
    }

    @Override
    public boolean canUse() {
        if (this._foxie.aiControl.isSleeping()) return false;
        return this._foxie.aiControl.canSleep();
    }

    public boolean canContinueToUse() {
        return this._foxie.aiControl.canSleep();
    }

    @Override
    public void start() {
        this._foxie.aiControl.startActivity(FoxieConstants.ACTIVITY_SLEEP);
        var position = this._foxie.blockPosition();
        this._foxie.startSleeping(position);
    }

    @Override
    public void stop() {
        this._foxie.stopSleeping();
        if (this._foxie.aiControl.hasActivity(FoxieConstants.ACTIVITY_SLEEP))
            this._foxie.aiControl.startActivity(FoxieConstants.ACTIVITY_NONE);
    }
}
