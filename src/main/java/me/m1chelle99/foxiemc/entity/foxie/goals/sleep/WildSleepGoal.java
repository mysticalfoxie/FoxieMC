package me.m1chelle99.foxiemc.entity.foxie.goals.sleep;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieActivities;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieMovementSpeed;
import me.m1chelle99.foxiemc.helper.Pathfinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

public class WildSleepGoal extends AbstractSleepGoal {
    private BlockPos _target;
    private int _cooldown;
    public WildSleepGoal(Foxie foxie) {
        super(foxie);
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) return false;
        if (this._foxie.ownerControl.isTame()) return false;
        return this.canSleepAtTimeOfDay();
    }

    @Override
    public boolean canContinueToUse() {
        if (!super.canContinueToUse()) return false;
        if (this.areThreadsNearby()) return false;
        return this.canSleepAtTimeOfDay();
    }

    @Override
    public void start() {
        var activity = FoxieActivities.SeekSleepShelter;
        this._foxie.aiControl.startActivity(activity);
    }

    @Override
    public void stop() {
        if (this._foxie.isSleeping())
            this._foxie.stopSleeping();

        if (!this._foxie.getNavigation().isDone())
            this._foxie.getNavigation().stop();

        if (this._foxie.aiControl.hasActivity(FoxieActivities.Sleep) ||
            this._foxie.aiControl.hasActivity(FoxieActivities.SeekSleepShelter))
            this._foxie.aiControl.startActivity(FoxieActivities.None);
    }

    private boolean canSleepAtTimeOfDay() {
        var time = this._foxie.level.getDayTime();
        if (time > 22_000) return false;
        if (time > 15_000) return true;
        if (time > 8_000) return false;
        return time > 4_000;
    }

    private boolean areThreadsNearby() {
        var boundary = this._foxie.getBoundingBox().inflate(10, 6, 10);
        var level = this._foxie.level;
        var threads = level.getEntities(this._foxie, boundary, entity -> {
            if (entity instanceof Monster) return true;
            if (!(entity instanceof Player player)) return false;
            if (player.isSpectator()) return false;
            return !player.isCrouching();
        });

        return !threads.isEmpty();
    }

    @Override
    public void tick() {
        if (!this._foxie.getNavigation().isDone())
            return;

        if (this._foxie.aiControl.hasActivity(FoxieActivities.Sleep)) {
            if (!this.areThreadsNearby()) return;
            this._foxie.stopSleeping();
            var activity = FoxieActivities.SeekSleepShelter;
            this._foxie.aiControl.startActivity(activity);
            this._cooldown = 0;
        }

        if (this._cooldown > 0) {
            // TODO: To make it perfect look around here a bit.
            this._cooldown--;
            return;
        }

        var position = this._foxie.blockPosition();
        if (this.isDesiredPosition(position)) {
            this._foxie.startSleeping(position);
            this._foxie.aiControl.startActivity(FoxieActivities.Sleep);
            this._cooldown = this._foxie.getRandom().nextInt(20, 100);
            return;
        }
        
        if (this._foxie.isSleeping()) {
            this._foxie.stopSleeping();
            
            var activity = FoxieActivities.SeekSleepShelter;
            if (!this._foxie.aiControl.hasActivity(activity))
                this._foxie.aiControl.startActivity(activity);
        }

        this.setNewTargetPosition();
        this._cooldown = this._foxie.getRandom().nextInt(20, 100);
        if (this._target != null) {
            var mod = FoxieMovementSpeed.SEEK_SLEEP_SHELTER;
            this._foxie.runTo(this._target, mod);
        }
    }

    private boolean isDesiredPosition(BlockPos pos) {
        if (this._foxie.level.canSeeSky(pos)) return false;
        var engine = this._foxie.level.getLightEngine();
        var light = engine.getRawBrightness(pos, 0);
        if (light > 13) return false;
        if (light <= 8) return false;
        return !this.areThreadsNearby();
    }

    private void setNewTargetPosition() {
        this._target = Pathfinder.getClosestPathWhere(
            this._foxie, 15, 3,
            this::isDesiredPosition);

        if (_target != null)
            return;

        this._target = Pathfinder
            .getPathInLookDirection(this._foxie, 20, 4, 10);
        if (_target == null)
            return;

        this._target = Pathfinder
            .getRandomPositionWithin(this._foxie, 15, 4, 6);
    }
}
