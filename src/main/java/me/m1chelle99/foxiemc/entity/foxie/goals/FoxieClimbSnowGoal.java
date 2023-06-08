package me.m1chelle99.foxiemc.entity.foxie.goals;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;

public class FoxieClimbSnowGoal extends ClimbOnTopOfPowderSnowGoal {
    private final Foxie foxie;

    public FoxieClimbSnowGoal(Foxie foxie) {
        super(foxie, foxie.level);
        this.foxie = foxie;
    }

    @Override
    public void start() {
        this.foxie.aiControl.activate(FoxieConstants.ACTIVITY_NONE);
    }
}
