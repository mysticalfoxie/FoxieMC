package me.m1chelle99.foxiemc.entities.foxie.goals;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.FoxieStates;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FoxieStalkPreyGoal extends Goal {
    private final Foxie foxie;

    public FoxieStalkPreyGoal(Foxie foxie) {
        this.foxie = foxie;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        if (foxie.getFlag(FoxieStates.SLEEPING)) return false;
        var target = foxie.getTarget();
        if (target == null) return false;
        if (!foxie.isPrey(target)) return false;
        if (foxie.distanceToSqr(target) < 36.0D) return false;
        if (foxie.isCrouching()) return false;
        if (foxie.getFlag(FoxieStates.INTERESTED)) return false;
        return !foxie.isJumping();
    }

    public void start() {
        foxie.setFlag(FoxieStates.SITTING, false);
        foxie.setFlag(FoxieStates.FACEPLANTED, false);
    }

    public void stop() {
        var prey = foxie.getTarget();
        if (prey == null || foxie.isPathClearTo(prey)) {
            foxie.setFlag(FoxieStates.INTERESTED, false);
            foxie.setFlag(FoxieStates.CROUCHING, false);
            return;
        }

        foxie.setFlag(FoxieStates.INTERESTED, true);
        foxie.setFlag(FoxieStates.CROUCHING, true);
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

        foxie.setFlag(FoxieStates.INTERESTED, true);
        foxie.setFlag(FoxieStates.CROUCHING, false);
        foxie.getNavigation().stop();
    }
}
