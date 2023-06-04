package me.m1chelle99.foxiemc.entities.foxie.goals;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public abstract class FoxieBehaviorGoal extends Goal {
    protected final Foxie foxie;
    private final TargetingConditions alertableTargeting;

    protected FoxieBehaviorGoal(Foxie foxie) {
        this.foxie = foxie;
        this.alertableTargeting = TargetingConditions
                .forCombat()
                .range(12.0D)
                .ignoreLineOfSight()
                .selector(this.foxie::isAlerting);
    }

    protected boolean hasShelter() {
        var pos = new BlockPos(foxie.getX(), foxie.getBoundingBox().maxY, foxie.getZ());
        return !foxie.level.canSeeSky(pos) && foxie.getWalkTargetValue(pos) >= 0.0F;
    }

    protected boolean alertable() {
        return !foxie.level.getNearbyEntities(LivingEntity.class, this.alertableTargeting, foxie,
                foxie.getBoundingBox().inflate(Foxie.ALERTING_RANGE, 10.0D, Foxie.ALERTING_RANGE)).isEmpty();
    }
}
