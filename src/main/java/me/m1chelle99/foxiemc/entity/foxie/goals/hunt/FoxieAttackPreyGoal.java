package me.m1chelle99.foxiemc.entity.foxie.goals.hunt;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieActivities;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieMovementSpeed;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class FoxieAttackPreyGoal extends Goal {
    private final Foxie _foxie;
    private Path path;
    private double pathTargetX;
    private double pathTargetY;
    private double pathTargetZ;
    private int ticksUntilRecalculation;
    private int ticksUntilNextAttack;
    private long lastCheck;
    public FoxieAttackPreyGoal(Foxie foxie) {
        this._foxie = foxie;

        var flags = EnumSet.of(
            Goal.Flag.MOVE,
            Goal.Flag.LOOK
        );

        this.setFlags(flags);
    }

    public boolean canUse() {
        return this._foxie.huntControl.prey != null
            && this._foxie.aiControl.canSearchForFood()
            && this.canCalculatePath();
    }

    public boolean canContinueToUse() {
        if (this._foxie.huntControl.prey == null)
            return false;
        else if (!this._foxie.huntControl.prey.isAlive())
            return false;

        var pos = this._foxie.huntControl.prey.blockPosition();
        return this._foxie.isWithinRestriction(pos);
    }

    public void start() {
        var mod = FoxieMovementSpeed.ATTACK;
        this._foxie.getNavigation().moveTo(this.path, mod);
        this._foxie.setAggressive(true);
        this.ticksUntilRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    public void stop() {
        this._foxie.huntControl.prey = null;
        this._foxie.setAggressive(false);
        this._foxie.getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        var prey = this._foxie.huntControl.prey;
        if (prey == null) return;

        this._foxie.getLookControl().setLookAt(prey, 30.0F, 30.0F);
        var d0 = this._foxie.distanceToSqr(
            prey.getX(),
            prey.getY(),
            prey.getZ()
        );

        this.ticksUntilRecalculation = Math.max(
            this.ticksUntilRecalculation - 1, 0);

        if (shouldRecalculatePath(prey))
            recalculatePath(prey, d0);

        this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        this.checkAndPerformAttack(prey, d0);
    }

    private boolean canCalculatePath() {
        var time = this._foxie.level().getGameTime();
        if (time - this.lastCheck < 20L)
            return false;

        this.lastCheck = time;

        var prey = this._foxie.huntControl.prey;
        if (prey == null) return false;
        if (!prey.isAlive()) return false;

        this.path = this._foxie
            .getNavigation()
            .createPath(prey, 0);

        if (this.path != null) return true;

        var att_dist = this.getAttackReachSqr(prey);
        var sqr_dist = this._foxie.distanceToSqr(
            prey.getX(),
            prey.getY(),
            prey.getZ());

        return att_dist >= sqr_dist;
    }

    private void recalculatePath(LivingEntity prey, double d0) {
        this.pathTargetX = prey.getX();
        this.pathTargetY = prey.getY();
        this.pathTargetZ = prey.getZ();
        this.ticksUntilRecalculation =
            4 + this._foxie.getRandom().nextInt(7);

        if (d0 > 1024.0D)
            this.ticksUntilRecalculation += 10;
        else if (d0 > 256.0D)
            this.ticksUntilRecalculation += 5;

        var canMove = this._foxie
            .getNavigation()
            .moveTo(prey, FoxieMovementSpeed.ATTACK);

        if (!canMove)
            this.ticksUntilRecalculation += 15;

        this.ticksUntilRecalculation =
            this.adjustedTickDelay(this.ticksUntilRecalculation);
    }

    private boolean shouldRecalculatePath(LivingEntity prey) {
        if (this.ticksUntilRecalculation > 0)
            return false;

        var pathTargetEmpty = this.pathTargetX == 0.0D
            && this.pathTargetY == 0.0D
            && this.pathTargetZ == 0.0D;

        var dist = prey.distanceToSqr(
            this.pathTargetX,
            this.pathTargetY,
            this.pathTargetZ);

        var rnd = this._foxie.getRandom().nextFloat() < 0.05F;

        return pathTargetEmpty || dist >= 1.0D || rnd;
    }

    protected void checkAndPerformAttack(LivingEntity prey, double dist) {
        var d0 = this.getAttackReachSqr(prey);
        if (dist > d0 || this.ticksUntilNextAttack > 0)
            return;

        this.ticksUntilNextAttack = this.adjustedTickDelay(20);
        this._foxie.doHurtTarget(prey);
        this._foxie.playSound(SoundEvents.FOX_BITE, 1.0F, 1.0F);

        if (prey.isDeadOrDying()) {
            this._foxie.hungerControl.setTicksSinceLastFood(0);
            this._foxie.huntControl.prey = null;
            this._foxie.aiControl.startActivity(FoxieActivities.None);
        }
    }

    protected double getAttackReachSqr(LivingEntity prey) {
        var bbWidth = Math.pow(this._foxie.getBbWidth(), 2);
        return bbWidth * 4.0F + prey.getBbWidth();
    }
}
