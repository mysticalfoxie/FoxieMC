package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

import java.util.Optional;
import java.util.UUID;

public class FoxieDataControl {
    private static final EntityDataAccessor<Optional<UUID>> TRUSTING
        = SynchedEntityData.defineId(Foxie.class,
        EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> HUNGER_STRENGTH
        = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> COMMAND
        = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> ACTIVITY
        = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.BYTE);

    private final Foxie foxie;

    public FoxieDataControl(Foxie foxie) {
        this.foxie = foxie;
    }

    public static void defineStates(Foxie foxie) {
        var data = foxie.getEntityData();
        data.define(ACTIVITY, FoxieConstants.ACTIVITY_NONE);
        data.define(TRUSTING, Optional.empty());
        data.define(COMMAND, FoxieConstants.COMMAND_NONE);
        data.define(HUNGER_STRENGTH, FoxieConstants.HUNGER_NONE);
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

    public void setTrusted(UUID value) {
        this.foxie.getEntityData().set(TRUSTING, Optional.of(value));
    }

    public byte getActivity() {
        return this.foxie.getEntityData().get(ACTIVITY);
    }

    public void setActivity(byte value) {
        this.foxie.getEntityData().set(ACTIVITY, value);
    }

    public void readSaveData(CompoundTag compound) {
        this.setActivity(compound.getByte("Activity"));
        this.setCommand(compound.getByte("Command"));
        if (compound.getBoolean("Trusting"))
            this.setTrusted(compound.getUUID("Trusted"));

        var lastEaten = compound.getInt("TicksSinceLastEaten");
        this.foxie.hungerControl.setTicksSinceLastFood(lastEaten);
    }

    public void writeSaveData(CompoundTag compound) {
        compound.putByte("Activity", this.getActivity());
        compound.putByte("Command", this.getCommand());

        this.getTrusted()
            .ifPresent(uuid -> compound.putUUID("Trusted", uuid));

        var lastEaten = this.foxie.hungerControl.getTicksSinceLastEaten();
        compound.putInt("TicksSinceLastEaten", lastEaten);
    }
}
