package me.m1chelle99.foxiemc.entity.foxie.goals.hunt;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.*;

public class FoxieSearchForPreyGoal extends FoxieAbstractHuntGoal {
    private final TargetingConditions _targeting = TargetingConditions
        .forNonCombat()
        .range(20)
        .selector(this::isPrey);

    public FoxieSearchForPreyGoal(Foxie foxie) {
        super(foxie);
    }

    private boolean isPrey(LivingEntity livingEntity) {
        if (livingEntity instanceof Sheep) return true;
        if (livingEntity instanceof Chicken) return true;
        if (livingEntity instanceof Rabbit) return true;
        return livingEntity instanceof AbstractFish;
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) return false;
        if (_prey != null) return false;
        return this.findPrey() != null;
    }


    public LivingEntity findPrey() {
        var boundary = this._foxie.getBoundingBox().inflate(20, 10, 20);
        return this._prey = this._foxie.level.getNearestEntity(
            Animal.class,
            this._targeting,
            this._foxie,
            this._foxie.getX(),
            this._foxie.getY(),
            this._foxie.getZ(),
            boundary
        );
    }
}
