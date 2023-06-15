package me.m1chelle99.foxiemc.entity.foxie.goals.fluids;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.world.level.material.Fluids;

public class FoxieAvoidCustomFluidsGoal extends FoxieAbstractAvoidFluidGoal {
    public FoxieAvoidCustomFluidsGoal(Foxie foxie) {
        super(foxie, FoxieConstants.ACTIVITY_AVOID_FLUID);
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) return false;
        if (!this._foxie.aiControl.canAvoidWater()) return false;
        return !this._fluid.is(Fluids.WATER)
            && !this._fluid.is(Fluids.FLOWING_WATER)
            && !this._fluid.is(Fluids.LAVA)
            && !this._fluid.is(Fluids.FLOWING_LAVA);
    }
}
