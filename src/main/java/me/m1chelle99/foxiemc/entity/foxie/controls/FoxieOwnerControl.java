package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public final class FoxieOwnerControl {
    private final Foxie _foxie;

    public FoxieOwnerControl(Foxie foxie) {
        this._foxie = foxie;
        this._foxie.setTame(false);
    }

    public boolean isTame() {
        return this._foxie.isTame();
    }

    public boolean isTamable() {
        if (!this._foxie.aiControl.isTamable()) return false;
        if (!this._foxie.mouthControl.hasItem()) return true;

        var item = this._foxie.mouthControl.getItem();
        return !this._foxie.hungerControl.isYummy(item);
    }

    public boolean isOwner(UUID uuid) {
        return this._foxie.getOwnerUUID() == uuid;
    }

    public void tryTame(Player player, ItemStack stack) {
        this._foxie.mouthControl.takeItem(stack);

        var success = this._foxie.getRandom().nextInt(5) == 0;
        if (!success) {
            this._foxie.level.broadcastEntityEvent(
                this._foxie,
                EntityEvent.TAMING_FAILED
            );

            return;
        }

        this._foxie.tame(player);
        this._foxie.getNavigation().stop();
        this._foxie.aiControl.setCommand(FoxieConstants.COMMAND_SIT);
        this._foxie.level.broadcastEntityEvent(
            this._foxie,
            EntityEvent.TAMING_SUCCEEDED
        );
    }

    // TODO: Handle both client but server
    public boolean canInteract(Player player) {
        if (this.isTame() && this.isOwner(player.getUUID()))
            return this.canOwnerInteract();

        return this.canStrangerTame(player);
    }

    public boolean canStrangerTame(Player player) {
        if (this.isTame()) return false;
        var item = player.getMainHandItem();
        if (!this._foxie.hungerControl.isYummy(item)) return false;
        if (!this._foxie.aiControl.canEat()) return false;
        return this.isTamable();
    }

    public boolean canOwnerInteract() {
        if (this._foxie.aiControl.canEat()) return true;
        return this._foxie.aiControl.canBeCommanded();
    }

    private InteractionResult ownerInteract(Player player) {
        if (this._foxie.aiControl.isSleeping()) {
            this._foxie.aiControl.setCommand(FoxieConstants.COMMAND_SIT);
            return InteractionResult.SUCCESS;
        }

        var item = player.getMainHandItem();
        if (this._foxie.hungerControl.isEdible(item)
            && this._foxie.hungerControl.isHungry()) {
            this._foxie.mouthControl.takeItem(item);
            return InteractionResult.SUCCESS;
        }

        if (!this._foxie.aiControl.canBeCommanded())
            return InteractionResult.SUCCESS;

        if (!this._foxie.aiControl.isCommanded())
            this._foxie.aiControl.setCommand(FoxieConstants.COMMAND_SIT);
        else
            this._foxie.aiControl.setCommand(FoxieConstants.COMMAND_NONE);

        return InteractionResult.PASS;
    }

    public InteractionResult interact(Player player) {
        if (this.isTame() && this.isOwner(player.getUUID()))
            return this.ownerInteract(player);

        if (this.canStrangerTame(player)) {
            this.tryTame(player, player.getMainHandItem());
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.SUCCESS;
    }
}
