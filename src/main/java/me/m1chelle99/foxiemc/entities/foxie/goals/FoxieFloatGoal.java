package me.m1chelle99.foxiemc.entities.foxie.goals;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.FloatGoal;

public class FoxieFloatGoal extends FloatGoal {
    private final Foxie foxie;

    public FoxieFloatGoal(Foxie foxie) {
        super(foxie);
        this.foxie = foxie;
    }

    public void start() {
        super.start();
        foxie.clearStates();
    }

    public boolean canUse() {
        return foxie.isInWater() && foxie.getFluidHeight(FluidTags.WATER) > 0.25D || foxie.isInLava();
    }
}
