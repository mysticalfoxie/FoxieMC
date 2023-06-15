package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import me.m1chelle99.foxiemc.entity.foxie.goals.*;
import me.m1chelle99.foxiemc.entity.foxie.goals.fluids.FoxieAvoidCustomFluidsGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.fluids.FoxieAvoidLavaGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.fluids.FoxieAvoidWaterGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.panic.FoxieAttackedPanicGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.panic.FoxieDefaultPanicGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.panic.FoxieFirePanicGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.sleep.TamedSleepGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.sleep.WildSleepGoal;
import me.m1chelle99.foxiemc.helper.EntityHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public final class FoxieAIControl {
    private final Foxie _foxie;

    public FoxieAIControl(Foxie foxie) {
        this._foxie = foxie;
    }

    public boolean isPanic() {
        var activity = this._foxie.dataControl.getActivity();
        return activity == FoxieConstants.ACTIVITY_PANIC;
    }

    public void trust(@Nullable UUID id) {
        if (id == null) return;
        if (this.isTrusted(id)) return;
        this._foxie.dataControl.setTrusted(id);
    }

    public boolean isCommanded() {
        return !this.hasCommand(FoxieConstants.COMMAND_NONE);
    }

    public boolean canEat() {
        if (this._foxie.isInWater()) return false;
        if (this.hasActivity(FoxieConstants.ACTIVITY_SLEEP)) return false;
        if (this.hasActivity(FoxieConstants.ACTIVITY_PANIC)) return false;
        return !this.hasActivity(FoxieConstants.ACTIVITY_SEEK_SHELTER);
    }

    public boolean isTamable() {
        if (this._foxie.isInWater()) return false;
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieConstants.ACTIVITY_SLEEP) return false;
        if (activity == FoxieConstants.ACTIVITY_PANIC) return false;
        if (activity == FoxieConstants.ACTIVITY_HUNT) return false;
        return activity != FoxieConstants.ACTIVITY_SEEK_SHELTER;
    }

    public boolean canSeekShelter() {
        if (this._foxie.isInWater()) return false;
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieConstants.ACTIVITY_OBEY) return false;
        return activity != FoxieConstants.ACTIVITY_PANIC;
    }

    public boolean canMove() {
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieConstants.ACTIVITY_OBEY) return false;
        return activity != FoxieConstants.ACTIVITY_SLEEP;
    }

    // TODO: Trusted state holds forever currently. Change that.
    public boolean isTrusted(@Nullable UUID id) {
        var value = this._foxie.dataControl.getTrusted();
        return value.isPresent() && value.get() == id;
    }

    public void onHurt() {
        // Foxie receives lava damage. The fleeing logic is already handled.
        if (this.hasActivity(FoxieConstants.ACTIVITY_AVOID_FLUID) &&
            this._foxie.isInLava())
            return;

        this.startActivity(FoxieConstants.ACTIVITY_PANIC);
    }

    public boolean isSleeping() {
        return this.hasActivity(FoxieConstants.ACTIVITY_SLEEP);
    }

    public boolean isSitting() {
        if (!this.hasActivity(FoxieConstants.ACTIVITY_OBEY)) return false;
        return this.hasCommand(FoxieConstants.COMMAND_SIT);
    }

    private boolean hasCommand(Byte command) {
        return this._foxie.dataControl.getCommand() == command;
    }

    public SoundEvent getAmbientSound() {
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieConstants.ACTIVITY_SLEEP)
            return SoundEvents.FOX_SLEEP;

        if (this._foxie.level.isDay())
            return SoundEvents.FOX_AMBIENT;

        if (this._foxie.getRandom().nextFloat() >= 0.1F)
            return SoundEvents.FOX_AMBIENT;

        if (!EntityHelper.playersInDistance(this._foxie, 16.0F))
            return SoundEvents.FOX_SCREECH;

        return SoundEvents.FOX_AMBIENT;
    }

    public void startActivity(Byte activity) {
        this._foxie.dataControl.setActivity(activity);
    }

    public Byte getActivity() {
        return this._foxie.dataControl.getActivity();
    }

    public boolean hasActivity(Byte activity) {
        return this.getActivity().equals(activity);
    }

    public void setCommand(Byte command) {
        if (Objects.equals(command, FoxieConstants.COMMAND_NONE)) {
            this.startActivity(FoxieConstants.ACTIVITY_NONE);
            this._foxie.dataControl.setCommand(FoxieConstants.COMMAND_NONE);
            return;
        }

        this.startActivity(FoxieConstants.ACTIVITY_OBEY);
        this._foxie.dataControl.setCommand(command);
    }

    public boolean canBeCommanded() {
        if (this._foxie.isInWater()) return false;
        var activity = this._foxie.dataControl.getActivity();
        return activity != FoxieConstants.ACTIVITY_PANIC;
    }

    public boolean canLook() {
        var activity = this._foxie.dataControl.getActivity();
        return activity != FoxieConstants.ACTIVITY_SLEEP;
    }

    public boolean canSleep() {
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieConstants.ACTIVITY_PANIC) return false;
        if (this._foxie.isInFluid()) return false;
        if (activity == FoxieConstants.ACTIVITY_SEEK_SHELTER) return false;
        if (activity == FoxieConstants.ACTIVITY_AVOID_FLUID) return false;
        if (activity == FoxieConstants.ACTIVITY_AVOID_LAVA) return false;
        if (activity == FoxieConstants.ACTIVITY_AVOID_PLAYER) return false;
        if (this._foxie.hungerControl.isHeavilyHungry()) return false;
        return activity != FoxieConstants.ACTIVITY_HUNT;
    }

    public boolean canAvoidWater() {
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieConstants.ACTIVITY_PANIC) return false;
        if (activity == FoxieConstants.ACTIVITY_AVOID_PLAYER) return false;
        return activity != FoxieConstants.ACTIVITY_HUNT;
    }

    public boolean canAvoidPlayer() {
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieConstants.ACTIVITY_PANIC) return false;
        if (this._foxie.isInFluid()) return false;
        if (activity == FoxieConstants.ACTIVITY_SEEK_SHELTER) return false;
        if (activity == FoxieConstants.ACTIVITY_AVOID_FLUID) return false;
        return activity != FoxieConstants.ACTIVITY_AVOID_LAVA;
    }

    public boolean canLookAtPlayer() {
        if (!this.canLook()) return false;
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieConstants.ACTIVITY_AVOID_LAVA) return false;
        if (activity == FoxieConstants.ACTIVITY_AVOID_FLUID) return false;
        if (activity == FoxieConstants.ACTIVITY_PANIC) return false;
        return activity != FoxieConstants.ACTIVITY_SEEK_SHELTER;
    }

    public static void register(Foxie foxie) {
        foxie.goalSelector.addGoal(0, new FoxieFloatGoal(foxie));
        foxie.goalSelector.addGoal(0, new FoxieClimbSnowGoal(foxie));
        foxie.goalSelector.addGoal(0, new FoxieAvoidLavaGoal(foxie));

        foxie.goalSelector.addGoal(1, new FoxieAvoidLavaGoal(foxie));

        foxie.goalSelector.addGoal(2, new FoxieFirePanicGoal(foxie));
        foxie.goalSelector.addGoal(2, new FoxieAttackedPanicGoal(foxie));
        foxie.goalSelector.addGoal(2, new FoxieDefaultPanicGoal(foxie));

        foxie.goalSelector.addGoal(3, new FoxieAvoidWaterGoal(foxie));
        foxie.goalSelector.addGoal(3, new FoxieAvoidCustomFluidsGoal(foxie));

        foxie.goalSelector.addGoal(4, new FoxieAvoidPlayerGoal(foxie));
        foxie.goalSelector.addGoal(4, new FoxieLookAtPlayerGoal(foxie));

        foxie.goalSelector.addGoal(5, new FoxieSeekShelterGoal(foxie));

        foxie.goalSelector.addGoal(6, new WildSleepGoal(foxie));
        foxie.goalSelector.addGoal(6, new TamedSleepGoal(foxie));

    }
}
