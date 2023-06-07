package me.m1chelle99.foxiemc.entities.foxie.goals;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.goals.panic.FoxieAbstractPanicGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

public class FoxieSeekShelterGoal extends Goal {
    private final Foxie foxie;

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
        
    } 
    
    // TODO: WHITEBOX TESTING!!!! URGENTLY
    public BlockPos findShelter() {
        var position_start = this.foxie.blockPosition();
        var y_start = position_start.getY() - 2;
        var y_end = position_start.getY() + 2;
        
        // Iterate Block Height in range of +/- 2 height
        // TODO: Search from current y first 
        for (int y = y_start; y < y_end; y++) {  
            
            // TODO: Bug: the surface would be cover as well now!
            
            for (int distance = 1; distance < 10; distance++) {
                
                // Apply radius first before searching
                var position_current = new BlockPos(position_start.getX(), position_start.getY(), position_start.getZ());
                for (int front = distance; front > 0; front--)
                    position_current = position_current.north();
                    
                for (int right_right = 0; right_right < distance; right_right++) {
                    if (this.hasCover(position_current))
                        return position_current;
                    position_current = position_start.east();
                }
                
                for (int down = -distance; down < distance; down++) {
                    if (this.hasCover(position_current))
                        return position_current;
                    position_current.south();
                }
                
                for (int left = -distance; left < distance; left++) {
                    if (this.hasCover(position_current))
                        return position_current;
                    position_current.west();                    
                }
                
                for (int top = -distance; top < distance; top++) {
                    if (this.hasCover(position_current))
                        return position_current;
                    position_current.north();
                }
                
                for (int left_right = -distance; left_right < 0; left_right++) {
                    if (this.hasCover(position_current))
                        return position_current;
                    position_current.east();
                }
                
                // No shelter found in current radius 
                
            }
            
            
        }
        
        var position_current = new BlockPos(position.getX(), position.getY(), position.getZ());
    }
    
    public boolean hasCover(BlockPos position) {
        var position_current = new BlockPos(position.getX(), position.getY(), position.getZ());

        for (int i = 0; i < 32; i++) {
            if (!this.foxie.level.getBlockState(position_current).isAir())
                return true;
            position_current = position_current.above();
        }

        return false;
    }
    
    public boolean standsInRain() {
        var position_start = this.foxie.blockPosition();
        return !this.hasCover(position_start);
    }
}
