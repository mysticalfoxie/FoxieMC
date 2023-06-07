package me.m1chelle99.foxiemc.entities.foxie.goals;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.FoxieConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class FoxieDefaultPanicGoal extends Goal {
    private final Foxie foxie;
    private int target_distance;
    private int cooldown;
    private boolean fire_is_cause;
    private LivingEntity attacker;

    public FoxieDefaultPanicGoal(Foxie foxie) {
        this.foxie = foxie;
    }

    public boolean canUse() {
        if (this.foxie.getLastHurtByMob() != null) {
            this.attacker = this.foxie.getLastHurtByMob();
            return true;
        }
        
        if (this.foxie.isFreezing())
            return this.findRandomPosition();
        
        if (!this.foxie.isOnFire())
            return this.findRandomPosition();
        
        var water = this.lookForWater(this.foxie.level, this.foxie, 5);
        if (water != null) {
            this.target = new Vec3(water.getX(), water.getY(), water.getZ());
            return true;
        }

        return this.findRandomPosition();
    }
        
    protected boolean findRandomPosition() {
        var point = DefaultRandomPos.getPos(this.foxie, 5, 4);
        if (point == null) 
            return false;
        
        this.target = point;
        return true;
    }

    public void start() {        
        if (this.foxie.isFreezing()) {
            this.cooldown = this.foxie.getRandomTicksWithin(2, 4);
            this.target = this.foxie.getRandomTargetWithin(3);
            this.foxie.runTo(this.target, FoxieConstants.MS_PANIC_MULTIPLIER);
            return;
        }

        if (this.foxie.isOnFire()) {
        }
        
    ;   if (this.foxie.getLastHurtByMob() != null) {
            this.attacker = this.foxie.getLastHurtByMob();
            this.cooldown = this.foxie.getRandomTicksWithin(6, 15);
            this.setRandomPosAwayFromAttacker();
            this.foxie.runTo(this.target, FoxieConstants.MS_PANIC_MULTIPLIER);
            return;
        }
        
        this.cooldown = this.foxie.getRandomTicksWithin(2, 5);
        this.target = this.foxie.getRandomTargetWithin(3);
        this.foxie.runTo(this.target, FoxieConstants.MS_PANIC_MULTIPLIER);
    }
    
    public void stop() {
        this.target = null;
        this.attacker = null;
        this.cooldown = 0;
    }
    
    public void setRandomPosAwayFromAttacker() {
        this.target = DefaultRandomPos.getPosAway(this.foxie, 6, 7, this.attacker.position());
        this.target_distance = 6;
    }

    public boolean requiresUpdateEveryTick() { return true; }
    
    public boolean canContinueToUse() { return this.cooldown > 0; }
    
    public void tick() {
        
    }
}
