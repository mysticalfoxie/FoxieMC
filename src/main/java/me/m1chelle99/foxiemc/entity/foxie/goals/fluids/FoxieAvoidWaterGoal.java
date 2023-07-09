package me.m1chelle99.foxiemc.entity.foxie.goals.fluids;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieActivities;
import net.minecraft.world.level.material.Fluids;

public class FoxieAvoidWaterGoal extends FoxieAbstractAvoidFluidGoal {

    public FoxieAvoidWaterGoal(Foxie foxie) {
        super(foxie, FoxieActivities.AvoidFluid);
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) return false;
        if (!this._foxie.aiControl.canAvoidWater()) return false;
        return this._fluid.is(Fluids.WATER)
            || this._fluid.is(Fluids.FLOWING_WATER);
    }
}
