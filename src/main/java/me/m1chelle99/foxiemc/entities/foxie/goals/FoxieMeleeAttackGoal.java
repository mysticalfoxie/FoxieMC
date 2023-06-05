package me.m1chelle99.foxiemc.entities.foxie.goals;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.FoxieStates;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class FoxieMeleeAttackGoal extends MeleeAttackGoal {
    private final Foxie foxie;

    public FoxieMeleeAttackGoal(Foxie foxie, double speed_modifier, boolean followingTargetEvenIfNotSeen) {
        super(foxie, speed_modifier, followingTargetEvenIfNotSeen);
        this.foxie = foxie;
    }

    protected void checkAndPerformAttack(@NotNull LivingEntity prey, double p_28725_) {
        double d0 = this.getAttackReachSqr(prey);
        if (p_28725_ <= d0 && this.isTimeToAttack()) {
            this.resetAttackCooldown();
            this.mob.doHurtTarget(prey);
            foxie.playSound(SoundEvents.FOX_BITE, 1.0F, 1.0F);
        }
    }

    public void start() {
        foxie.setFlag(FoxieStates.INTERESTED, false);
        super.start();
    }

    public boolean canUse() {
        return !foxie.getFlag(FoxieStates.SITTING)
                && !foxie.getFlag(FoxieStates.COMMAND_DOWN)
                && !foxie.getFlag(FoxieStates.SLEEPING)
                && !foxie.getFlag(FoxieStates.CROUCHING)
                && !foxie.getFlag(FoxieStates.FACEPLANTED)
                && super.canUse();
    }
}
