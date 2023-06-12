package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import me.m1chelle99.foxiemc.entity.foxie.goals.AvoidFluidGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.FoxieClimbSnowGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.FoxieFloatGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.FoxieSeekShelterGoal;
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

public class FoxieAIControl {
    private final Foxie foxie;

    public FoxieAIControl(Foxie foxie) {
        this.foxie = foxie;
    }

    public static void register(Foxie foxie) {
        foxie.goalSelector.addGoal(0, new FoxieFloatGoal(foxie));
        foxie.goalSelector.addGoal(0, new FoxieClimbSnowGoal(foxie));
        foxie.goalSelector.addGoal(0, new AvoidFluidGoal(foxie));

        foxie.goalSelector.addGoal(1, new FoxieFirePanicGoal(foxie));
        foxie.goalSelector.addGoal(1, new FoxieAttackedPanicGoal(foxie));
        foxie.goalSelector.addGoal(1, new FoxieDefaultPanicGoal(foxie));

        foxie.goalSelector.addGoal(2, new FoxieSeekShelterGoal(foxie));

        foxie.goalSelector.addGoal(3, new WildSleepGoal(foxie));
        foxie.goalSelector.addGoal(3, new TamedSleepGoal(foxie));

    }

    public boolean isPanic() {
        var activity = this.foxie.dataControl.getActivity();
        return activity == FoxieConstants.ACTIVITY_PANIC;
    }

    public void trust(@Nullable UUID id) {
        if (id == null) return;
        if (this.isTrusted(id)) return;
        this.foxie.dataControl.setTrusted(id);
    }

    public boolean isCommanded() {
        return this.hasCommand(FoxieConstants.COMMAND_NONE);
    }

    public boolean canEat() {
        if (this.foxie.isInWater()) return false;
        if (this.hasActivity(FoxieConstants.ACTIVITY_SLEEP)) return false;
        if (this.hasActivity(FoxieConstants.ACTIVITY_PANIC)) return false;
        return !this.hasActivity(FoxieConstants.ACTIVITY_SEEK_SHELTER);
    }

    public boolean isTamable() {
        if (this.foxie.isInWater()) return false;
        var activity = this.foxie.dataControl.getActivity();
        if (activity == FoxieConstants.ACTIVITY_SLEEP) return false;
        if (activity == FoxieConstants.ACTIVITY_PANIC) return false;
        if (activity == FoxieConstants.ACTIVITY_HUNT) return false;
        return activity != FoxieConstants.ACTIVITY_SEEK_SHELTER;
    }

    public boolean canSeekShelter() {
        if (this.foxie.isInWater()) return false;
        var activity = this.foxie.dataControl.getActivity();
        if (activity == FoxieConstants.ACTIVITY_OBEY) return false;
        return activity != FoxieConstants.ACTIVITY_PANIC;
    }

    public boolean canMove() {
        var activity = this.foxie.dataControl.getActivity();
        if (activity == FoxieConstants.ACTIVITY_OBEY) return false;
        return activity != FoxieConstants.ACTIVITY_SLEEP;
    }

    // TODO: Trusted state holds forever currently. Change that.
    public boolean isTrusted(@Nullable UUID id) {
        var value = this.foxie.dataControl.getTrusted();
        return value.isPresent() && value.get() == id;
    }

    public void onHurt() {
        // Foxie receives lava damage. The fleeing logic is already handled.
        if (this.hasActivity(FoxieConstants.ACTIVITY_AVOID_FLUID) &&
            this.foxie.isInLava())
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
        return this.foxie.dataControl.getCommand() == command;
    }

    public SoundEvent getAmbientSound() {
        var activity = this.foxie.dataControl.getActivity();
        if (activity == FoxieConstants.ACTIVITY_SLEEP)
            return SoundEvents.FOX_SLEEP;

        if (this.foxie.level.isDay())
            return SoundEvents.FOX_AMBIENT;

        if (this.foxie.getRandom().nextFloat() >= 0.1F)
            return SoundEvents.FOX_AMBIENT;

        if (!EntityHelper.playersInDistance(this.foxie, 16.0F))
            return SoundEvents.FOX_SCREECH;

        return SoundEvents.FOX_AMBIENT;
    }

    public void startActivity(Byte activity) {
        this.foxie.dataControl.setActivity(activity);
    }

    public Byte getActivity() {
        return this.foxie.dataControl.getActivity();
    }

    public boolean hasActivity(Byte activity) {
        return this.getActivity().equals(activity);
    }

    public void setCommand(Byte command) {
        if (Objects.equals(command, FoxieConstants.COMMAND_NONE)) {
            this.startActivity(FoxieConstants.ACTIVITY_NONE);
            return;
        }

        this.startActivity(FoxieConstants.ACTIVITY_OBEY);
        this.foxie.dataControl.setCommand(command);
    }

    public boolean canBeCommanded() {
        if (this.foxie.isInWater()) return false;
        var activity = this.foxie.dataControl.getActivity();
        return activity != FoxieConstants.ACTIVITY_PANIC;
    }

    public boolean canLook() {
        var activity = this.foxie.dataControl.getActivity();
        return activity != FoxieConstants.ACTIVITY_SLEEP;
    }

    public boolean canSleep() {
        var activity = this.foxie.dataControl.getActivity();
        if (activity == FoxieConstants.ACTIVITY_PANIC) return false;
        if (this.foxie.isInFluid()) return false;
        if (activity == FoxieConstants.ACTIVITY_SEEK_SHELTER) return false;
        if (this.foxie.hungerControl.isHeavilyHungry()) return false;
        return activity != FoxieConstants.ACTIVITY_HUNT;
    }

    public boolean canAvoidWater() {
        if (this.hasActivity(FoxieConstants.ACTIVITY_PANIC)) return false;
        return !this.hasActivity(FoxieConstants.ACTIVITY_HUNT);
    }
}
