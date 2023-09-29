package me.m1chelle99.foxiemc.entity.foxie.goals;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import me.m1chelle99.foxiemc.helper.RandomHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public final class FoxieFollowOwnerGoal extends Goal {
    
    private final Foxie _foxie;
    private LivingEntity owner;
    private float waterMalus;
    private int recalculationIn;
    
    public FoxieFollowOwnerGoal(Foxie foxie) {
        this._foxie = foxie;
    }
    
    @Override
    public boolean canUse() {
        if (!this._foxie.aiControl.canFollowPlayer()) return false;
        this.getOwner();
        if (this.owner == null) return false;
        var maxDistance = FoxieConstants.MAX_DIST_TO_OWNER;
        return this.owner.distanceTo(this._foxie) >= maxDistance;
    }
    
    @Override 
    public boolean canContinueToUse() {
        if (!this._foxie.aiControl.canFollowPlayer()) return false;
        this.getOwner();
        if (this.owner == null) return false;
        var maxDistance = FoxieConstants.MAX_DIST_TO_OWNER;
        return this.owner.distanceTo(this._foxie) >= maxDistance;
    }
    
    public void start() {
        this.recalculationIn = 0;
        this.waterMalus = this._foxie.getPathfindingMalus(BlockPathTypes.WATER);
        this._foxie.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }
    
    @Override
    public void stop() {
        this.owner = null;
        this._foxie.getNavigation().stop();
        this._foxie.setPathfindingMalus(BlockPathTypes.WATER, this.waterMalus);
    }
    
    public void tick() {
        var maxRotation = (float)this._foxie.getMaxHeadXRot();
        this._foxie.getLookControl().setLookAt(this.owner, 10.0F, maxRotation);
        
        if (--this.recalculationIn > 0) return;
        this.recalculationIn = 20;
        
        if (this._foxie.isLeashed()) return;
        if (this._foxie.isPassenger()) return;
        
        if (this._foxie.distanceTo(this.owner) >= 50.0D) {
            this.teleport();
            return;
        }
            
        var distance = this._foxie.distanceToSqr(this.owner);
        var movementSpeed = this.getMovementSpeedByDistance(distance);
        this._foxie.getNavigation().moveTo(this.owner, movementSpeed);
    }

    private float getMovementSpeedByDistance(double distance) {
        if (distance < 8) 
            return 1.1F;
        if (distance < 16)
            return 1.2F;
        if (distance < 24)
            return 1.3F;
        
        return 1.4F;
    }

    private void getOwner() {
        var owner = this._foxie.getOwner();
        if (owner == null) return;        
        if (owner.isSpectator()) return;
        if (!owner.isAlive()) return;
        this.owner = owner;
    }
    
    private void teleport() {
        var blockpos = this.owner.blockPosition();
        
        for (int i = 0; i < 10; ++i) {
            var xMod = RandomHelper.nextFloat(-5, 5);
            var yMod = RandomHelper.nextFloat(-2, 2);
            var zMod = RandomHelper.nextFloat(-5, 5);
            
            var succeeded = this.tryTeleport(
                (int)(blockpos.getX() + xMod),
                (int)(blockpos.getY() + yMod),
                (int)(blockpos.getZ() + zMod)
            );
            
            if (succeeded)
                return;
        }
    }
    
    private boolean tryTeleport(int x, int y, int z) {
        if (Math.abs((double)x - this.owner.getX()) < 2.0D && 
            Math.abs((double)y - this.owner.getZ()) < 2.0D)
            return false;

        var position = new BlockPos(x, y, z);
        var level = this._foxie.level;
        var foxie = this._foxie;
        var types = WalkNodeEvaluator.getBlockPathTypeStatic(
            level, position.mutable());
        
        if (types != BlockPathTypes.WALKABLE) 
            return false;
        
        var state = level.getBlockState(position.below());
        if (state.getBlock() instanceof LeavesBlock) 
            return false;

        foxie.moveTo(x, y, z, foxie.getYRot(), foxie.getXRot());
        foxie.getNavigation().stop();
        return true;
    }
}
