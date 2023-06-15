package me.m1chelle99.foxiemc.entity.foxie;

import me.m1chelle99.foxiemc.entity.foxie.controls.*;
import me.m1chelle99.foxiemc.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Foxie extends TamableAnimal {
    public static final String ID = "foxie";
    public final FoxieAIControl aiControl;
    public final FoxieMouthControl mouthControl;
    public final FoxieDataControl dataControl;
    public final FoxieHungerControl hungerControl;
    public final FoxieOwnerControl ownerControl;

    public Foxie(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);

        this.moveControl = new FoxieMoveControl(this);
        this.lookControl = new FoxieLookControl(this);
        this.dataControl = new FoxieDataControl(this);
        this.aiControl = new FoxieAIControl(this);
        this.mouthControl = new FoxieMouthControl(this);
        this.hungerControl = new FoxieHungerControl(this);
        this.ownerControl = new FoxieOwnerControl(this);

        this.setPathfindingMalus(BlockPathTypes.DANGER_OTHER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);

        this.setCanPickUpLoot(true);
    }

    public void runTo(@NotNull Vec3 position, double speed_modifier) {
        var blockpos = new BlockPos(position.x, position.y, position.z);
        this.runTo(blockpos, speed_modifier);
    }

    public void runTo(@NotNull BlockPos position, double speed_modifier) {
        var navigator = this.getNavigation();
        var path = navigator.createPath(position, 0);
        navigator.moveTo(path, speed_modifier);
    }

    public boolean isInFluid() {
        var position = this.blockPosition();
        var fluid = this.level.getFluidState(position);
        var fluidBelow = level.getFluidState(position.below());
        return !fluid.isEmpty() || !fluidBelow.isEmpty();
    }

    public Vec3 getRandomTargetWithin(int distance) {
        return DefaultRandomPos.getPos(this, distance, 4);
    }

    // Foxie should run almost aside of you when on leash. 
    // You don't allow foxie too much freedom with this.
    public @NotNull Vec3 getLeashOffset() {
        return new Vec3(
            0.0D, 0.55F * this.getEyeHeight(),
            this.getBbWidth() * 0.4F);
    }

    protected void dropAllDeathLoot(@NotNull DamageSource source) {
        super.dropAllDeathLoot(source);
    }

    // Forge: move extra drops to dropEquipment to 
    // allow them to be captured by LivingDropsEvent
    protected void dropEquipment() {
        super.dropEquipment();
        this.mouthControl.drop();
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.FOX_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.FOX_DEATH;
    }

    protected int calculateFallDamage(float distance, float dmg_multiplier) {
        return Mth.ceil((distance - 5.0F) * dmg_multiplier);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return EntityDimensions.scalable(.65F, .65F);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        FoxieDataControl.defineStates(this);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.dataControl.writeSaveData(compound);
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.dataControl.readSaveData(compound);
    }

    public SpawnGroupData finalizeSpawn(
        @NotNull ServerLevelAccessor level,
        @NotNull DifficultyInstance difficulty,
        @NotNull MobSpawnType mobSpawnType,
        @Nullable SpawnGroupData spawnGroupData,
        @Nullable CompoundTag tag) {
        this.populateDefaultEquipmentSlots(difficulty);
        return super.finalizeSpawn(level, difficulty, mobSpawnType, null, tag);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(
        @NotNull ServerLevel world,
        @NotNull AgeableMob foxie
    ) {
        return EntityInit.FOXIE.get().create(world);
    }

    @Override
    protected void registerGoals() {
        FoxieAIControl.register(this);
    }

    public void playAmbientSound() {
        var event = this.getAmbientSound();
        if (event == SoundEvents.FOX_SCREECH)
            this.playSound(event, 2.0F, this.getVoicePitch());
        else super.playAmbientSound();
    }

    public void tick() {
        super.tick();
        this.hungerControl.tick();
    }

    protected SoundEvent getAmbientSound() {
        return this.aiControl.getAmbientSound();
    }

    protected void pickUpItem(@NotNull ItemEntity item) {
        this.mouthControl.pickupItem(item);
    }

    public boolean canHoldItem(@NotNull ItemStack stack) {
        return this.mouthControl.canTakeItem(stack) && super.canHoldItem(stack);
    }

    protected void populateDefaultEquipmentSlots(
        @NotNull DifficultyInstance difficulty) {
        this.mouthControl.setDefaultEquipment();
    }

    public boolean canTakeItem(@NotNull ItemStack stack) {
        return this.mouthControl.canTakeItem(stack) && super.canTakeItem(stack);
    }

    protected void onOffspringSpawnedFromEgg(Player player, @NotNull Mob mob) {
        var foxie = (Foxie) mob;
        foxie.aiControl.trust(player.getUUID());
    }

    public @NotNull InteractionResult mobInteract(
        @NotNull Player player, @NotNull InteractionHand hand) {
        if (this.hungerControl.canInteract(player)) {
            this.hungerControl.interact(player);
            return InteractionResult.CONSUME;
        }

        if (this.ownerControl.canInteract(player))
            return this.ownerControl.interact(player);

        return InteractionResult.PASS;
    }

    protected void usePlayerItem(
        @NotNull Player player,
        @NotNull InteractionHand hand,
        @NotNull ItemStack stack
    ) {
        super.usePlayerItem(player, hand, stack);
    }

    public float[] getHandDropChances() {
        return this.handDropChances;
    }

    public static AttributeSupplier.Builder getFoxieAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(Attributes.MOVEMENT_SPEED, FoxieConstants.MOVEMENT_SPEED)
            .add(Attributes.MAX_HEALTH, FoxieConstants.MAX_HEALTH)
            .add(Attributes.FOLLOW_RANGE, FoxieConstants.FOLLOW_RANGE)
            .add(Attributes.ATTACK_DAMAGE, FoxieConstants.ATTACK_DAMAGE)
            .add(Attributes.ATTACK_KNOCKBACK);
    }
}
