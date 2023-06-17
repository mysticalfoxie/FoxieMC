package me.m1chelle99.foxiemc.entity.foxie.goals;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieActivities;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;

public class FoxieClimbSnowGoal extends ClimbOnTopOfPowderSnowGoal {
    private final Foxie _foxie;

    public FoxieClimbSnowGoal(Foxie foxie) {
        super(foxie, foxie.level);
        this._foxie = foxie;
    }

    @Override
    public void start() {
        this._foxie.aiControl.startActivity(FoxieActivities.None);
    }
}
