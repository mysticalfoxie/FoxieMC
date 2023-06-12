package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.UUID;

public class FoxieOwnerControl {
    private final Foxie foxie;

    public FoxieOwnerControl(Foxie foxie) {
        this.foxie = foxie;
        this.foxie.setTame(false);
    }

    public boolean isTame() {
        return this.foxie.isTame();
    }

    public boolean isTamable() {
        if (!this.foxie.aiControl.isTamable()) return false;

        // TODO: Test: Foxie should drop less quality food for the taming food
        var item = this.foxie.mouthControl.getItem();
        return !this.foxie.hungerControl.isYummy(item);
    }

    public boolean isOwner(UUID uuid) {
        return this.foxie.getOwnerUUID() == uuid;
    }

    public void tryTame(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild)
            stack.shrink(1);

        var success = this.foxie.getRandom().nextInt(5) == 0;
        if (!success || ForgeEventFactory.onAnimalTame(this.foxie, player)) {
            this.foxie.level.broadcastEntityEvent(
                this.foxie, 
                EntityEvent.TAMING_FAILED
            );
            
            return;
        }

        this.foxie.tame(player);
        this.foxie.getNavigation().stop();
        this.foxie.setTarget(null);
        this.foxie.setOrderedToSit(true);
        this.foxie.level.broadcastEntityEvent(
            this.foxie, 
            EntityEvent.TAMING_SUCCEEDED
        );
    }

    // TODO: Handle both client but server
    public boolean canInteract(Player player) {
        if (this.isTame() && this.isOwner(player.getUUID()))
            return this.canOwnerInteract();
        else
            return !this.canStrangerFeed(player) 
                && !this.canStrangerTame(player);
    }

    public boolean canStrangerTame(Player player) {
        if (this.isTame()) return false;
        var item = player.getMainHandItem();
        if (!this.foxie.hungerControl.isYummy(item)) return false;
        if (!this.foxie.hungerControl.isHungry()) return false;
        if (!this.foxie.aiControl.canEat()) return false;
        return !this.isTamable();
    }

    public boolean canStrangerFeed(Player player) {
        var item = player.getMainHandItem();
        if (!this.foxie.hungerControl.isEdible(item)) return false;
        if (!this.foxie.hungerControl.isHungry()) return false;
        return this.foxie.aiControl.canEat();
    }

    public boolean canOwnerInteract() {
        if (this.foxie.aiControl.canEat()) return true;
        return this.foxie.aiControl.canBeCommanded();
    }

    private void strangerFeedInteract(Player player) {
        var item = player.getMainHandItem();
        this.foxie.mouthControl.takeItem(item);
    }

    private InteractionResult ownerInteract(Player player) {
        if (this.foxie.aiControl.isSleeping()) {
            this.foxie.aiControl.setCommand(FoxieConstants.COMMAND_SIT);
            return InteractionResult.SUCCESS;
        }

        var item = player.getMainHandItem();
        if (this.foxie.hungerControl.isEdible(item)) {
            if (this.foxie.hungerControl.isHungry()) {
                this.foxie.hungerControl.eatItemFromHand(player);
                return InteractionResult.CONSUME;
            }

            return InteractionResult.PASS;
        }

        if (!this.foxie.aiControl.canBeCommanded())
            return InteractionResult.PASS;

        if (!this.foxie.aiControl.isCommanded())
            this.foxie.aiControl.setCommand(FoxieConstants.COMMAND_SIT);
        else
            this.foxie.aiControl.setCommand(FoxieConstants.COMMAND_NONE);

        return InteractionResult.SUCCESS;
    }

    public InteractionResult interact(Player player) {
        if (this.isTame() && this.isOwner(player.getUUID()))
            return this.ownerInteract(player);

        if (this.canStrangerTame(player)) {
            this.tryTame(player, player.getMainHandItem());
            return InteractionResult.CONSUME;
        }

        if (this.canStrangerFeed(player)) {
            this.strangerFeedInteract(player);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.FAIL;
    }
}
