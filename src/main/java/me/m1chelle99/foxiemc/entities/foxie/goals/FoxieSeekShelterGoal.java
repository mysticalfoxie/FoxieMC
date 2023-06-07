package me.m1chelle99.foxiemc.entities.foxie.goals;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
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
        if (!this.foxie.stateControl.canSeekShelter()) return false;
        if (!this.foxie.level.isRaining() && !this.foxie.level.isThundering()) return false;
        return this.standsInRain();
    }

    @Override
    public void start() {
        this.target = this.findShelter();
        this.foxie.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), 1.0F);
    }
    
    public void 

    // TODO: WHITEBOX TESTING!!!! URGENTLY
    public BlockPos findShelter() {

        var position_start = this.foxie.blockPosition();

        for (int distance = 1; distance < 5; distance++) {

            // Apply radius first before searching
            var current_x = position_start.getX();
            var current_z = position_start.getZ() + distance;

            for (int tl_to_tr = -distance; tl_to_tr < distance; tl_to_tr++) {
                var top_solid = this.foxie.level.getHeight(Heightmap.Types.WORLD_SURFACE, current_x, current_z);
                if (this.hasCover(current_x, top_solid, current_z) && this.canNavigateTo(current_x, top_solid, current_z))
                    return new BlockPos(current_x, top_solid, current_z);
                current_x++;
            }

            for (int tr_to_br = -distance; tr_to_br < distance; tr_to_br++) {
                var top_solid = this.foxie.level.getHeight(Heightmap.Types.WORLD_SURFACE, current_x, current_z);
                if (this.hasCover(current_x, top_solid, current_z) && this.canNavigateTo(current_x, top_solid, current_z))
                    return new BlockPos(current_x, top_solid, current_z);
                current_z--;
            }

            for (int br_to_bl = distance; br_to_bl > -distance; br_to_bl--) {
                var top_solid = this.foxie.level.getHeight(Heightmap.Types.WORLD_SURFACE, current_x, current_z);
                if (this.hasCover(current_x, top_solid, current_z) && this.canNavigateTo(current_x, top_solid, current_z))
                    return new BlockPos(current_x, top_solid, current_z);
                current_x--;
            }

            for (int bl_to_tl = distance; bl_to_tl > -distance; bl_to_tl--) {
                var top_solid = this.foxie.level.getHeight(Heightmap.Types.WORLD_SURFACE, current_x, current_z);
                if (this.hasCover(current_x, top_solid, current_z) && this.canNavigateTo(current_x, top_solid, current_z))
                    return new BlockPos(current_x, top_solid, current_z);
                current_z++;
            }

            // No shelter found in current radius
        }
        
        return null;
    }

    public boolean hasCover(int x, int y, int z) {
        var position_current = new BlockPos(x, y, z);

        for (int i = 0; i < 32; i++) {
            if (!this.foxie.level.getBlockState(position_current).isAir())
                return true;
            position_current = position_current.above();
        }

        return false;
    }

    public boolean canNavigateTo(int x, int y, int z) {
        var pos = new BlockPos(x, y, z);
        var path = this.foxie.getNavigation().createPath(pos, 10);
        if (path == null) return false;
        return path.canReach();
    }

    public boolean standsInRain() {
        var pos = this.foxie.blockPosition();
        return !this.hasCover(pos.getX(), pos.getY(), pos.getZ());
    }
}
