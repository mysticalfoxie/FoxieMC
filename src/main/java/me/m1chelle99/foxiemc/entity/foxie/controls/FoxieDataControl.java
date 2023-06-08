package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class FoxieDataControl {
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> INTERESTED = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> POUNCING = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> TRUSTING = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> HUNGER_STRENGTH = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> COMMAND = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BYTE);

    private final Foxie foxie;

    public FoxieDataControl(Foxie foxie) {
        this.foxie = foxie;
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

    public byte getCommand() {
        return this.foxie.getEntityData().get(COMMAND);
    }

    public void setCommand(byte command) {
        this.foxie.getEntityData().set(COMMAND, command);
    }

    public int getHungerStrength() {
        return this.foxie.getEntityData().get(HUNGER_STRENGTH);
    }

    public void setHungerStrength(int value) {
        this.foxie.getEntityData().set(HUNGER_STRENGTH, value);
    }

    public Optional<UUID> getTrusted() {
        return this.foxie.getEntityData().get(TRUSTING);
    }

    public void setTrusted(@NotNull UUID id) {
        this.foxie.getEntityData().set(TRUSTING, Optional.of(id));
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public void setTrusted(Optional<UUID> value) {
        this.foxie.getEntityData().set(TRUSTING, value);
    }

    public void readSaveData(CompoundTag compound) {
        this.setSitting(compound.getBoolean("Sitting"));
        this.setInterested(compound.getBoolean("Interested"));
        this.setPouncing(compound.getBoolean("Pouncing"));
        this.setSleeping(compound.getBoolean("Sleeping"));
        this.setCommand(compound.getByte("Command"));
        if (compound.getBoolean("Trusting"))
            this.setTrusted(compound.getUUID("Trusted"));

        this.foxie.hungerControl.setTicksSinceLastFood(compound.getInt("TicksSinceLastEaten"));
    }

    public void writeSaveData(CompoundTag compound) {
        compound.putBoolean("Sitting", this.isSitting());
        compound.putBoolean("Interested", this.isInterested());
        compound.putBoolean("Pouncing", this.isPouncing());
        compound.putBoolean("Sleeping", this.isSleeping());
        compound.putByte("Command", this.getCommand());
        this.getTrusted().ifPresent(uuid -> compound.putUUID("Trusted", uuid));
        compound.putInt("TicksSinceLastEaten", this.foxie.hungerControl.getTicksSinceLastEaten());
    }

    public void defineStates() {
        this.foxie.getEntityData().define(SITTING, false);
        this.foxie.getEntityData().define(INTERESTED, false);
        this.foxie.getEntityData().define(POUNCING, false);
        this.foxie.getEntityData().define(SLEEPING, false);
        this.foxie.getEntityData().define(TRUSTING, Optional.empty());
        this.foxie.getEntityData().define(COMMAND, FoxieConstants.COMMAND_NONE);
        this.foxie.getEntityData().define(HUNGER_STRENGTH, FoxieConstants.HUNGER_NONE);
    }
}
