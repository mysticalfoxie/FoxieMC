package me.m1chelle99.foxiemc.entity.foxie.goals.panic;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.helper.EntityHelper;
import me.m1chelle99.foxiemc.helper.Pathfinder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.phys.Vec3;

public class FoxieFirePanicGoal extends FoxieAbstractPanicGoal {
    public FoxieFirePanicGoal(Foxie foxie) {
        super(foxie);
    }

    @Override
    public boolean canUse() {
        return super.canUse()
                && this._foxie.isOnFire();
    }

    @Override
    public void setNewTarget() {
        this.setWaterAsTarget();

        if (this._target != null)
            return;

        var target = Pathfinder
                .getPathInLookDirection(this._foxie, 20, 4, 10);

        if (target == null)
            return;

        this._target = Vec3.atCenterOf(target);
    }

    @Override
    public void setCooldown() {
        this._cooldown = EntityHelper.getRandomTicksWithin(this._foxie, 3, 8);
    }

    public void setWaterAsTarget() {
        var position = this._foxie.blockPosition();
        var level = this._foxie.level;
        var current_block = level.getBlockState(position);
        var collision = current_block.getCollisionShape(level, position);
        if (!collision.isEmpty())
            return;

        var match = BlockPos.findClosestMatch(position, 10, 3,
                x -> this._foxie.level.getFluidState(x).is(FluidTags.WATER));
        if (match.isEmpty()) return;

        this._target = new Vec3(
                match.get().getX(),
                match.get().getY(),
                match.get().getZ()
        );
    }
}
