package me.m1chelle99.foxiemc.entity.foxie.goals.sleep;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.world.entity.ai.goal.Goal;

// More than this dev sleeps on a normal work day uwu
public class AbstractSleepGoal extends Goal {
    protected final Foxie foxie;

    public AbstractSleepGoal(Foxie foxie) {
        this.foxie = foxie;
    }

    @Override
    public boolean canUse() {
        if (this.foxie.aiControl.isSleeping()) return false;
        return this.foxie.aiControl.canSleep();
    }

    public boolean canContinueToUse() {
        return this.foxie.aiControl.canSleep();
    }

    @Override
    public void start() {
        this.foxie.aiControl.startActivity(FoxieConstants.ACTIVITY_SLEEP);
        var position = this.foxie.blockPosition();
        this.foxie.startSleeping(position);
    }

    @Override
    public void stop() {
        this.foxie.stopSleeping();
        if (this.foxie.aiControl.hasActivity(FoxieConstants.ACTIVITY_SLEEP))
            this.foxie.aiControl.startActivity(FoxieConstants.ACTIVITY_NONE);
    }
}
