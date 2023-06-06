package me.m1chelle99.foxiemc.entities.foxie.controls;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
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
}
