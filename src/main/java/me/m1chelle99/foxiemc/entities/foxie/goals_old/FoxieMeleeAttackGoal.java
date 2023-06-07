package me.m1chelle99.foxiemc.entities.foxie.goals_old;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.controls.FoxieAIControl;
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
            foxie.doHurtTarget(prey);

            if (prey.isDeadOrDying()) {
                foxie.setTicksSinceLastFood(0);
                foxie.playSound(SoundEvents.FOX_EAT, 1.0F, 1.0F);
            } else {
                foxie.playSound(SoundEvents.FOX_BITE, 1.0F, 1.0F);
            }
        }
    }

    public void start() {
        foxie.setFlag(FoxieAIControl.INTERESTED, false);
        super.start();
    }

    public boolean canUse() {
        return foxie.getTicksSinceLastFood() > Foxie.TICKS_UNTIL_HUNGER
                && !foxie.getFlag(FoxieAIControl.SITTING)
                && !foxie.getFlag(FoxieAIControl.COMMAND_DOWN)
                && !foxie.getFlag(FoxieAIControl.SLEEPING)
                && !foxie.getFlag(FoxieAIControl.CROUCHING)
                && !foxie.getFlag(FoxieAIControl.FACEPLANTED)
                && foxie.getMainHandItem().isEmpty()
                && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return canUse() && super.canContinueToUse();
    }
}
