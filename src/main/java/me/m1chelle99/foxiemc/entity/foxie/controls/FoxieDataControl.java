package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class FoxieDataControl {
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> INTERESTED = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> POUNCING = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> COMMANDED = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> TRUSTING = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> HUNGER_STRENGTH = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.INT);


    private final Foxie foxie;
    public LivingHurtEvent lastHurtEvent;
    private boolean _isInPanic;

    public FoxieDataControl(Foxie foxie) {
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

    public boolean isTrusted(@Nullable UUID id) {
        var value = this.foxie.getEntityData().get(TRUSTING);
        return value.isPresent() && value.get() == id;
    }

    public int getHungerStrength() {
        return this.foxie.getEntityData().get(HUNGER_STRENGTH);
    }

    public boolean canMove() {
        return !this.isSleeping()
                && !this.isCommanded()
                && !this.isPouncing();
    }

    public boolean canSeekShelter() {
        return !this.isCommanded() && !this.isPouncing() && !this.isInPanic();
    }

    public boolean isInPanic() {
        return this._isInPanic;
    }

    public void setInPanic(boolean value) {
        this._isInPanic = value;
    }

    public void setInPanic() {
        setInPanic(true);
    }

    public void onHurt(LivingHurtEvent event) {
        this._lastHurtEvent = event;
        this.setInPanic(true);
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

    public void stopActivity() {

    }

    public void clear() {
        this.setCommand(FoxieConstants.COMMAND_NONE);
        this.setCommanded(false);
        this.setInterested(false);
        this.setSitting(false);
        this.setPouncing(false);
        this.setSleeping(false);
    }

    public SoundEvent getAmbientSound() {
        if (this.getFlag(FoxieAIControl.SLEEPING))
            return SoundEvents.FOX_SLEEP;

        if (this.level.isDay() || !(this.random.nextFloat() < 0.1F))
            return SoundEvents.FOX_AMBIENT;

        var playerList = this.level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(16.0D, 16.0D, 16.0D), EntitySelector.NO_SPECTATORS);
        if (playerList.isEmpty())
            return SoundEvents.FOX_SCREECH;

        return SoundEvents.FOX_AMBIENT;
    }
}
