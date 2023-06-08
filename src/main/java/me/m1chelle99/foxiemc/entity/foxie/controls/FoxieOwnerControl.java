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

    public void isTame() {
        this.foxie.isTame();
    }

    public boolean isOwner(UUID uuid) {
        return this.foxie.getOwnerUUID() == uuid;
    }

    public void tryTame(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild)
            stack.shrink(1);

        var success = this.foxie.getRandom().nextInt(5) == 0;
        if (!success || ForgeEventFactory.onAnimalTame(this.foxie, player)) {
            this.foxie.level.broadcastEntityEvent(this.foxie, EntityEvent.TAMING_FAILED);
            return;
        }

        this.foxie.tame(player);
        this.foxie.getNavigation().stop();
        this.foxie.setTarget(null);
        this.foxie.setOrderedToSit(true);
        this.foxie.level.broadcastEntityEvent(this.foxie, EntityEvent.TAMING_SUCCEEDED);
    }

    // TODO: Handle both client but server
    public boolean canInteract(Player player) {
        if (!this.isOwner(player.getUUID())) return false;
        if (this.foxie.aiControl.isSleeping()) return true;
        return this.foxie.aiControl.canBeCommanded();
    }

    public InteractionResult wakeUp() {
        this.foxie.aiControl.setCommand(FoxieConstants.COMMAND_SIT);
        return InteractionResult.SUCCESS;
    }

    public InteractionResult interact() {
        if (this.foxie.aiControl.isSleeping())
            return this.wakeUp();

        if (!this.foxie.aiControl.canBeCommanded())
            return InteractionResult.PASS;

        if (!this.foxie.aiControl.isCommanded())
            this.foxie.aiControl.setCommand(FoxieConstants.COMMAND_SIT);
        else
            this.foxie.aiControl.setCommand(FoxieConstants.COMMAND_NONE);

        return InteractionResult.PASS;
    }
}
