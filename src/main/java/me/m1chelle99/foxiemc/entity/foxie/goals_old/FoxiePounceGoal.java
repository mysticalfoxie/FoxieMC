package me.m1chelle99.foxiemc.entity.foxie.goals_old;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.controls.FoxieAIControl;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class FoxiePounceGoal extends JumpGoal {
    private final Foxie foxie;

    public FoxiePounceGoal(Foxie foxie) {
        this.foxie = foxie;
    }

    public boolean canUse() {
        if (foxie.getFlag(FoxieAIControl.COMMAND_DOWN))
            return false;

        if (foxie.getTicksSinceLastFood() < Foxie.TICKS_UNTIL_HUNGER)
            return false;

        if (!foxie.isFullyCrouched())
            return false;

        var prey = foxie.getTarget();
        if (prey == null || !prey.isAlive())
            return false;

        if (prey.getMotionDirection() != prey.getDirection())
            return false;

        var clearway = foxie.isPathClearTo(prey);
        if (!clearway) {
            foxie.getNavigation().createPath(prey, 0);
            foxie.setFlag(FoxieAIControl.CROUCHING, false);
            foxie.setFlag(FoxieAIControl.INTERESTED, false);
        }

        return clearway;
    }

    public boolean canContinueToUse() {
        var prey = foxie.getTarget();
        if (prey == null || !prey.isAlive())
            return false;

        double d0 = foxie.getDeltaMovement().y;
        return (!(d0 * d0 < (double) 0.05F)
                || !(Math.abs(foxie.getXRot()) < 15.0F)
                || !foxie.isOnGround())
                && !foxie.getFlag(FoxieAIControl.FACEPLANTED);
    }

    public boolean isInterruptable() {
        return false;
    }

    public void start() {
        foxie.setJumping(true);
        foxie.setFlag(FoxieAIControl.POUNCING, true);
        foxie.setFlag(FoxieAIControl.INTERESTED, false);

        var prey = foxie.getTarget();
        if (prey == null) {
            foxie.getNavigation().stop();
            return;
        }

        foxie.getLookControl().setLookAt(prey, 60.0F, 30.0F);
        Vec3 vec3 = (new Vec3(prey.getX() - foxie.getX(), prey.getY() - foxie.getY(), prey.getZ() - foxie.getZ())).normalize();
        foxie.setDeltaMovement(foxie.getDeltaMovement().add(vec3.x * 0.7D, 0.7D, vec3.z * 0.7D));
    }

    public void stop() {
        foxie.setFlag(FoxieAIControl.CROUCHING, false);
        foxie.setCrouchAmount(0.0F);
        foxie.setFlag(FoxieAIControl.INTERESTED, false);
        foxie.setFlag(FoxieAIControl.POUNCING, false);
    }

    public void tick() {
        var prey = foxie.getTarget();
        if (prey != null)
            foxie.getLookControl().setLookAt(prey, 60.0F, 30.0F);

        if (!foxie.getFlag(FoxieAIControl.FACEPLANTED)) {
            var vec = foxie.getDeltaMovement();
            if (vec.y * vec.y < (double) 0.03F && foxie.getXRot() != 0.0F) {
                //noinspection deprecation
                foxie.setXRot(Mth.rotlerp(foxie.getXRot(), 0.0F, 0.2F));
            } else {
                double d0 = vec.horizontalDistance();
                double d1 = Math.signum(-vec.y) * Math.acos(d0 / vec.length()) * (double) (180F / (float) Math.PI);
                foxie.setXRot((float) d1);
            }
        }

        if (prey != null && foxie.distanceTo(prey) <= 2.0F) {
            foxie.doHurtTarget(prey);
        } else if (foxie.getXRot() > 0.0F && foxie.isOnGround() && (float) foxie.getDeltaMovement().y != 0.0F && foxie.level.getBlockState(foxie.blockPosition()).is(Blocks.SNOW)) {
            foxie.setXRot(60.0F);
            foxie.setTarget(null);
            foxie.setFlag(FoxieAIControl.FACEPLANTED, true);
        }
    }
}
