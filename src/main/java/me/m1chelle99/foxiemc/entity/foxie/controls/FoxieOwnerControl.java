package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FoxieOwnerControl {
    private final Foxie foxie;

    public FoxieOwnerControl(Foxie foxie) {
        this.foxie = foxie;
        this.foxie.setTame(false);
    }

    public void isTame() {

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
    public boolean canInteract(@NotNull Player player) {
        var item = player.getMainHandItem();
        //this.
    }

    public InteractionResult interact(@NotNull Player player) {
        // Give foxie some food
        // TODO: Refuse to eat when tummy's full, even if health is slightly lower
        if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
            var food_data = Objects.requireNonNull(stack.getFoodProperties(this));
            this.heal((float) food_data.getNutrition());
            if (!player.getAbilities().instabuild)
                stack.shrink(1);

            this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());
            return InteractionResult.SUCCESS;
        }

        // Wake foxie up, get up (this is currently both, down command and sleeping, TODO: Change that! c:)
        if (this.getFlag(FoxieAIControl.SLEEPING) || this.getFlag(FoxieAIControl.COMMAND_DOWN)) {
            this.setFlag(FoxieAIControl.SLEEPING, false);
            this.setFlag(FoxieAIControl.COMMAND_DOWN, false);
            // immediately show interest for something when getting up again
            this.setFlag(FoxieAIControl.INTERESTED, true);
            return InteractionResult.SUCCESS;
        }

        // Foxie lay down buddy!
        if (!this.getFlag(FoxieAIControl.COMMAND_DOWN)) {
            this.clearStates();
            this.setFlag(FoxieAIControl.COMMAND_DOWN, true);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
