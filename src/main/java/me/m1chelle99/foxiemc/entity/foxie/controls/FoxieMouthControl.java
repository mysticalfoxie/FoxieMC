package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("CommentedOutCode") // TODO: Remove before merge
public class FoxieMouthControl {
    private final Foxie foxie;

    public FoxieMouthControl(Foxie foxie) {

        this.foxie = foxie;
    }

    // todo: doesn't spit out item except for hunger c;
    private void spitOutItem(ItemStack stack) {
        if (stack.isEmpty() || this.foxie.level.isClientSide) return;

        var pos = new Vec3(
            this.foxie.getX() + this.foxie.getLookAngle().x, 
            this.foxie.getY() + 1.0D, 
            this.foxie.getZ() + this.foxie.getLookAngle().z
        );
        
        var item = new ItemEntity(this.foxie.level, pos.x, pos.y, pos.z, stack);
        item.setThrower(this.foxie.getUUID());
        item.setPickUpDelay(FoxieConstants.PICKUP_DELAY);

        this.foxie.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F);
        this.foxie.level.addFreshEntity(item);
    }

    public void drop() {
        var items = this.foxie.getItemBySlot(EquipmentSlot.MAINHAND);
        if (items.isEmpty()) return;
        this.foxie.spawnAtLocation(items);
        this.foxie.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
    }

    public void dropItemStack(ItemStack stack) {
        var item = new ItemEntity(
            this.foxie.level, 
            this.foxie.getX(), 
            this.foxie.getY(), 
            this.foxie.getZ(), 
            stack
        );
        
        this.foxie.level.addFreshEntity(item);
    }

    public void takeItem(@NotNull ItemStack stack) {
        if (!this.foxie.canHoldItem(stack)) return;

        this.spitOutItem(this.foxie.getItemBySlot(EquipmentSlot.MAINHAND));
        this.foxie.setItemSlot(EquipmentSlot.MAINHAND, stack.split(1));
        this.foxie
            .getHandDropChances()[EquipmentSlot.MAINHAND.getIndex()] = 1.0F;
    }

    public void pickupItem(@NotNull ItemEntity item) {
        var stack = item.getItem();
        if (!this.foxie.canHoldItem(stack)) return;

        int count = stack.getCount();
        if (count > 1) this.dropItemStack(stack.split(count - 1));

        this.spitOutItem(this.foxie.getItemBySlot(EquipmentSlot.MAINHAND));
        this.foxie.onItemPickup(item);
        this.foxie.setItemSlot(EquipmentSlot.MAINHAND, stack.split(1));
        this.foxie
            .getHandDropChances()[EquipmentSlot.MAINHAND.getIndex()] = 1.0F;
        this.foxie.take(item, stack.getCount());
        item.discard();
    }

    public boolean canTakeItem(ItemStack stack) {
        var slot = Mob.getEquipmentSlotForItem(stack);
        if (!this.foxie.getItemBySlot(slot).isEmpty()) return false;

        return slot == EquipmentSlot.MAINHAND;
    }

    public boolean hasItem() {
        return !getItem().isEmpty();
    }

    public ItemStack getItem() {
        return this.foxie.getItemBySlot(EquipmentSlot.MAINHAND);
    }

    private void setItem(ItemStack item) {
        this.foxie.setItemSlot(EquipmentSlot.MAINHAND, item);
    }

    public void eatItem() {
        var item = this.getItem();
        item = item.finishUsingItem(this.foxie.level, this.foxie);
        if (!item.isEmpty()) this.setItem(item);
    }

    public void summonFoodParticles() {
        var stack = this.getItem();
        if (stack.isEmpty()) return;

        for (int i = 0; i < 8; ++i) {
            var random_x = (double) this.foxie.getRandom().nextFloat();
            
            var foxieX = this.foxie.getX();
            var foxieY = this.foxie.getY();
            var foxieZ = this.foxie.getZ();
            var angle = this.foxie.getLookAngle();
            
            var x = (random_x - 0.5D) * 0.1D;
            var y = Math.random() * 0.1D + 0.1D;
            
            var origin = new Vec3(x, y, 0.0D);
            var destination = origin
                .xRot(-this.foxie.getXRot() * ((float) Math.PI / 180F))
                .yRot(-this.foxie.getYRot() * ((float) Math.PI / 180F));
            
            var particle = new ItemParticleOption(ParticleTypes.ITEM, stack);
            
            var instanceX = foxieX + angle.x / 2.0D;
            var instanceZ = foxieZ + angle.z / 2.0D;
            
            this.foxie.level.addParticle(
                particle, 
                instanceX, 
                foxieY, 
                instanceZ,
                destination.x, 
                destination.y + 0.05D, 
                destination.z
            );
        }
    }

    // TODO: Idk what to do with that yet... 
    //  (maybe do something with that difficultly idk) 
    public void setDefaultEquipment() {
        if (this.foxie.getRandom().nextFloat() >= 0.2F) return;

        float random = this.foxie.getRandom().nextFloat();
        
        ItemStack stack;
        if (random < 0.05F) stack = new ItemStack(Items.EMERALD);
        else if (random < 0.2F) stack = new ItemStack(Items.EGG);
        else if (random < 0.4F) stack = this.foxie.getRandom().nextBoolean() 
            ? new ItemStack(Items.RABBIT_FOOT) 
            : new ItemStack(Items.RABBIT_HIDE);
        else if (random < 0.6F) stack = new ItemStack(Items.WHEAT);
        else if (random < 0.8F) stack = new ItemStack(Items.LEATHER);
        else stack = new ItemStack(Items.FEATHER);

        this.foxie.setItemSlot(EquipmentSlot.MAINHAND, stack);
    }
}
