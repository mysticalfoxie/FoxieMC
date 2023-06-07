package me.m1chelle99.foxiemc.entities.foxie.goals_old;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.controls.FoxieAIControl;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FoxieStalkPreyGoal extends Goal {
    private final Foxie foxie;

    public FoxieStalkPreyGoal(Foxie foxie) {
        this.foxie = foxie;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        if (foxie.getFlag(FoxieAIControl.COMMAND_DOWN)) return false;
        if (foxie.getFlag(FoxieAIControl.SLEEPING)) return false;
        var target = foxie.getTarget();
        if (target == null) return false;
        if (!foxie.isPrey(target)) return false;
        if (foxie.distanceToSqr(target) < 36.0D) return false;
        if (foxie.isCrouching()) return false;
        if (foxie.getFlag(FoxieAIControl.INTERESTED)) return false;
        return !foxie.isJumping();
    }

    public void start() {
        foxie.setFlag(FoxieAIControl.SITTING, false);
        foxie.setFlag(FoxieAIControl.FACEPLANTED, false);
    }

    public void stop() {
        var prey = foxie.getTarget();
        if (prey == null || foxie.isPathClearTo(prey)) {
            foxie.setFlag(FoxieAIControl.INTERESTED, false);
            foxie.setFlag(FoxieAIControl.CROUCHING, false);
            return;
        }

        foxie.setFlag(FoxieAIControl.INTERESTED, true);
        foxie.setFlag(FoxieAIControl.CROUCHING, true);
        foxie.getNavigation().stop();
        foxie.getLookControl().setLookAt(prey, (float) foxie.getMaxHeadYRot(), (float) foxie.getMaxHeadXRot());
    }

    public void tick() {
        var prey = foxie.getTarget();
        if (prey == null) return;

        foxie.getLookControl().setLookAt(prey, (float) foxie.getMaxHeadYRot(), (float) foxie.getMaxHeadXRot());
        if (foxie.distanceToSqr(prey) > 36.0D) {
            foxie.getNavigation().moveTo(prey, 1.5D);
            return;
        }

        foxie.setFlag(FoxieAIControl.INTERESTED, true);
        foxie.setFlag(FoxieAIControl.CROUCHING, false);
        foxie.getNavigation().stop();
    }
}
