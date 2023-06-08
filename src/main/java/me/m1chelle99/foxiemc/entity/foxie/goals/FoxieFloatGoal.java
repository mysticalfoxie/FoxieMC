package me.m1chelle99.foxiemc.entity.foxie.goals;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.world.entity.ai.goal.FloatGoal;

public class FoxieFloatGoal extends FloatGoal {
    private final Foxie foxie;

    public FoxieFloatGoal(Foxie foxie) {
        super(foxie);
        this.foxie = foxie;
    }

    @Override
    public void start() {
        this.foxie.aiControl.activate(FoxieConstants.ACTIVITY_NONE);
    }
}
