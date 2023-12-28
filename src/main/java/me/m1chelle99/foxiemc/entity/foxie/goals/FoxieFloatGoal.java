package me.m1chelle99.foxiemc.entity.foxie.goals;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FoxieFloatGoal extends Goal {
    private final Foxie _foxie;

    public FoxieFloatGoal(Foxie foxie) {
        this._foxie = foxie;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP));
        foxie.getNavigation().setCanFloat(true);
    }

    @Override
    public boolean canUse() {
        if (!this._foxie.isInFluid()) return false;

        @SuppressWarnings("deprecation")
        var fluid_height = this._foxie.getFluidHeight(FluidTags.WATER);
        return fluid_height > 0.25;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this._foxie.getJumpControl().jump();
    }
}
