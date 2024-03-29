package me.m1chelle99.foxiemc.entity.foxie.goals.panic;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieActivities;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieMovementSpeed;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public abstract class FoxieAbstractPanicGoal extends Goal {
    protected final Foxie _foxie;
    protected Vec3 _target;
    protected int _cooldown;

    public FoxieAbstractPanicGoal(Foxie foxie) {
        this._foxie = foxie;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }
    
    public boolean canUse() {
        if (this._foxie.isDeadOrDying()) return false;
        return this._foxie.aiControl.isPanic();
    }

    public boolean canContinueToUse() {
        return this._cooldown > 0 || this._foxie.isDeadOrDying();
    }

    public void start() {
        this.setCooldown();
        this.setNewTarget();
        if (this._target != null)
            this._foxie.runTo(this._target, FoxieMovementSpeed.PANIC);
    }

    public void stop() {
        this._target = null;
        this._cooldown = 0;
        if (this._foxie.aiControl.hasActivity(FoxieActivities.Panic))
            this._foxie.aiControl.startActivity(FoxieActivities.None);
        
        this._foxie.getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        if (this._foxie.level.isClientSide) return;
        if (this._cooldown <= 0) return;
        this._cooldown--;

        if (!this._foxie.getNavigation().isDone()) return;
        this.setNewTarget();
        if (this._target != null)
            this._foxie.runTo(this._target, FoxieMovementSpeed.PANIC);
    }

    public abstract void setNewTarget();

    public abstract void setCooldown();
}
