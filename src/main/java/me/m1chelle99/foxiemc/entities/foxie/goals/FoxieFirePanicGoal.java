package me.m1chelle99.foxiemc.entities.foxie.goals;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.FoxieConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class FoxieFirePanicGoal extends Goal {
    private final Foxie foxie;
    private Vec3 target;
    private int cooldown;

    public FoxieFirePanicGoal(Foxie foxie) {

        this.foxie = foxie;
    }
    
    public boolean canUse() {
        return this.foxie.isOnFire();
    }

    public void start() {
        this.cooldown = this.foxie.getRandomTicksWithin(3, 8);
        this.setNewTarget();
        this.foxie.runTo(this.target, FoxieConstants.MS_PANIC_MULTIPLIER);
    }
    
    public void setNewTarget() {
        this.setWaterAsTarget();
        if (this.target == null)
            this.target = this.foxie.getRandomTargetWithin(3);
    }
    
    public void setWaterAsTarget() {
        var position = this.foxie.blockPosition();
        var current_block = this.foxie.level.getBlockState(position);
        var collision = current_block.getCollisionShape(this.foxie.level, position);
        if (!collision.isEmpty())
            return;

        var match = BlockPos.findClosestMatch(position, 10, 3, x -> this.foxie.level.getFluidState(x).is(FluidTags.WATER));
        if (match.isEmpty()) return;

        this.target = new Vec3(match.get().getX(), match.get().getY(), match.get().getZ());
    }
    
    public void stop() {
        this.target = null;
        this.cooldown = 0;
    }
    
    public boolean requiresUpdateEveryTick() { return true; }

    public boolean canContinueToUse() { return this.cooldown > 0 || this.foxie.isDeadOrDying(); }
    
    public void tick() {
        if (this.foxie.level.isClientSide) return;
        if (this.cooldown <= 0) return;
        this.cooldown--;
        
        if (!this.foxie.getNavigation().isDone()) return;
        this.setNewTarget();
    }
}
