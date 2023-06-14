package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public final class FoxieHungerControl {
    private final Foxie _foxie;
    private int _ticksSinceLastEaten = 0;
    public FoxieHungerControl(Foxie foxie) {

        this._foxie = foxie;
    }

    public boolean isYummy(ItemStack stack) {
        // Todo: Implement more c: foxie likes a lot of food
        return stack.is(ItemTags.FOX_FOOD);
    }

    // TODO: This has to be more precise. 
    // For example foxie should prefer always the better food. 
    // It can't be that foxie likes rotten flesh over chicken for example! 
    // I need some sort of Ranking here ;) 
    // [That can become a player item as well, some sort of receipt]
    public boolean isEdible(ItemStack stack) {
        return stack.isEdible();
    }

    private void eatItemInMouth() {
        var item = this._foxie.mouthControl.getItem();
        var food = item.getFoodProperties(this._foxie);
        assert food != null;

        var nutrition = (float) food.getNutrition();

        this._foxie.mouthControl.eatItem();
        this._ticksSinceLastEaten = 0;

        // TODO: Implement over a span of ticks! UwU #tomakeitperfect
        this._foxie.playSound(SoundEvents.FOX_EAT, 1.0F, 1.0F);
        this._foxie.mouthControl.summonFoodParticles();
        this._foxie.level.broadcastEntityEvent(this._foxie, EntityEvent.FOX_EAT);
        this._foxie.heal(nutrition);
    }

    public void eatItemFromHand(Player player) {
        var stack = player.getMainHandItem();
        var food = stack.getFoodProperties(this._foxie);
        assert food != null;

        var nutrition = (float) food.getNutrition();

        // TODO: To make it perfect: First in mouth, 
        //  then eating animation, then next one.

        if (!player.isCreative()) stack.shrink(-1);
        this._ticksSinceLastEaten = 0;
        this._foxie.playSound(SoundEvents.FOX_EAT, 1.0F, 1.0F);
        this._foxie.mouthControl.summonFoodParticles();
        this._foxie.level.broadcastEntityEvent(this._foxie, EntityEvent.FOX_EAT);
        var eyeBlockPosition = this._foxie.eyeBlockPosition();
        this._foxie.gameEvent(GameEvent.MOB_INTERACT, eyeBlockPosition);
        this._foxie.heal(nutrition);
    }

    public boolean canInteract(@NotNull Player player) {
        if (!this._foxie.aiControl.canEat()) return false;
        var item = player.getMainHandItem();
        if (item.isEmpty()) return false;
        if (!this.isHungry()) return false;
        return this.isEdible(item);
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
            this._foxie.dataControl.setHungerStrength(0);
        else if (_ticksSinceLastEaten < FoxieConstants.TICKS_UNTIL_HEAVY_HUNGER)
            this._foxie.dataControl.setHungerStrength(1);
        else this._foxie.dataControl.setHungerStrength(2);
    }

    public void interact(@NotNull Player player) {
        this.eatItemFromHand(player);
        if (!this._foxie.ownerControl.isTame())
            this._foxie.aiControl.trust(player.getUUID());
    }

    private void tryEat() {
        if (!this.isHungry()) return;
        if (!this._foxie.aiControl.canEat()) return;
        if (!this._foxie.mouthControl.hasItem()) return;

        var item = this._foxie.mouthControl.getItem();

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
        return this._foxie.dataControl.getHungerStrength() == 1;
    }

    public boolean isHeavilyHungry() {
        return this._foxie.dataControl.getHungerStrength() >= 2;
    }

    public void tick() {
        if (this._foxie.level.isClientSide) return;
        if (!this._foxie.isAlive()) return;
        if (!this._foxie.isEffectiveAi()) return;

        ++_ticksSinceLastEaten;

        this.tryEat();
    }
}
