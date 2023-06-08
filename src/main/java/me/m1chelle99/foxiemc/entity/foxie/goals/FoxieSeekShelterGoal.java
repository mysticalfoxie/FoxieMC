package me.m1chelle99.foxiemc.entity.foxie.goals;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.levelgen.Heightmap;

public class FoxieSeekShelterGoal extends Goal {
    private final Foxie foxie;
    private BlockPos target;

    public FoxieSeekShelterGoal(Foxie foxie) {
        this.foxie = foxie;
    }

    @Override
    public boolean canUse() {
        if (!this.foxie.aiControl.canSeekShelter()) return false;
        if (!this.foxie.level.isRaining() && !this.foxie.level.isThundering()) return false;
        return this.foxie.level.isRainingAt(this.foxie.blockPosition());
    }

    @Override
    public void start() {
        this.foxie.aiControl.startActivity(FoxieConstants.ACTIVITY_SEEK_SHELTER);
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public void tick() {
        if (this.target != null && this.foxie.getNavigation().isInProgress()) return;
        if (this.target != null && !this.foxie.getNavigation().isStuck()) return;

        this.target = this.findNewShelter();
        var navigator = this.foxie.getNavigation();
        navigator.moveTo(target.getX(), target.getY(), target.getZ(), 1.0F);
    }

    @Override
    public boolean canContinueToUse() {
        var pos = this.foxie.blockPosition();
        return this.foxie.level.isRainingAt(pos);
    }

    // TODO: WHITEBOX TESTING!!!! URGENTLY
    public BlockPos findNewShelter() {
        var start = this.foxie.blockPosition();

        for (int distance = 1; distance < 10; distance++) {

            for (int t = 0; t < distance; t++) {
                var x = start.getX();
                var z = start.getZ() + t;
                var y = this.foxie.level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                if (this.foxie.level.isRainingAt(new BlockPos(x, y, z))) continue;
                if (this.isUnreachable(x, y, z)) continue;
                return new BlockPos(x, y, z);
            }

            for (int r = 0; r < distance; r++) {
                var x = start.getX() + r;
                var z = start.getZ();
                var y = this.foxie.level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                if (this.foxie.level.isRainingAt(new BlockPos(x, y, z))) continue;
                if (this.isUnreachable(x, y, z)) continue;
                return new BlockPos(x, y, z);
            }

            for (int b = 0; b < (distance / 2); b++) {
                var x = start.getX();
                var z = start.getZ() - b;
                var y = this.foxie.level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                if (this.foxie.level.isRainingAt(new BlockPos(x, y, z))) continue;
                if (this.isUnreachable(x, y, z)) continue;
                return new BlockPos(x, y, z);
            }

            for (int l = 0; l < distance; l++) {
                var x = start.getX() - l;
                var z = start.getZ();
                var y = this.foxie.level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                if (this.foxie.level.isRainingAt(new BlockPos(x, y, z))) continue;
                if (this.isUnreachable(x, y, z)) continue;
                return new BlockPos(x, y, z);
            }

            // No shelter found in current radius
        }

        return null;
    }

    public boolean isUnreachable(int x, int y, int z) {
        var pos = new BlockPos(x, y, z);
        var path = this.foxie.getNavigation().createPath(pos, 10);
        if (path == null) return true;
        return !path.canReach();
    }
}
