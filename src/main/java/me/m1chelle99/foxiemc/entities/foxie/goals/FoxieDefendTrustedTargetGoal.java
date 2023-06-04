package me.m1chelle99.foxiemc.entities.foxie.goals;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.FoxieStates;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import javax.annotation.Nullable;

public class FoxieDefendTrustedTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {

    private final Foxie foxie;
    @Nullable
    private LivingEntity trustedLastHurtBy;
    @Nullable
    private LivingEntity trustedLastHurt;
    private int timestamp;

    public FoxieDefendTrustedTargetGoal(Foxie foxie) {
        super(foxie, LivingEntity.class, 10, false, false, foxie::getTrustedAttacker);
        this.foxie = foxie;
    }

    public boolean canUse() {
        if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0)
            return false;

        for (var uuid : foxie.getTrustedUUIDs()) {
            if (uuid == null || !(foxie.level instanceof ServerLevel))
                continue;

            var e = ((ServerLevel) foxie.level).getEntity(uuid);
            if (!(e instanceof LivingEntity entity)) continue;

            this.trustedLastHurt = entity;
            this.trustedLastHurtBy = entity.getLastHurtByMob();

            if (foxie.isThreatening(entity)) continue;

            int timestamp = entity.getLastHurtByMobTimestamp();
            if (timestamp == this.timestamp)
                return false;

            return this.canAttack(this.trustedLastHurtBy, this.targetConditions);
        }

        return false;
    }

    public void start() {
        this.setTarget(this.trustedLastHurtBy);
        this.target = this.trustedLastHurtBy;
        if (this.trustedLastHurt != null) {
            this.timestamp = this.trustedLastHurt.getLastHurtByMobTimestamp();
        }

        foxie.playSound(SoundEvents.FOX_AGGRO, 1.0F, 1.0F);
        foxie.setFlag(FoxieStates.DEFENDING, true);
        foxie.setFlag(FoxieStates.SLEEPING, false);
        super.start();
    }
}
