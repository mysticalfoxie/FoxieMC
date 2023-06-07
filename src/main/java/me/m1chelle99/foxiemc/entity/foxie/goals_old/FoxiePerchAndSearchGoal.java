package me.m1chelle99.foxiemc.entity.foxie.goals_old;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.controls.FoxieAIControl;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FoxiePerchAndSearchGoal extends FoxieBehaviorGoal {
    private double relX;
    private double relZ;
    private int lookTime;
    private int looksRemaining;

    public FoxiePerchAndSearchGoal(Foxie foxie) {
        super(foxie);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        return foxie.getLastHurtByMob() == null
                && foxie.getRandom().nextFloat() < 0.02F
                && !foxie.getFlag(FoxieAIControl.SLEEPING)
                && !foxie.getFlag(FoxieAIControl.COMMAND_DOWN)
                && foxie.getTarget() == null
                && foxie.getNavigation().isDone()
                && !this.alertable()
                && !foxie.getFlag(FoxieAIControl.POUNCING)
                && !foxie.getFlag(FoxieAIControl.CROUCHING);
    }

    public boolean canContinueToUse() {
        return this.looksRemaining > 0;
    }

    public void start() {
        this.resetLook();
        this.looksRemaining = 2 + foxie.getRandom().nextInt(3);
        foxie.setFlag(FoxieAIControl.SITTING, true);
        foxie.getNavigation().stop();
    }

    public void stop() {
        foxie.setFlag(FoxieAIControl.SITTING, false);
    }

    public void tick() {
        --this.lookTime;
        if (this.lookTime <= 0) {
            --this.looksRemaining;
            this.resetLook();
        }

        foxie.getLookControl()
                .setLookAt(foxie.getX() + this.relX, foxie.getEyeY(), foxie.getZ() + this.relZ, (float) foxie.getMaxHeadYRot(), (float) foxie.getMaxHeadXRot());
    }

    private void resetLook() {
        double d0 = (Math.PI * 2D) * foxie.getRandom().nextDouble();
        this.relX = Math.cos(d0);
        this.relZ = Math.sin(d0);
        this.lookTime = this.adjustedTickDelay(80 + foxie.getRandom().nextInt(20));
    }
}
