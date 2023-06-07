package me.m1chelle99.foxiemc.entities.foxie.controls;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.FoxieConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class FoxieStateControl {
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> INTERESTED = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> POUNCING = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> COMMANDED = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> TRUSTING = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> HUNGER_STRENGTH = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.INT);


    private final Foxie foxie;
    private int _ticksSinceLastEaten = 0;

    public FoxieStateControl(Foxie foxie) {
        this.foxie = foxie;
    }

    public Optional<UUID> getTrusted() {
        return this.foxie.getEntityData().get(TRUSTING);
    }

    public void setTrusted(@NotNull UUID id) {
        this.foxie.getEntityData().set(TRUSTING, Optional.of(id));
    }

    public boolean isSitting() {
        return this.foxie.getEntityData().get(SITTING);
    }

    private void setSitting(boolean value) {
        this.foxie.getEntityData().set(SITTING, value);
    }

    public boolean isInterested() {
        return this.foxie.getEntityData().get(INTERESTED);
    }

    private void setInterested(boolean value) {
        this.foxie.getEntityData().set(INTERESTED, value);
    }

    public boolean isPouncing() {
        return this.foxie.getEntityData().get(POUNCING);
    }

    private void setPouncing(boolean value) {
        this.foxie.getEntityData().set(POUNCING, value);
    }

    public boolean isSleeping() {
        return this.foxie.getEntityData().get(SLEEPING);
    }

    private void setSleeping(boolean value) {
        this.foxie.getEntityData().set(SLEEPING, value);
    }

    public boolean isCommanded() {
        return this.foxie.getEntityData().get(COMMANDED);
    }

    private void setCommanded(boolean value) {
        this.foxie.getEntityData().set(COMMANDED, value);
    }

    private int getCommand() {
        return this.foxie.getEntityData().get(COMMAND);
    }

    private void setCommand(int command) {
        this.foxie.getEntityData().set(COMMAND, command);
    }

    public void clearTrusted() {
        this.foxie.getEntityData().set(TRUSTING, Optional.empty());
    }

    public int getTicksSinceLastEaten() {
        return this._ticksSinceLastEaten;
    }

    public void setTicksSinceLastFood(int ticks) {
        this._ticksSinceLastEaten = ticks;
    }

    public boolean isTrusted(@Nullable UUID id) {
        var value = this.foxie.getEntityData().get(TRUSTING);
        return value.isPresent() && value.get() == id;
    }

    public boolean isSatiated() {
        return this.foxie.getEntityData().get(HUNGER_STRENGTH) == 0;
    }

    public boolean isHungry() {
        return this.isHeavilyHungry() || this.isSlightlyHungry();
    }

    public boolean isSlightlyHungry() {
        return this.foxie.getEntityData().get(HUNGER_STRENGTH) == 1;
    }

    public boolean isHeavilyHungry() {
        return this.foxie.getEntityData().get(HUNGER_STRENGTH) >= 2;
    }

    public boolean canMove() {
        return !this.isSleeping()
                && !this.isCommanded()
                && !this.isPouncing();
    }
    
    private LivingHurtEvent _lastHurtEvent;
        
    private boolean _isInPanic;
    
    public boolean isInPanic() {
        return this._isInPanic;
    }

    public boolean setInPanic() {
        return setInPanic(true);
    }

    public boolean setInPanic(boolean value = true) {
        
    }
    
    public void onHurt(LivingHurtEvent event) {
        // 
    }

    private void calculateHungerStrength() {
        if (_ticksSinceLastEaten < FoxieConstants.TICKS_UNTIL_SLIGHT_HUNGER)
            this.foxie.getEntityData().set(HUNGER_STRENGTH, 0);
        else if (_ticksSinceLastEaten < FoxieConstants.TICKS_UNTIL_HEAVY_HUNGER)
            this.foxie.getEntityData().set(HUNGER_STRENGTH, 1);
        else
            this.foxie.getEntityData().set(HUNGER_STRENGTH, 2);
    }

    public void readSaveData(CompoundTag compound) {
        this.setSitting(compound.getBoolean("Sitting"));
        this.setInterested(compound.getBoolean("Interested"));
        this.setPouncing(compound.getBoolean("Pouncing"));
        this.setSleeping(compound.getBoolean("Sleeping"));
        this.setCommanded(compound.getBoolean("Commanded"));
        if (compound.getBoolean("Trusting"))
            this.setTrusted(compound.getUUID("Trusted"));

        this.setCommand(compound.getInt("COMMAND"));
        this._ticksSinceLastEaten = compound.getInt("TicksSinceLastEaten");
        this.calculateHungerStrength();
    }

    public void writeSaveData(CompoundTag compound) {
        compound.putBoolean("Sitting", this.isSitting());
        compound.putBoolean("Interested", this.isInterested());
        compound.putBoolean("Pouncing", this.isPouncing());
        compound.putBoolean("Sleeping", this.isSleeping());
        compound.putBoolean("Commanded", this.isCommanded());
        this.getTrusted().ifPresent(uuid -> compound.putUUID("Trusted", uuid));
        compound.putInt("TicksSinceLastEaten", this._ticksSinceLastEaten);
        compound.putInt("COMMAND", this.getCommand());
    }

    public void defineStates() {
        this.foxie.getEntityData().define(SITTING, false);
        this.foxie.getEntityData().define(INTERESTED, false);
        this.foxie.getEntityData().define(POUNCING, false);
        this.foxie.getEntityData().define(SLEEPING, false);
        this.foxie.getEntityData().define(COMMANDED, false);
        this.foxie.getEntityData().define(TRUSTING, Optional.empty());
        this.foxie.getEntityData().define(HUNGER_STRENGTH, FoxieConstants.HUNGER_NONE);
        this.foxie.getEntityData().define(COMMAND, FoxieConstants.COMMAND_NONE);
    }

    public void clear() {
        this.setCommand(FoxieConstants.COMMAND_NONE);
        this.setCommanded(false);
        this.setInterested(false);
        this.setSitting(false);
        this.setPouncing(false);
        this.setSleeping(false);
    }
}
