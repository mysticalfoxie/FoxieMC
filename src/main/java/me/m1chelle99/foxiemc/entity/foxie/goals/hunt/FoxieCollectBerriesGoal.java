package me.m1chelle99.foxiemc.entity.foxie.goals.hunt;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;

import java.util.EnumSet;

public class FoxieCollectBerriesGoal extends Goal {
    private final Foxie _foxie;

    public FoxieCollectBerriesGoal(Foxie foxie) {
        this._foxie = foxie;

        var flags = EnumSet.of(
            Goal.Flag.MOVE,
            Goal.Flag.LOOK
        );

        this.setFlags(flags);
    }

    public boolean canUse() {
        return this._foxie.huntControl.berries != null
            && this._foxie.aiControl.canSearchForFood()
            && this._foxie.hungerControl.isHungry();
    }

    public boolean canContinueToUse() {
        return this._foxie.huntControl.berries != null
            && this._foxie.aiControl.canSearchForFood()
            && this.isBerryBushEatable();
    }

    public void stop() {
        this._foxie.huntControl.berries = null;
    }

    public void tick() {
        var pos = this._foxie.huntControl.berries;
        var navigator = this._foxie.getNavigation();
        if (navigator.isDone()) {
            var path = navigator.createPath(pos, 2);
            navigator.moveTo(path, 1F);
        }

        this._foxie
            .getLookControl()
            .setLookAt(pos.getX(), pos.getY(), pos.getZ());

        var distance = this._foxie.distanceToSqr(
            pos.getX(),
            pos.getY(),
            pos.getZ()
        );

        if (distance > 2D) return;

        this.pickBerries(pos);
    }

    private void pickBerries(BlockPos berries) {
        var state = this._foxie.level.getBlockState(berries);
        state.setValue(SweetBerryBushBlock.AGE, 1);
        var item = new ItemStack(Items.SWEET_BERRIES, 1);
        this._foxie.mouthControl.takeItem(item);

        var sound = SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES;
        this._foxie.playSound(sound, 1.0F, 1.0F);
        this._foxie.level.setBlock(berries, state, 2);

        this._foxie.huntControl.berries = null;
    }

    private boolean isBerryBushEatable() {
        var pos = this._foxie.huntControl.berries;
        var state = this._foxie.level.getBlockState(pos);
        if (!state.is(Blocks.SWEET_BERRY_BUSH)) return false;
        return state.getValue(SweetBerryBushBlock.AGE) >= 2;
    }
}
