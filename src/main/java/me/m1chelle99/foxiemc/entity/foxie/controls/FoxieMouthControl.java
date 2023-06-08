package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class FoxieMouthControl {
    private final Foxie foxie;

    public FoxieMouthControl(Foxie foxie) {

        this.foxie = foxie;
    }

    // todo: doesnt spit out item except for hunger c;
    private void spitOutItem(ItemStack stack) {
        if (stack.isEmpty() || this.foxie.level.isClientSide) return;

        _spittedItem = new ItemEntity(this.foxie.level, this.foxie.getX() + this.foxie.getLookAngle().x, this.foxie.getY() + 1.0D, this.foxie.getZ() + this.foxie.getLookAngle().z, stack);
        _spittedItem.setPickUpDelay(40);
        _spittedItem.setThrower(this.foxie.getUUID());

        this.foxie.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F);
        this.foxie.level.addFreshEntity(_spittedItem);
    }

    public void drop() {
        var items = this.foxie.getItemBySlot(EquipmentSlot.MAINHAND);
        if (items.isEmpty()) return;
        this.foxie.spawnAtLocation(items);
        this.foxie.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
    }

    public void dropItemStack(ItemStack stack) {
        var item = new ItemEntity(this.foxie.level, this.foxie.getX(), this.foxie.getY(), this.foxie.getZ(), stack);
        this.foxie.level.addFreshEntity(item);
    }

    public @NotNull SoundEvent getEatingSound(@NotNull ItemStack stack) {
        return SoundEvents.FOX_EAT;
    }

    public void pickupItem(@NotNull ItemEntity item) {
        if (item == _spittedItem) return;
        var stack = item.getItem();

        if (this.foxie.canHoldItem(stack)) {
            int i = stack.getCount();
            if (i > 1)
                this.dropItemStack(stack.split(i - 1));

            this.spitOutItem(this.foxie.getItemBySlot(EquipmentSlot.MAINHAND));
            this.foxie.onItemPickup(item);
            this.foxie.setItemSlot(EquipmentSlot.MAINHAND, stack.split(1));
            // TODO: Verify it's 100% sure. Idk if it's 1.0 or 100.
            this.foxie.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0F;
            this.foxie.take(item, stack.getCount());
            item.discard();
        }
    }


    public boolean isPrey(LivingEntity entity) {
        return entity instanceof Chicken
                || entity instanceof Rabbit
                || entity instanceof Sheep;
    }

    public boolean canHoldItem(ItemStack stack) {
        var item = stack.getItem();
        var held = this.foxie.getItemBySlot(EquipmentSlot.MAINHAND);
        return held.isEmpty() || this.getTicksSinceLastFood() > 0 && item.isEdible() && !held.getItem().isEdible();
    }

    public boolean canTakeItem(ItemStack stack) {
        var slot = Mob.getEquipmentSlotForItem(stack);
        if (!this.foxie.getItemBySlot(slot).isEmpty())
            return false;

        return slot == EquipmentSlot.MAINHAND;
    }

    public boolean hasItem() {
        return !getItem().isEmpty();
    }

    public ItemStack getItem() {
        return this.foxie.getItemBySlot(EquipmentSlot.MAINHAND);
    }

    public void eatItem() {

    }

    public void summonFoodParticles() {
        var stack = this.getItem();
        if (stack.isEmpty()) return;

        for (int i = 0; i < 8; ++i) {
            var vec = new Vec3(((double) this.foxie.getRandom().nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            var vec2 = vec.xRot(-this.foxie.getXRot() * ((float) Math.PI / 180F)).yRot(-this.foxie.getYRot() * ((float) Math.PI / 180F));
            var particle = new ItemParticleOption(ParticleTypes.ITEM, stack);
            var x = this.foxie.getX() + this.foxie.getLookAngle().x / 2.0D;
            var z = this.foxie.getZ() + this.foxie.getLookAngle().z / 2.0D;
            this.foxie.level.addParticle(particle, x, this.foxie.getY(), z, vec2.x, vec2.y + 0.05D, vec2.z);
        }
    }

    // TODO: Idk what to do with that yet... (maybe do something with that difficultly idk) 
    public void setDefaultEquipment() {
        if (!(this.foxie.getRandom().nextFloat() < 0.2F)) return;

        float random = this.foxie.getRandom().nextFloat();
        ItemStack stack;
        if (random < 0.05F)
            stack = new ItemStack(Items.EMERALD);
        else if (random < 0.2F)
            stack = new ItemStack(Items.EGG);
        else if (random < 0.4F)
            stack = this.foxie.getRandom().nextBoolean() ? new ItemStack(Items.RABBIT_FOOT) : new ItemStack(Items.RABBIT_HIDE);
        else if (random < 0.6F)
            stack = new ItemStack(Items.WHEAT);
        else if (random < 0.8F)
            stack = new ItemStack(Items.LEATHER);
        else
            stack = new ItemStack(Items.FEATHER);

        this.foxie.setItemSlot(EquipmentSlot.MAINHAND, stack);
    }

    public void tick() {

    }
}