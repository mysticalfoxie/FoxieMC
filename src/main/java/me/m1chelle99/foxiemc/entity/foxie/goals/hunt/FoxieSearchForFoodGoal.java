package me.m1chelle99.foxiemc.entity.foxie.goals.hunt;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieActivities;
import me.m1chelle99.foxiemc.helper.Pathfinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.phys.AABB;

public class FoxieSearchForFoodGoal extends Goal {
    private final TargetingConditions _preyTargetingContext
        = TargetingConditions
        .forNonCombat()
        .range(20)
        .selector(this::isPrey);

    private final Foxie _foxie;

    public FoxieSearchForFoodGoal(Foxie foxie) {

        this._foxie = foxie;
    }

    private boolean isPrey(LivingEntity livingEntity) {
        if (livingEntity instanceof Sheep) return true;
        if (livingEntity instanceof Chicken) return true;
        if (livingEntity instanceof Rabbit) return true;
        return livingEntity instanceof AbstractFish;
    }

    @Override
    public boolean canUse() {
        if (!this._foxie.aiControl.canSearchForFood()) return false;
        if (!this._foxie.hungerControl.isHungry()) return false;
        if (!this._foxie.getNavigation().isDone()) return false;
        return !this._foxie.huntControl.foundFood();
    }

    @Override
    public boolean canContinueToUse() {
        if (!this._foxie.aiControl.canSearchForFood()) return false;
        if (!this._foxie.hungerControl.isHungry()) return false;
        return !this._foxie.huntControl.foundFood();
    }

    @Override
    public void start() {
        this._foxie.aiControl.startActivity(FoxieActivities.SearchForFood);
    }

    public void tick() {
        if (this._foxie.getNavigation().isInProgress())
            return;
        if (this._foxie.getRandom().nextFloat() > .04F)
            return;

        var boundary = this._foxie.getBoundingBox().inflate(20, 10, 20);

        this._foxie.huntControl.prey = this.findPrey(boundary);
        if (this._foxie.huntControl.prey != null)
            return;

        this._foxie.huntControl.berries = this.findBerries(boundary);
        if (this._foxie.huntControl.berries != null)
            return;

        this._foxie.huntControl.foodItem = this.findFoodItems(boundary);
        if (this._foxie.huntControl.foodItem != null)
            return;

        var random = Pathfinder.getRandomPositionWithin(this._foxie, 20, 5, 3);
        if (random == null)
            return;

        this._foxie.runTo(random, 1.0F);
    }

    public BlockPos findBerries(AABB boundary) {
        BlockPos match = null;
        double distance = Double.MAX_VALUE;
        for (var x = boundary.minX; x < boundary.maxX; x++)
            for (var y = boundary.minY; y < boundary.maxY; y++)
                for (var z = boundary.minZ; z < boundary.maxZ; z++) {
                    var block = new BlockPos(x, y, z);
                    var state = this._foxie.level.getBlockState(block);
                    if (!state.is(Blocks.SWEET_BERRY_BUSH))
                        continue;

                    if (state.getValue(SweetBerryBushBlock.AGE) < 2)
                        continue;

                    if (match == null) {
                        match = block;
                        distance = block.distManhattan(match);
                        continue;
                    }

                    var dist = block.distManhattan(match);
                    if (dist < distance) {
                        match = block;
                        distance = dist;
                    }
                }

        return match;
    }

    public ItemEntity findFoodItems(AABB boundary) {
        var entities = this._foxie.level.getEntities(
            this._foxie,
            boundary,
            this::isFoodItem);

        if (entities.isEmpty())
            return null;

        ItemEntity item = null;
        double distance = Double.MAX_VALUE;
        for (var entity : entities) {
            var dist = entity.distanceTo(this._foxie);
            if (dist < distance) {
                item = (ItemEntity) entity;
                distance = dist;
            }
        }

        return item;
    }

    // TODO: Prefer "yummy" items over normal food items (v1.1.0)
    private boolean isFoodItem(Entity entity) {
        if (!(entity instanceof ItemEntity itemEntity))
            return false;

        return this._foxie.hungerControl.isEdible(itemEntity.getItem());
    }

    public LivingEntity findPrey(AABB boundary) {
        return this._foxie.level.getNearestEntity(
            Animal.class,
            this._preyTargetingContext,
            this._foxie,
            this._foxie.getX(),
            this._foxie.getY(),
            this._foxie.getZ(),
            boundary
        );
    }
}
