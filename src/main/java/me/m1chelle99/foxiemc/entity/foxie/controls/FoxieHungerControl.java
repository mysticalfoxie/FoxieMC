package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FoxieHungerControl {
    private final Foxie foxie;

    public FoxieHungerControl(Foxie foxie) {

        this.foxie = foxie;
    }

    public boolean isYummy(ItemStack stack) {
        // Todo: Implement more c: foxie likes a lot of food
        return stack.is(ItemTags.FOX_FOOD);
    }

    // TODO: This has to be more precise. For example foxie should prefer always the better food. 
    // It can't be that foxie likes rotten flesh over chicken for example! 
    // I need some sort of Ranking here ;) [That can become a player item as well, some sort of receipt]
    public boolean isEdible(ItemStack stack) {
        return stack.isEdible();
    }

    private void eatItemInMouth() {
        var item = this.foxie.mouthControl.getItem();
        var food = item.getFoodProperties(this.foxie);
        assert food != null;

        var nutrition = (float) food.getNutrition();

        this.foxie.mouthControl.eatItem();
        this.foxie.stateControl.setTicksSinceLastFood(0);

        // TODO: Implement over a span of ticks! UwU #tomakeitperfect
        this.foxie.playSound(SoundEvents.FOX_EAT, 1.0F, 1.0F);

        this.foxie.level.broadcastEntityEvent(this.foxie, EntityEvent.FOX_EAT);
        this.foxie.heal(nutrition);
    }

    private void eatItemFromHand(ItemStack stack) {
        var food = stack.getFoodProperties(this.foxie);
        assert food != null;

        var nutrition = (float) food.getNutrition();

        // TODO: To make it perfect: First in mouth, then eating animation, then next one.

        stack.shrink(-1);
        this.foxie.stateControl.setTicksSinceLastFood(0);
        this.foxie.playSound(SoundEvents.FOX_EAT, 1.0F, 1.0F);

        this.foxie.level.broadcastEntityEvent(this.foxie, EntityEvent.FOX_EAT);
        this.foxie.heal(nutrition);
    }

    public boolean canInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (!this.foxie.aiControl.canEat()) return false;
        var item = player.getMainHandItem();
        return this.isEdible(item);
    }

    public InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        this.eatItemInMouth();
        return InteractionResult.CONSUME;
    }

    private void tryEat() {
        if (!this.foxie.stateControl.isHungry()) return;
        if (!this.foxie.aiControl.canEat()) return;
        if (!this.foxie.mouthControl.hasItem()) return;

        var item = this.foxie.mouthControl.getItem();

        if (!this.isEdible(item)) return;

        if (this.isYummy(item)) {
            this.eatItemInMouth();
            return;
        }

        if (!this.foxie.stateControl.isHeavilyHungry()) return;

        this.eatItemInMouth();
    }

    public void tick() {
        if (this.foxie.level.isClientSide) return;
        if (!this.foxie.isAlive()) return;
        if (!this.foxie.isEffectiveAi()) return;

        var ticksSinceEaten = this.foxie.stateControl.getTicksSinceLastEaten();
        this.foxie.stateControl.setTicksSinceLastFood(++ticksSinceEaten);

        this.tryEat();
    }
}
