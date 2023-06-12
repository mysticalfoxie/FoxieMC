package me.m1chelle99.foxiemc.entity.foxie.goals.sleep;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import me.m1chelle99.foxiemc.helper.Pathfinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

public class WildSleepGoal extends AbstractSleepGoal {
    private BlockPos target;
    private int cooldown;

    public WildSleepGoal(Foxie foxie) {
        super(foxie);
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) return false;
        if (this.foxie.ownerControl.isTame()) return false;
        return this.canSleepAtTimeOfDay();
    }

    @Override
    public boolean canContinueToUse() {
        if (!super.canContinueToUse()) return false;
        return this.canSleepAtTimeOfDay();
    }

    @Override
    public void start() {
        var activity = FoxieConstants.ACTIVITY_SEARCH_FOR_SLEEP;
        this.foxie.aiControl.startActivity(activity);
    }

    @Override
    public void stop() {
        if (this.foxie.isSleeping())
            this.foxie.stopSleeping();

        if (!this.foxie.getNavigation().isDone())
            this.foxie.getNavigation().stop();

        if (this.foxie.aiControl.hasActivity(FoxieConstants.ACTIVITY_SLEEP))
            this.foxie.aiControl.startActivity(FoxieConstants.ACTIVITY_NONE);

        var stillOnSearch = FoxieConstants.ACTIVITY_SEARCH_FOR_SLEEP;
        if (this.foxie.aiControl.hasActivity(stillOnSearch))
            this.foxie.aiControl.startActivity(FoxieConstants.ACTIVITY_NONE);
    }

    private boolean canSleepAtTimeOfDay() {
        var time = this.foxie.level.getDayTime();
        if (time > 22_000) return false;
        if (time > 15_000) return true;
        if (time > 8_000) return false;
        return time > 4_000;
    }

    private boolean areThreadsNearby() {
        var boundary = this.foxie.getBoundingBox().inflate(10, 6, 10);
        var level = this.foxie.level;
        var threads = level.getEntities(this.foxie, boundary, entity -> {
            if (entity.isSprinting()) return true;
            if (entity instanceof Monster) return true;
            if (!(entity instanceof Player player)) return false;
            return !player.isCrouching();
        });

        return !threads.isEmpty();
    }

    @Override
    public void tick() {
        if (!this.foxie.getNavigation().isDone())
            return;

        if (this.foxie.aiControl.hasActivity(FoxieConstants.ACTIVITY_SLEEP)) {
            if (!this.areThreadsNearby()) return;
            this.foxie.stopSleeping();
            var activity = FoxieConstants.ACTIVITY_SEARCH_FOR_SLEEP;
            this.foxie.aiControl.startActivity(activity);
            this.cooldown = 0;
        }

        if (this.cooldown > 0) {
            // To make it perfect look around here a bit.
            this.cooldown--;
            return;
        }

        var position = this.foxie.blockPosition();
        if (this.isDesiredPosition(position)) {
            this.foxie.startSleeping(position);
            this.foxie.aiControl.startActivity(FoxieConstants.ACTIVITY_SLEEP);
            this.cooldown = this.foxie.getRandom().nextInt(40, 140);
            return;
        }

        this.setNewTargetPosition();
        this.cooldown = this.foxie.getRandom().nextInt(40, 140);
        if (this.target != null) {
            var mod = FoxieConstants.SEARCH_SLEEP_MOVEMENT_SPEED_MULTIPLIER;
            this.foxie.runTo(this.target, mod);
        }
    }

    private boolean isDesiredPosition(BlockPos pos) {
        if (this.foxie.level.canSeeSky(pos)) return false;
        var engine = this.foxie.level.getLightEngine();
        var light = engine.getRawBrightness(pos, 0);
        if (light > 12) return false;
        if (light <= 8) return false;
        return !this.areThreadsNearby();
    }

    private void setNewTargetPosition() {
        this.target = Pathfinder.getClosestPathWhere(
            this.foxie, 15, 3,
            this::isDesiredPosition);

        if (target != null)
            return;

        var target = Pathfinder
            .getRandomPositionWithin(this.foxie, 20, 4, 10);
        if (target == null)
            return;

        this.target = new BlockPos(target);
    }
}
