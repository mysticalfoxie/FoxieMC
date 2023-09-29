package me.m1chelle99.foxiemc.entity.foxie.goals.fluids;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieActivities;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieMovementSpeed;
import me.m1chelle99.foxiemc.helper.Pathfinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.material.FluidState;

public abstract class FoxieAbstractAvoidFluidGoal extends Goal {
    protected final Foxie _foxie;
    protected final FoxieActivities _activity;
    protected FluidState _fluid;

    public FoxieAbstractAvoidFluidGoal(
        Foxie foxie,
        FoxieActivities activity
    ) {
        this._foxie = foxie;
        this._activity = activity;
    }

    @Override
    public boolean canUse() {
        return !this.getFluid().isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() && !this._foxie.getNavigation().isDone();
    }

    @Override
    public void start() {
        this._foxie.aiControl.startActivity(this._activity);
        var target = this.getTarget();
        if (target == null)
            return;

        this._foxie.runTo(target, 1.0F);
    }

    @Override
    public void stop() {
        if (this._foxie.aiControl.hasActivity(FoxieActivities.AvoidFluid) ||
            this._foxie.aiControl.hasActivity(FoxieActivities.AvoidLava))
            this._foxie.aiControl.startActivity(FoxieActivities.None);
        this._foxie.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (!this._foxie.isInFluid()) {
            this._foxie.getNavigation().setSpeedModifier(1.0F);
            return;
        }

        var mod = FoxieMovementSpeed.AVOID_FLUID;
        this._foxie.getNavigation().setSpeedModifier(mod);
    }

    private FluidState getFluid() {
        var position = this._foxie.blockPosition();
        var level = this._foxie.level;
        var fluid = level.getFluidState(position);
        var fluidBelow = level.getFluidState(position.below());
        return this._fluid = fluid.isEmpty() ? fluidBelow : fluid;
    }

    private BlockPos getTarget() {
        var target = Pathfinder.getClosestPathWhere(this._foxie, 7, 2, block ->
            this._foxie.level
                .isFluidAtPosition(block, FluidState::isEmpty));

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
}
