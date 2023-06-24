package me.m1chelle99.foxiemc.entity.foxie.goals.hunt;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieActivities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

public abstract class FoxieAbstractSearchForFoodGoal extends Goal {

    protected final Foxie _foxie;

    public FoxieAbstractSearchForFoodGoal(Foxie foxie) {

        this._foxie = foxie;
    }

    protected LivingEntity _prey;
    protected ItemEntity _foodItem;
    protected Player _player;
    protected BlockPos _berries;

    @Override
    public boolean canUse() {
        if (!this._foxie.aiControl.canSearchForFood()) return false;
        if (!this._foxie.hungerControl.isHungry()) return false;
        return !this._foxie.getNavigation().isDone();
    }

    @Override
    public boolean canContinueToUse() {
        if (!this._foxie.aiControl.canSearchForFood()) return false;
        return this._foxie.hungerControl.isHungry();
    }

    @Override
    public void start() {
        this._foxie.aiControl.startActivity(FoxieActivities.SearchForFood);
    }
}
