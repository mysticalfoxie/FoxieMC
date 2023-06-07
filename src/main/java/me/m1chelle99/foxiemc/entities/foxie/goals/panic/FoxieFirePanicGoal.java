package me.m1chelle99.foxiemc.entities.foxie.goals.panic;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.FoxieConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class FoxieFirePanicGoal extends FoxieAbstractPanicGoal {
    public FoxieFirePanicGoal(Foxie foxie) {
        super(foxie);
    }

    @Override
    public boolean canUse() { return super.canUse() &&  this.foxie.isOnFire(); }

    @Override
    public void setNewTarget() {
        this.setWaterAsTarget();
        if (this.target == null)
            this.target = this.foxie.getRandomTargetWithin(3);
    }

    @Override
    public void setCooldown() {
        this.cooldown = this.foxie.getRandomTicksWithin(3, 8);
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
}
