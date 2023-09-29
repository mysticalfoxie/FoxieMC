package me.m1chelle99.foxiemc.entity.foxie.goals.fluids;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieActivities;
import net.minecraft.world.level.material.Fluids;

public class FoxieAvoidLavaGoal extends FoxieAbstractAvoidFluidGoal {
    public FoxieAvoidLavaGoal(Foxie foxie) {
        super(foxie, FoxieActivities.AvoidLava);
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) return false;
        return this._fluid.is(Fluids.LAVA)
            || this._fluid.is(Fluids.FLOWING_LAVA);
    }

    @Override
    public void stop() {
        this._foxie.aiControl.startActivity(FoxieActivities.Panic);
    }
}
