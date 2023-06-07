package me.m1chelle99.foxiemc.entity.foxie.goals_old;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.controls.FoxieAIControl;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FoxieEatBerriesGoal extends MoveToBlockGoal {
    private final Foxie foxie;
    protected int ticksWaited;

    public FoxieEatBerriesGoal(Foxie foxie, double speedModifier, int searchRange, int verticalSearchRange) {
        super(foxie, speedModifier, searchRange, verticalSearchRange);
        this.foxie = foxie;
    }

    public double acceptedDistance() {
        return 2.0D;
    }

    public boolean shouldRecalculatePath() {
        return this.tryTicks % 100 == 0;
    }

    protected boolean isValidTarget(LevelReader reader, @NotNull BlockPos pos) {
        var block = reader.getBlockState(pos);
        return block.is(Blocks.SWEET_BERRY_BUSH)
                && block.getValue(SweetBerryBushBlock.AGE) >= 2
                || CaveVines.hasGlowBerries(block);
    }

    public void tick() {
        if (!this.isReachedTarget() && !this.isReachedTarget() && foxie.getRandom().nextFloat() < 0.05F) {
            foxie.playSound(SoundEvents.FOX_SNIFF, 1.0F, 1.0F);
            super.tick();
            return;
        }

        if (this.ticksWaited >= 40)
            this.onReachedTarget();
        else
            ++this.ticksWaited;

        super.tick();
    }

    protected void onReachedTarget() {
        if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(foxie.level, foxie)) {
            BlockState blockstate = foxie.level.getBlockState(this.blockPos);
            if (blockstate.is(Blocks.SWEET_BERRY_BUSH)) {
                this.pickSweetBerries(blockstate);
            } else if (CaveVines.hasGlowBerries(blockstate)) {
                this.pickGlowBerry(blockstate);
            }

        }
    }

    private void pickGlowBerry(BlockState block) {
        CaveVines.use(block, foxie.level, this.blockPos);
    }

    private void pickSweetBerries(BlockState block) {
        int i = block.getValue(SweetBerryBushBlock.AGE);
        block.setValue(SweetBerryBushBlock.AGE, Integer.valueOf(1));
        int j = 1 + foxie.level.random.nextInt(2) + (i == 3 ? 1 : 0);
        var items = foxie.getItemBySlot(EquipmentSlot.MAINHAND);
        if (items.isEmpty()) {
            foxie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.SWEET_BERRIES));
            --j;
        }

        if (j > 0)
            Block.popResource(foxie.level, this.blockPos, new ItemStack(Items.SWEET_BERRIES, j));

        foxie.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
        foxie.level.setBlock(this.blockPos, block.setValue(SweetBerryBushBlock.AGE, 1), 2);
    }

    public boolean canUse() {
        return !foxie.getFlag(FoxieAIControl.SLEEPING)
                && !foxie.getFlag(FoxieAIControl.COMMAND_DOWN)
                && super.canUse();
    }

    public void start() {
        this.ticksWaited = 0;
        foxie.setFlag(FoxieAIControl.SITTING, false);
        super.start();
    }
}
