package me.m1chelle99.foxiemc.entity.foxie.goals.sleep;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import net.minecraft.world.entity.LivingEntity;

public class TamedSleepGoal extends AbstractSleepGoal {
    private int _randomSleepCooldown = 0;

    public TamedSleepGoal(Foxie foxie) {
        super(foxie);
    }

    public boolean canUse() {
        if (!super.canUse()) return false;
        if (!this._foxie.ownerControl.isTame()) return false;
        var owner = this._foxie.getOwner();
        if (!this.canUseWithOwner(owner)) return false;
        return this._foxie.getRandom().nextBoolean();
    }

    private boolean canUseWithOwner(LivingEntity owner) {
        if (owner == null) return false;
        if (owner.isSleeping()) return true;
        if (this._randomSleepCooldown > 0) return false;
        if (!owner.isAlive()) return false;
        if (owner.getLastDamageSource() != null) return false;
        return owner.moveDist <= 0;
    }

    public void start() {
        super.start();
        this._randomSleepCooldown = 3_000;
    }
}
