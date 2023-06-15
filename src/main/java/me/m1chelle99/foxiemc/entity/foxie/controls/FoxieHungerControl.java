package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class FoxieHungerControl {
    private final Foxie _foxie;

    public FoxieHungerControl(Foxie foxie) {

        this._foxie = foxie;
    }

    private int _eatDelay = 80;
    private int _ticksSinceLastEaten = 0;

    public boolean isYummy(ItemStack stack) {
        // Todo: Implement more c: foxie likes a lot of food
        return stack.is(Items.CHICKEN)
            || stack.is(Items.COOKED_CHICKEN);
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
        if (this._eatDelay > 0) {
            this._eatDelay--;
            if (this._eatDelay % 10 == 0 &&
                this._foxie.getRandom().nextBoolean())
                this.startFoodEffects();
            return;
        }

        this._eatDelay = this._foxie.getRandom().nextInt(40, 90);

        var item = this._foxie.mouthControl.getItem();
        var food = item.getFoodProperties(this._foxie);
        assert food != null;

        var nutrition = (float) food.getNutrition();

        this._foxie.mouthControl.eatItem();
        this._ticksSinceLastEaten = 0;

        this.startFoodEffects();
        this._foxie.level
            .broadcastEntityEvent(this._foxie, EntityEvent.FOX_EAT);
        this._foxie.heal(nutrition);
    }

    public void startFoodEffects() {
        this._foxie.playSound(SoundEvents.FOX_EAT, .5F, 1F);
        this._foxie.mouthControl.summonFoodParticles();
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

    private void tryEat() {
        if (!this.isHungry()) return;
        if (!this._foxie.aiControl.canEat()) return;
        if (!this._foxie.mouthControl.hasItem()) return;

        var item = this._foxie.mouthControl.getItem();

        if (!this.isEdible(item)) return;

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

        this.calculateHungerStrength();
        this.tryEat();
    }
}
