package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import me.m1chelle99.foxiemc.entity.foxie.goals.FoxieFloatGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.panic.FoxieAttackedPanicGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.panic.FoxieDefaultPanicGoal;
import me.m1chelle99.foxiemc.entity.foxie.goals.panic.FoxieFirePanicGoal;
import me.m1chelle99.foxiemc.helper.EntityHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class FoxieAIControl {
    private final Foxie foxie;
    private byte _currentActivity;

    public FoxieAIControl(Foxie foxie) {
        this.foxie = foxie;
    }

    public boolean canEat() {
        return !this.foxie.dataControl.isSleeping();
    }

    public boolean isPanic() {
        return this._currentActivity == FoxieConstants.ACTIVITY_PANIC;
    }

    public void trust(@Nullable UUID id) {
        if (id == null) return;
        if (this.isTrusted(id)) return;
        this.foxie.dataControl.setTrusted(id);
    }

    public boolean isCommanded() {
        if (this._currentActivity != FoxieConstants.ACTIVITY_OBEY) return false;
        return this.foxie.dataControl.getCommand() != FoxieConstants.COMMAND_NONE;
    }

    public void register() {
        this.foxie.goalSelector.addGoal(0, new FoxieFloatGoal(this.foxie));

        this.foxie.goalSelector.addGoal(1, new FoxieFirePanicGoal(this.foxie));
        this.foxie.goalSelector.addGoal(1, new FoxieAttackedPanicGoal(this.foxie));
        this.foxie.goalSelector.addGoal(1, new FoxieDefaultPanicGoal(this.foxie));


//            this.foxie.goalSelector.addGoal(3, new FoxieObeyDownCommandGoal(this));
//            this.foxie.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 15.0F, 1.0F, false));

        // TODO: Custom breeding... Im not breedable like a casual animal... grrr! put that berries away! *grrrr*
        // TODO: Foxie sits in "foxiemc:basket" goal
        // TODO: Foxie sits on bed goal
        // TODO: Foxie sits on block goal

//            this.foxie.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, Player.class, 16.0F, 1.6D, 1.4D, this::isScaryHuman));
//            this.foxie.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, Animal.class, 8.0F, 1.6D, 1.4D, this::isThreatening));

//            this.foxie.goalSelector.addGoal(7, new FoxieStalkPreyGoal(this)); // TODO: doesn't stalk
//            this.foxie.goalSelector.addGoal(8, new FoxiePounceGoal(this)); // TODO: doesn't pounce

        // TODO: foxie seeks shelter now, BUT somewhere deep down in caves. :/  
        // TODO: Tree should be enough
//            this.foxie.goalSelector.addGoal(9, new FoxieSeekShelterGoal(this, FoxieConstants.SEEK_SHELTER_MOVEMENT_SPEED_MULTIPLIER));

        // TODO: killing prey doesnt reset food bar
        // To make it perfect: Prey is guaranteed to spawn it's drops, foxie holds it in her mouth and eats it after some time
//            this.foxie.goalSelector.addGoal(10, new FoxieMeleeAttackGoal(this, FoxieConstants.ATTACK_MOVEMENT_SPEED_MULTIPLIER, FoxieConstants.FOLLOW_PREY_EVEN_IF_NOT_SEEN));

        // TODO: Foxie cant sleep at thunder
//            this.foxie.goalSelector.addGoal(11, new FoxieSleepGoal(this)); // maybe only sleep at night  - bigger Cooldown

        // TODO: Foxie follow parent goal

//            this.foxie.goalSelector.addGoal(12, new FoxieStrollThroughVillageGoal(this, FoxieConstants.STROLL_THROUGH_VILLAGE_INTERVAL));
//            this.foxie.goalSelector.addGoal(13, new FoxieEatBerriesGoal(this, FoxieConstants.EAT_BERRIES_SPEED_MULTIPLIER, FoxieConstants.BERRIES_SEARCH_RANGE, 3));
//            this.foxie.goalSelector.addGoal(14, new LeapAtTargetGoal(this, 0.4F));
//            this.foxie.goalSelector.addGoal(15, new WaterAvoidingRandomStrollGoal(this, 1.0D));
//            this.foxie.goalSelector.addGoal(16, new FoxieSearchForItemsGoal(this));
//            this.foxie.goalSelector.addGoal(17, new FoxieLookAtPlayerGoal(this, FoxieConstants.PLAYER_LOOK_DISTANCE));
//            this.foxie.goalSelector.addGoal(18, new FoxiePerchAndSearchGoal(this));
//            this.targetSelector.addGoal(19, new FoxieDefendTrustedTargetGoal(this));
//            this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Animal.class, 10, false, false, this::isPrey));
//            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, false, false, Turtle.BABY_ON_LAND_SELECTOR));
//            this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractFish.class, 20, false, false, e -> e instanceof AbstractSchoolingFish));
    }

    public boolean canSeekShelter() {
        if (this._currentActivity == FoxieConstants.ACTIVITY_OBEY) return false;
        return this._currentActivity != FoxieConstants.ACTIVITY_PANIC;
    }

    public boolean canMove() {
        if (this._currentActivity == FoxieConstants.ACTIVITY_PANIC) return false;
        if (this._currentActivity == FoxieConstants.ACTIVITY_OBEY) return false;
        return this._currentActivity != FoxieConstants.ACTIVITY_SLEEP;
    }

    // TODO: Trusted state holds forever currently. Change that.
    public boolean isTrusted(@Nullable UUID id) {
        var value = this.foxie.dataControl.getTrusted();
        return value.isPresent() && value.get() == id;
    }

    public void onHurt() {
        // this._lastHurtEvent = event; -> LivingHurtEvent
        this.activate(FoxieConstants.ACTIVITY_PANIC);
    }

    public boolean isSleeping() {
        return this.foxie.dataControl.isSleeping();
    }

    public SoundEvent getAmbientSound() {
        if (this._currentActivity == FoxieConstants.ACTIVITY_SLEEP)
            return SoundEvents.FOX_SLEEP;

        if (this.foxie.level.isDay() || !(this.foxie.getRandom().nextFloat() < 0.1F))
            return SoundEvents.FOX_AMBIENT;

        if (!EntityHelper.playersInDistance(this.foxie, 16.0F))
            return SoundEvents.FOX_SCREECH;

        return SoundEvents.FOX_AMBIENT;
    }

    public void tick() {
        // Hopefully I don't need this.
    }

    public void step() {
        // Hopefully not needed.
    }

    public void activate(Byte activity) {
        this._currentActivity = activity;
    }

    public void setCommand(Byte command) {
        if (Objects.equals(command, FoxieConstants.COMMAND_NONE)) {
            this.activate(FoxieConstants.ACTIVITY_NONE);
            return;
        }

        this.activate(FoxieConstants.ACTIVITY_OBEY);
        this.foxie.dataControl.setCommand(command);
    }

    public boolean canBeCommanded() {
        return this._currentActivity != FoxieConstants.ACTIVITY_PANIC;
    }
}
