package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieActivities;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieCommands;
import me.m1chelle99.foxiemc.entity.foxie.goals.*;
import me.m1chelle99.foxiemc.entity.foxie.goals.fluids.FoxieAvoidCustomFluidsGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.fluids.FoxieAvoidLavaGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.fluids.FoxieAvoidWaterGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.hunt.FoxieAttackPreyGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.hunt.FoxieCollectBerriesGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.hunt.FoxieSearchForFoodGoal;
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

    public static void register(Foxie foxie) {
        foxie.goalSelector.addGoal(0, new FoxieFloatGoal(foxie));
        foxie.goalSelector.addGoal(0, new FoxieClimbSnowGoal(foxie));
        foxie.goalSelector.addGoal(0, new FoxieAvoidLavaGoal(foxie));

        foxie.goalSelector.addGoal(1, new FoxieFirePanicGoal(foxie));
        foxie.goalSelector.addGoal(1, new FoxieAttackedPanicGoal(foxie));
        foxie.goalSelector.addGoal(1, new FoxieDefaultPanicGoal(foxie));

        foxie.goalSelector.addGoal(2, new FoxieAvoidWaterGoal(foxie));
        foxie.goalSelector.addGoal(2, new FoxieAvoidCustomFluidsGoal(foxie));

        foxie.goalSelector.addGoal(3, new FoxieAvoidPlayerGoal(foxie));
        foxie.goalSelector.addGoal(3, new FoxieLookAtPlayerGoal(foxie));

        foxie.goalSelector.addGoal(4, new FoxieSearchForFoodGoal(foxie));
        foxie.goalSelector.addGoal(4, new FoxieAttackPreyGoal(foxie));
        foxie.goalSelector.addGoal(4, new FoxieCollectBerriesGoal(foxie));

        foxie.goalSelector.addGoal(5, new FoxieSeekShelterGoal(foxie));

        foxie.goalSelector.addGoal(6, new WildSleepGoal(foxie));
        foxie.goalSelector.addGoal(6, new TamedSleepGoal(foxie));

        foxie.goalSelector.addGoal(7, new FoxieStrayGoal(foxie));

        // TODO: Custom breeding... I'm not breedable like a casual animal... 
        //  grrr! put that berries away! *grrr*

        // TODO: Foxie sits in "foxiemc:basket" goal
        // TODO: Foxie sits on bed goal
        // TODO: Foxie sits on block goal

        // TODO: Stalk & Pounce as hunting strategy 
        // TODO: Foxie cant sleep at thunder
        // TODO: Following the parents
    }

    public boolean isPanic() {
        var activity = this._foxie.dataControl.getActivity();
        return activity == FoxieActivities.Panic;
    }

    public void trust(@Nullable UUID id) {
        if (id == null) return;
        if (this.isTrusted(id)) return;
        this._foxie.dataControl.setTrusted(id);
    }

    public boolean isCommanded() {
        return !this.hasCommand(FoxieCommands.None);
    }

    public boolean canEat() {
        if (this._foxie.isInWater()) return false;
        if (this.hasActivity(FoxieActivities.Sleep)) return false;
        if (this.hasActivity(FoxieActivities.Panic)) return false;
        return !this.hasActivity(FoxieActivities.SeekRainShelter);
    }

    public boolean canSeekShelter() {
        if (this._foxie.isInWater()) return false;
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieActivities.Obey) return false;
        return activity != FoxieActivities.Panic;
    }

    public boolean canMove() {
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieActivities.Obey) return false;
        return activity != FoxieActivities.Sleep;
    }

    // TODO: Trusted state holds forever currently. Change that.
    public boolean isTrusted(@Nullable UUID id) {
        var value = this._foxie.dataControl.getTrusted();
        return value.isPresent() && value.get() == id;
    }

    public void onHurt() {
        // Foxie receives lava damage. The fleeing logic is already handled.
        if (this.hasActivity(FoxieActivities.AvoidFluid) &&
            this._foxie.isInLava())
            return;

        this.startActivity(FoxieActivities.Panic);
    }

    public boolean isSleeping() {
        return this.hasActivity(FoxieActivities.Sleep);
    }

    public boolean isSitting() {
        if (!this.hasActivity(FoxieActivities.Obey)) return false;
        return this.hasCommand(FoxieCommands.Sit);
    }

    private boolean hasCommand(FoxieCommands command) {
        return this._foxie.dataControl.getCommand() == command;
    }

    public SoundEvent getAmbientSound() {
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieActivities.Sleep)
            return SoundEvents.FOX_SLEEP;

        if (this._foxie.level.isDay())
            return SoundEvents.FOX_AMBIENT;

        if (this._foxie.getRandom().nextFloat() >= 0.1F)
            return SoundEvents.FOX_AMBIENT;

        if (!EntityHelper.playersInDistance(this._foxie, 16.0F))
            return SoundEvents.FOX_SCREECH;

        return SoundEvents.FOX_AMBIENT;
    }

    public void startActivity(FoxieActivities activity) {
        this._foxie.dataControl.setActivity(activity);
    }

    public FoxieActivities getActivity() {
        return this._foxie.dataControl.getActivity();
    }

    public boolean hasActivity(FoxieActivities activity) {
        return this.getActivity() == activity;
    }

    public void setCommand(FoxieCommands command) {
        if (Objects.equals(command, FoxieCommands.None)) {
            this.startActivity(FoxieActivities.None);
            this._foxie.dataControl.setCommand(FoxieCommands.None);
            return;
        }

        this.startActivity(FoxieActivities.Obey);
        this._foxie.dataControl.setCommand(command);
    }

    public boolean canBeCommanded() {
        if (this._foxie.isInWater()) return false;
        var activity = this._foxie.dataControl.getActivity();
        return activity != FoxieActivities.Panic;
    }

    public boolean canLook() {
        var activity = this._foxie.dataControl.getActivity();
        return activity != FoxieActivities.Sleep;
    }

    public boolean canSleep() {
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieActivities.Panic) return false;
        if (this._foxie.isInFluid()) return false;
        if (activity == FoxieActivities.SeekRainShelter) return false;
        if (activity == FoxieActivities.AvoidFluid) return false;
        if (activity == FoxieActivities.AvoidLava) return false;
        if (activity == FoxieActivities.AvoidPlayer) return false;
        if (this._foxie.hungerControl.isHeavilyHungry()) return false;
        return activity != FoxieActivities.Attack;
    }

    public boolean canAvoidWater() {
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieActivities.Panic) return false;
        if (activity == FoxieActivities.AvoidPlayer) return false;
        return activity != FoxieActivities.Attack;
    }

    public boolean canBeTamed() {
        if (this._foxie.isInWater()) return false;
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieActivities.Sleep) return false;
        if (activity == FoxieActivities.Panic) return false;
        if (activity == FoxieActivities.Attack) return false;
        return activity != FoxieActivities.SeekRainShelter;
    }

    public boolean canAvoidPlayer() {
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieActivities.Panic) return false;
        if (this._foxie.isInFluid()) return false;
        if (activity == FoxieActivities.SeekRainShelter) return false;
        if (activity == FoxieActivities.AvoidFluid) return false;
        if (activity == FoxieActivities.Attack) return false;
        return activity != FoxieActivities.AvoidLava;
    }

    public boolean canLookAtPlayer() {
        if (!this.canLook()) return false;
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieActivities.AvoidLava) return false;
        if (activity == FoxieActivities.AvoidFluid) return false;
        if (activity == FoxieActivities.SearchForFood) return false;
        if (activity == FoxieActivities.Panic) return false;
        if (activity == FoxieActivities.Attack) return false;
        return activity != FoxieActivities.SeekRainShelter;
    }

    public boolean canSearchForFood() {
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieActivities.Obey) return false;
        if (activity == FoxieActivities.Sleep) return false;
        if (activity == FoxieActivities.Panic) return false;
        if (activity == FoxieActivities.AvoidLava) return false;
        if (activity == FoxieActivities.AvoidFluid) return false;
        if (activity == FoxieActivities.SeekRainShelter) return false;
        if (activity == FoxieActivities.AvoidPlayer) return false;
        if (activity == FoxieActivities.SeekSleepShelter) return false;
        return activity != FoxieActivities.Attack;
    }

    public boolean canContinueToAttack() {
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieActivities.Obey) return false;
        if (activity == FoxieActivities.Sleep) return false;
        if (activity == FoxieActivities.Panic) return false;
        return activity != FoxieActivities.AvoidLava;
    }

    public boolean canStray() {
        var activity = this._foxie.dataControl.getActivity();
        if (activity == FoxieActivities.Panic) return false;
        if (activity == FoxieActivities.Obey) return false;
        if (activity == FoxieActivities.Sleep) return false;
        if (activity == FoxieActivities.AvoidFluid) return false;
        if (activity == FoxieActivities.AvoidLava) return false;
        if (activity == FoxieActivities.AvoidPlayer) return false;
        if (activity == FoxieActivities.SeekSleepShelter) return false;
        if (activity == FoxieActivities.Attack) return false;
        if (activity == FoxieActivities.SeekRainShelter) return false;
        return activity != FoxieActivities.SearchForFood;
    }
}
