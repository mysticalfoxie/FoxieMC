package me.m1chelle99.foxiemc.entity.foxie.goals;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieActivities;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieMovementSpeed;
import me.m1chelle99.foxiemc.helper.Pathfinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FoxieSeekShelterGoal extends Goal {
    private final Foxie _foxie;
    private BlockPos _target;

    public FoxieSeekShelterGoal(Foxie foxie) {
        this._foxie = foxie;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this._foxie.aiControl.canSeekShelter()) return false;
        if (!this.isRainingOrThundering()) return false;
        var position = this._foxie.blockPosition();
        return this._foxie.level.canSeeSky(position);
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void start() {
        var activity = FoxieActivities.SeekRainShelter;
        this._foxie.aiControl.startActivity(activity);
    }

    @Override
    public void stop() {
        var seekShelter = FoxieActivities.SeekRainShelter;
        this._foxie.getNavigation().stop();
        if (this._foxie.aiControl.hasActivity(seekShelter))
            this._foxie.aiControl.startActivity(FoxieActivities.None);
        this._target = null;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (!this._foxie.getNavigation().isDone()) return;
        this.setNewTargetPosition();
        if (this._target != null) {
            var mod = FoxieMovementSpeed.SEEK_RAIN_SHELTER;
            this._foxie.runTo(this._target, mod);
        }
    }

    private boolean isRainingOrThundering() {
        return this._foxie.level.isRaining()
            || this._foxie.level.isThundering();
    }

    public void setNewTargetPosition() {
        var range = 25;

        this._target = Pathfinder
            .getClosestPathWhere(this._foxie, range, 5, b ->
                !this._foxie.level.canSeeSky(b));

        if (_target != null)
            return;

        this._target = Pathfinder
            .getPathInLookDirection(this._foxie, range, 4, 2);

        if (_target != null)
            return;

        var target = Pathfinder
            .getRandomPositionWithin(this._foxie, range, 4, 10);

        if (target == null)
            return;

        this._target = new BlockPos(target);
    }
}
