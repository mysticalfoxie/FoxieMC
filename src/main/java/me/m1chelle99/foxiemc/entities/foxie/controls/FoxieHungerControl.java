package me.m1chelle99.foxiemc.entities.foxie.controls;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.item.ItemStack;

public class FoxieHungerControl {
    private final Foxie foxie;

    public FoxieHungerControl(Foxie foxie) {

        this.foxie = foxie;
    }

    public boolean isYummy(ItemStack stack) {
        // Todo: Implement more c: foxie likes a lot of food
        return stack.is(ItemTags.FOX_FOOD);
    }

    public boolean isEdible(ItemStack stack) {
        return stack.isEdible();
    }

    private void eat() {
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

    private void tryEat() {
        if (!this.foxie.stateControl.isHungry()) return;
        if (!this.foxie.aiControl.canEat()) return;
        if (!this.foxie.mouthControl.hasItem()) return;

        var item = this.foxie.mouthControl.getItem();

        if (!this.isEdible(item)) return;

        if (this.isYummy(item)) {
            this.eat();
            return;
        }

        if (!this.foxie.stateControl.isHeavilyHungry()) return;

        this.eat();
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
