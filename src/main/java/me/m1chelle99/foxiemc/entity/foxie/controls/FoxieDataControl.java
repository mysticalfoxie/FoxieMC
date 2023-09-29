package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieActivities;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieCommands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

import java.util.Optional;
import java.util.UUID;

public final class FoxieDataControl {
    private static final EntityDataAccessor<Optional<UUID>> TRUSTING
        = SynchedEntityData.defineId(Foxie.class,
        EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> HUNGER_STRENGTH
        = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> COMMAND
        = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> ACTIVITY
        = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.STRING);
    private final Foxie _foxie;

    public FoxieDataControl(Foxie foxie) {
        this._foxie = foxie;
    }

    public FoxieCommands getCommand() {
        var command = this._foxie.getEntityData().get(COMMAND);
        if (command.isEmpty())
            return FoxieCommands.None;

        return FoxieCommands.valueOf(command);
    }

    public void setCommand(FoxieCommands command) {
        this._foxie.getEntityData().set(COMMAND, command.toString());
    }

    public int getHungerStrength() {
        return this._foxie.getEntityData().get(HUNGER_STRENGTH);
    }

    public void setHungerStrength(int value) {
        this._foxie.getEntityData().set(HUNGER_STRENGTH, value);
    }

    public Optional<UUID> getTrusted() {
        return this._foxie.getEntityData().get(TRUSTING);
    }

    public void setTrusted(UUID value) {
        this._foxie.getEntityData().set(TRUSTING, Optional.of(value));
    }

    public FoxieActivities getActivity() {
        var activity = this._foxie.getEntityData().get(ACTIVITY);
        if (activity.isEmpty())
            return FoxieActivities.None;

        return FoxieActivities.valueOf(activity);
    }

    public void setActivity(FoxieActivities value) {
        this._foxie.getEntityData().set(ACTIVITY, value.toString());
    }

    public void readSaveData(CompoundTag compound) {
        var activity = compound.getString("Activity");
        if (!activity.isEmpty())
            this.setActivity(FoxieActivities.valueOf(activity));

        var command = compound.getString("Command");
        if (!command.isEmpty())
            this.setCommand(FoxieCommands.valueOf(command));
        if (compound.getBoolean("Trusting"))
            this.setTrusted(compound.getUUID("Trusted"));

        var lastEaten = compound.getInt("TicksSinceLastEaten");
        this._foxie.hungerControl.setTicksSinceLastFood(lastEaten);
    }

    public void writeSaveData(CompoundTag compound) {
        compound.putString("Activity", this.getActivity().toString());
        compound.putString("Command", this.getCommand().toString());

        this.getTrusted()
            .ifPresent(uuid -> compound.putUUID("Trusted", uuid));

        var lastEaten = this._foxie.hungerControl.getTicksSinceLastEaten();
        compound.putInt("TicksSinceLastEaten", lastEaten);
    }

    public static void defineStates(Foxie foxie) {
        var data = foxie.getEntityData();
        data.define(ACTIVITY, FoxieActivities.None.toString());
        data.define(TRUSTING, Optional.empty());
        data.define(COMMAND, FoxieCommands.None.toString());
        data.define(HUNGER_STRENGTH, FoxieConstants.HUNGER_NONE);
    }
}
