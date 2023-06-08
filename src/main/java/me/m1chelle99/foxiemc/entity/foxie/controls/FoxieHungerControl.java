package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("CommentedOutCode") // TODO: Remove before merge
public class FoxieHungerControl {
    private final Foxie foxie;
    private int _ticksSinceLastEaten = 0;

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
        this._ticksSinceLastEaten = 0;

        // TODO: Implement over a span of ticks! UwU #tomakeitperfect
        this.foxie.playSound(SoundEvents.FOX_EAT, 1.0F, 1.0F);
        this.foxie.mouthControl.summonFoodParticles();
        this.foxie.level.broadcastEntityEvent(this.foxie, EntityEvent.FOX_EAT);
        this.foxie.heal(nutrition);
    }

    private void eatItemFromHand(Player player) {
        var stack = player.getMainHandItem();
        var food = stack.getFoodProperties(this.foxie);
        assert food != null;

        var nutrition = (float) food.getNutrition();

        // TODO: To make it perfect: First in mouth, then eating animation, then next one.

        if (!player.isCreative()) stack.shrink(-1);
        this._ticksSinceLastEaten = 0;
        this.foxie.playSound(SoundEvents.FOX_EAT, 1.0F, 1.0F);
        this.foxie.mouthControl.summonFoodParticles();
        this.foxie.level.broadcastEntityEvent(this.foxie, EntityEvent.FOX_EAT);
        this.foxie.gameEvent(GameEvent.MOB_INTERACT, this.foxie.eyeBlockPosition());
        this.foxie.heal(nutrition);
    }

//    public boolean isPrey(LivingEntity entity) {
//        return entity instanceof Chicken
//                || entity instanceof Rabbit
//                || entity instanceof Sheep;
//    }

    public boolean canInteract(@NotNull Player player) {
        if (!this.foxie.aiControl.canEat()) return false;
        var item = player.getMainHandItem();
        if (!item.isEmpty()) return false;
        return this.isEdible(item) && this.isHungry();
    }

    public int getTicksSinceLastEaten() {
        return this._ticksSinceLastEaten;
    }

    public void setTicksSinceLastFood(int value) {
        this._ticksSinceLastEaten = value;
        this.calculateHungerStrength();
    }

    private void calculateHungerStrength() {
        if (_ticksSinceLastEaten < FoxieConstants.TICKS_UNTIL_SLIGHT_HUNGER)
            this.foxie.dataControl.setHungerStrength(0);
        else if (_ticksSinceLastEaten < FoxieConstants.TICKS_UNTIL_HEAVY_HUNGER)
            this.foxie.dataControl.setHungerStrength(1);
        else
            this.foxie.dataControl.setHungerStrength(2);
    }

    public InteractionResult interact(@NotNull Player player) {
        this.eatItemFromHand(player);
        return InteractionResult.CONSUME;
    }

    private void tryEat() {
        if (!this.isHungry()) return;
        if (!this.foxie.aiControl.canEat()) return;
        if (!this.foxie.mouthControl.hasItem()) return;

        var item = this.foxie.mouthControl.getItem();

        if (!this.isEdible(item)) return;

        if (this.isYummy(item)) {
            this.eatItemInMouth();
            return;
        }

        if (!this.isHeavilyHungry()) return;

        this.eatItemInMouth();
    }

    public boolean isHungry() {
        return this.isHeavilyHungry() || this.isSlightlyHungry();
    }

    public boolean isSlightlyHungry() {
        return this.foxie.dataControl.getHungerStrength() == 1;
    }

    public boolean isHeavilyHungry() {
        return this.foxie.dataControl.getHungerStrength() >= 2;
    }

    public void tick() {
        if (this.foxie.level.isClientSide) return;
        if (!this.foxie.isAlive()) return;
        if (!this.foxie.isEffectiveAi()) return;

        ++_ticksSinceLastEaten;

        this.tryEat();
    }
}
