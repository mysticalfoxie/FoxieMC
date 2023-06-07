package me.m1chelle99.foxiemc.entities.foxie.goals.panic;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.FoxieConstants;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public abstract class FoxieAbstractPanicGoal extends Goal {
    protected final Foxie foxie;
    protected Vec3 target;
    protected int cooldown;

    public FoxieAbstractPanicGoal(Foxie foxie) {
        this.foxie = foxie;
    }

    public boolean canUse() {
        if (this.foxie.isDeadOrDying()) return false;
        return this.foxie.stateControl.isInPanic();
    }

    public void start() {
        this.setCooldown();
        this.setNewTarget();
        if (this.target != null)
            this.foxie.runTo(this.target, FoxieConstants.MS_PANIC_MULTIPLIER);
    }
    
    public void stop() {
        this.target = null;
        this.cooldown = 0;
    }

    public abstract void setNewTarget();
    public abstract void setCooldown();
    
    public boolean requiresUpdateEveryTick() { return true; }
    
    public boolean canContinueToUse() { return this.cooldown > 0 || this.foxie.isDeadOrDying(); }
    
    public void tick() {
        if (this.foxie.level.isClientSide) return;
        if (this.cooldown <= 0) return;
        this.cooldown--;

        if (!this.foxie.getNavigation().isDone()) return;
        this.setNewTarget();
    }
}
