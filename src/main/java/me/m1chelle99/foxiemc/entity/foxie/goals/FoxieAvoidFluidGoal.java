package me.m1chelle99.foxiemc.entity.foxie.goals;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import me.m1chelle99.foxiemc.helper.Pathfinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class FoxieAvoidFluidGoal extends Goal {

    private final Foxie _foxie;
    private boolean _shouldPanic = false;
    private byte _lastActivity = FoxieConstants.ACTIVITY_NONE;

    public FoxieAvoidFluidGoal(Foxie foxie) {
        this._foxie = foxie;
    }

    @Override
    public boolean canUse() {
        var position = this._foxie.blockPosition();
        var level = this._foxie.level;
        var fluid = level.getFluidState(position);
        var fluidBelow = level.getFluidState(position.below());
        if (fluid.isEmpty() || fluidBelow.isEmpty())
            return false;

        if (fluid.is(Fluids.LAVA) || fluid.is(Fluids.FLOWING_LAVA))
            return true;

        return this._foxie.aiControl.canAvoidWater(); // Or custom fluids
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() && !this._foxie.getNavigation().isDone();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this._foxie.aiControl
                .startActivity(FoxieConstants.ACTIVITY_AVOID_FLUID);
        this._lastActivity = this._foxie.aiControl.getActivity();

        var level = this._foxie.level;
        var position = this._foxie.blockPosition();
        var fluid = level.getFluidState(position);
        var fluidBelow = level.getFluidState(position.below());
        this._shouldPanic = fluid.is(Fluids.LAVA)
                || fluidBelow.is(Fluids.FLOWING_LAVA);

        var target = this.getTarget();
        if (target == null)
            return;

        this._foxie.runTo(target, 1.0F);
    }

    @Override
    public void stop() {
        var activity = this._shouldPanic
                ? FoxieConstants.ACTIVITY_PANIC
                : this._lastActivity;

        this._foxie.aiControl.startActivity(activity);
    }

    private BlockPos getTarget() {
        var target = Pathfinder.getClosestPathWhere(this._foxie, 7, 2, block ->
                this._foxie.level.isFluidAtPosition(block, FluidState::isEmpty));

        if (target != null)
            return target;

        target = Pathfinder.getPathInLookDirection(this._foxie, 7, 2, 3);

        if (target != null)
            return target;

        var vector = DefaultRandomPos.getPos(this._foxie, 7, 2);

        if (vector != null)
            target = new BlockPos(vector);

        return target;
    }

    public void tick() {
        if (!this._foxie.isInFluid()) {
            this._foxie.getNavigation().setSpeedModifier(1.0F);
            return;
        }

        var mod = FoxieConstants.AVOID_FLUID_MOVEMENT_SPEED_MULTIPLIER;
        this._foxie.getNavigation().setSpeedModifier(mod);
    }
}
