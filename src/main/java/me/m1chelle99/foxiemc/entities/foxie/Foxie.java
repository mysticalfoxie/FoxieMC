package me.m1chelle99.foxiemc.entities.foxie;

import com.google.common.collect.Lists;
import me.m1chelle99.foxiemc.entities.foxie.controls.FoxieLookControl;
import me.m1chelle99.foxiemc.entities.foxie.controls.FoxieMoveControl;
import me.m1chelle99.foxiemc.entities.foxie.goals.*;
import me.m1chelle99.foxiemc.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Foxie extends TamableAnimal {
    public static final String ID = "foxie";
    public static final Double MOVEMENT_SPEED = 0.3D;
    public static final Double PANIC_MOVEMENT_SPEED_MULTIPLIER = 2.5D;
    public static final Double EAT_BERRIES_SPEED_MULTIPLIER = 1.2D;
    public static final int BERRIES_SEARCH_RANGE = 20;
    public static final Double SEEK_SHELTER_MOVEMENT_SPEED_MULTIPLIER = 1.25D;
    public static final boolean FOLLOW_PREY_EVEN_IF_NOT_SEEN = true; // foxie has a good nose <3 
    public static final Double ATTACK_MOVEMENT_SPEED_MULTIPLIER = 1.5D;
    public static final int WAIT_TIME_BEFORE_SLEEP = 140;
    public static final int STROLL_THROUGH_VILLAGE_INTERVAL = 200;
    public static final Double ALERTING_RANGE = 15.0D;
    public static final Float PLAYER_LOOK_DISTANCE = 32.0F;
    public static final Double MAX_HEALTH = 10.0D;
    public static final Double FOLLOW_RANGE = 40.0D;
    public static final Double ATTACK_DAMAGE = 3.0D;

    private static final EntityDataAccessor<Optional<UUID>> DATA_TRUSTED_ID_0 = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Optional<UUID>> DATA_TRUSTED_ID_1 = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> DATA_FLAGS_ID = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_CROUCH_AMOUNT = SynchedEntityData.defineId(Foxie.class, EntityDataSerializers.FLOAT);
    private float interestedAngle;
    private int ticksSinceEaten;

    public Foxie(EntityType<? extends TamableAnimal> type, Level world) {
        super(type, world);

        this.moveControl = new FoxieMoveControl(this);
        this.lookControl = new FoxieLookControl(this);

        this.setPathfindingMalus(BlockPathTypes.DANGER_OTHER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 0.0F);

        this.setCanPickUpLoot(true);
        this.setTame(false);
    }

    public static AttributeSupplier.Builder getFoxieAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED)
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
                .add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(.65F, .65F);
    }

    public float getCrouchAmount() {
        return this.entityData.get(DATA_CROUCH_AMOUNT);
    }

    public void setCrouchAmount(float amount) {
        this.entityData.set(DATA_CROUCH_AMOUNT, amount);
    }

    public boolean canMove() {
        return !this.getFlag(FoxieStates.SLEEPING)
                && !this.getFlag(FoxieStates.COMMAND_DOWN)
                && !this.getFlag(FoxieStates.SITTING)
                && !this.getFlag(FoxieStates.FACEPLANTED);
    }

    public boolean isJumping() {
        return this.jumping;
    }

    protected int calculateFallDamage(float distance, float dmg_multiplier) {
        return Mth.ceil((distance - 5.0F) * dmg_multiplier);
    }

    public void playAmbientSound() {
        var event = this.getAmbientSound();
        if (event == SoundEvents.FOX_SCREECH)
            this.playSound(event, 2.0F, this.getVoicePitch());
        else super.playAmbientSound();
    }

    protected void populateDefaultEquipmentSlots(@NotNull DifficultyInstance difficulty) {
        if (!(this.random.nextFloat() < 0.2F)) return;

        float random = this.random.nextFloat();
        ItemStack stack;
        if (random < 0.05F)
            stack = new ItemStack(Items.EMERALD);
        else if (random < 0.2F)
            stack = new ItemStack(Items.EGG);
        else if (random < 0.4F)
            stack = this.random.nextBoolean() ? new ItemStack(Items.RABBIT_FOOT) : new ItemStack(Items.RABBIT_HIDE);
        else if (random < 0.6F)
            stack = new ItemStack(Items.WHEAT);
        else if (random < 0.8F)
            stack = new ItemStack(Items.LEATHER);
        else
            stack = new ItemStack(Items.FEATHER);

        this.setItemSlot(EquipmentSlot.MAINHAND, stack);
    }

    protected SoundEvent getAmbientSound() {
        if (this.getFlag(FoxieStates.SLEEPING))
            return SoundEvents.FOX_SLEEP;

        if (this.level.isDay() || !(this.random.nextFloat() < 0.1F))
            return SoundEvents.FOX_AMBIENT;

        var playerList = this.level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(16.0D, 16.0D, 16.0D), EntitySelector.NO_SPECTATORS);
        if (playerList.isEmpty())
            return SoundEvents.FOX_SCREECH;

        return SoundEvents.FOX_AMBIENT;
    }

    // TODO: Doesnt have something todo with the leash offset perse, buuuuuuut! to write that idea down:
    // Foxie should run almost aside of you when on leash. You don't allow foxie too much freedom with this.
    public @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0D, 0.55F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
    }

    protected void dropEquipment() { // Forge: move extra drops to dropEquipment to allow them to be captured by LivingDropsEvent
        super.dropEquipment();
        var items = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (items.isEmpty()) return;

        this.spawnAtLocation(items);
        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
    }

    protected void dropAllDeathLoot(@NotNull DamageSource source) {
        super.dropAllDeathLoot(source);
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.FOX_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.FOX_DEATH;
    }

    public boolean getTrustedAttacker(LivingEntity entity) {
        if (entity == null) return false;
        return entity.getLastHurtMob() != null
                && entity.getLastHurtMobTimestamp() < entity.tickCount + 600
                && !this.trusts(entity.getUUID());
    }

    public List<UUID> getTrustedUUIDs() {
        List<UUID> list = Lists.newArrayList();
        list.add(this.entityData.get(DATA_TRUSTED_ID_0).orElse(null));
        list.add(this.entityData.get(DATA_TRUSTED_ID_1).orElse(null));
        return list;
    }

    public boolean trusts(UUID player) {
        return this.getTrustedUUIDs().contains(player);
    }

    public boolean isPathClearTo(LivingEntity prey) {
        double d0 = prey.getZ() - this.getZ();
        double d1 = prey.getX() - this.getX();
        double d2 = d0 / d1;

        for (int j = 0; j < 6; ++j) {
            double d3 = d2 == 0.0D ? 0.0D : d0 * (double) ((float) j / 6.0F);
            double d4 = d2 == 0.0D ? d1 * (double) ((float) j / 6.0F) : d3 / d2;

            for (int k = 1; k < 4; ++k) {
                var block = new BlockPos(this.getX() + d4, this.getY() + (double) k, this.getZ() + d3);
                if (!this.level.getBlockState(block).getMaterial().isReplaceable())
                    return false;
            }
        }

        return true;
    }

    public boolean canPickupItem(ItemEntity item) {
        return !item.hasPickUpDelay() && item.isAlive();
    }

    public boolean isScaryHuman(LivingEntity human) {
        var isHumanAGodOrSpectator = EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(human);
        var isHumanAGhost = EntitySelector.NO_SPECTATORS.test(human);
        var isHumanAGod = !isHumanAGhost && isHumanAGodOrSpectator;
        var isHumanTrusted = this.trusts(human.getUUID());
        if (isHumanAGod) return false; // God's are ok. 
        if (isHumanAGhost) return true; // Ghosts... brr.. scarry
        if (isHumanTrusted) return false; // I like that guy :)
        return !human.isDiscrete(); // this stranger comes really close...
    }

    public boolean isThreatening(LivingEntity entity) {
        return entity instanceof PolarBear
                || entity instanceof Wolf
                || entity instanceof Goat
                || entity instanceof Cow
                || entity instanceof IronGolem;
    }

    public boolean isAlerting(LivingEntity entity) {
        if (this.isPrey(entity)) return true;
        if (entity instanceof Monster) return true;
        if (this.isThreatening(entity)) return true;
        return this.isScaryHuman(entity);
    }

    public boolean isPrey(LivingEntity entity) {
        return entity instanceof Chicken
                || entity instanceof Rabbit
                || entity instanceof Sheep;
    }

    public void clearStates() {
        this.setFlag(FoxieStates.INTERESTED, false);
        this.setFlag(FoxieStates.CROUCHING, false);
        this.setFlag(FoxieStates.SITTING, false);
        this.setFlag(FoxieStates.SLEEPING, false);
        this.setFlag(FoxieStates.DEFENDING, false);
        this.setFlag(FoxieStates.FACEPLANTED, false);
    }

    protected void pickUpItem(ItemEntity item) {
        var stack = item.getItem();
        if (this.canHoldItem(stack)) {
            int i = stack.getCount();
            if (i > 1)
                this.dropItemStack(stack.split(i - 1));

            this.spitOutItem(this.getItemBySlot(EquipmentSlot.MAINHAND));
            this.onItemPickup(item);
            this.setItemSlot(EquipmentSlot.MAINHAND, stack.split(1));
            this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0F;
            this.take(item, stack.getCount());
            item.discard();
            this.ticksSinceEaten = 0;
        }
    }

    // todo: doesnt spit out item c;
    private void spitOutItem(ItemStack stack) {
        if (stack.isEmpty() || this.level.isClientSide) return;

        var entity = new ItemEntity(this.level, this.getX() + this.getLookAngle().x, this.getY() + 1.0D, this.getZ() + this.getLookAngle().z, stack);
        entity.setPickUpDelay(40);
        entity.setThrower(this.getUUID());
        this.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F);
        this.level.addFreshEntity(entity);
    }

    private void dropItemStack(ItemStack stack) {
        var item = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), stack);
        this.level.addFreshEntity(item);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TRUSTED_ID_0, Optional.empty());
        this.entityData.define(DATA_TRUSTED_ID_1, Optional.empty());
        this.entityData.define(DATA_FLAGS_ID, 0);
        this.entityData.define(DATA_CROUCH_AMOUNT, 0.0F);
    }

    public @NotNull SoundEvent getEatingSound(@NotNull ItemStack stack) {
        return SoundEvents.FOX_EAT;
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        var players = compound.getList("Trusted", 11);

        for (net.minecraft.nbt.Tag player : players)
            this.addTrustedUUID(NbtUtils.loadUUID(player));

        this.setFlag(FoxieStates.SLEEPING, compound.getBoolean("Sleeping"));
        this.setFlag(FoxieStates.SITTING, compound.getBoolean("Sitting"));
        this.setFlag(FoxieStates.CROUCHING, compound.getBoolean("Crouching"));
        this.setFlag(FoxieStates.COMMAND_DOWN, compound.getBoolean("CommandDown"));
        if (this.level instanceof ServerLevel)
            this.setPreyGoals();
    }

    public boolean canTakeItem(@NotNull ItemStack stack) {
        EquipmentSlot slot = Mob.getEquipmentSlotForItem(stack);
        if (!this.getItemBySlot(slot).isEmpty())
            return false;

        return slot == EquipmentSlot.MAINHAND && super.canTakeItem(stack);
    }

    protected void onOffspringSpawnedFromEgg(Player player, @NotNull Mob mob) {
        var foxie = (Foxie) mob;
        foxie.addTrustedUUID(player.getUUID());
    }

    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.FOX_FOOD); // Todo: Implement more c: foxie likes a lot of food
    }

    public boolean canHoldItem(ItemStack stack) {
        var item = stack.getItem();
        var itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        return itemstack.isEmpty() || this.ticksSinceEaten > 0 && item.isEdible() && !itemstack.getItem().isEdible();
    }

    public void addTrustedUUID(@Nullable UUID id) {
        if (this.entityData.get(DATA_TRUSTED_ID_0).isPresent())
            this.entityData.set(DATA_TRUSTED_ID_1, Optional.ofNullable(id));
        else this.entityData.set(DATA_TRUSTED_ID_0, Optional.ofNullable(id));
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        var players = this.getTrustedUUIDs();
        var tags = new ListTag();

        for (var player : players)
            if (player != null)
                tags.add(NbtUtils.createUUID(player));

        compound.put("Trusted", tags);
        compound.putBoolean("Sleeping", this.getFlag(FoxieStates.SLEEPING));
        compound.putBoolean("Sitting", this.getFlag(FoxieStates.SITTING));
        compound.putBoolean("Crouching", this.getFlag(FoxieStates.CROUCHING));
        compound.putBoolean("CommandDown", this.getFlag(FoxieStates.COMMAND_DOWN));
    }

    public SpawnGroupData finalizeSpawn(
            @NotNull ServerLevelAccessor level,
            @NotNull DifficultyInstance difficulty,
            @NotNull MobSpawnType mobSpawnType,
            @Nullable SpawnGroupData spawnGroupData,
            @Nullable CompoundTag tag) {

        if (level instanceof ServerLevel)
            this.setPreyGoals();

        this.populateDefaultEquipmentSlots(difficulty);
        return super.finalizeSpawn(level, difficulty, mobSpawnType, null, tag);
    }

    protected void usePlayerItem(
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull ItemStack stack
    ) {
        if (this.isFood(stack))
            this.playSound(this.getEatingSound(stack), 1.0F, 1.0F);

        super.usePlayerItem(player, hand, stack);
    }

    public void handleEntityEvent(byte eventId) {
        // TODO: Research constants... idfk why this exact number
        if (eventId != 45) {
            super.handleEntityEvent(eventId);
            return;
        }

        var stack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (stack.isEmpty()) return;

        for (int i = 0; i < 8; ++i) {
            var vec = new Vec3(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            var vec2 = vec.xRot(-this.getXRot() * ((float) Math.PI / 180F)).yRot(-this.getYRot() * ((float) Math.PI / 180F));
            var particle = new ItemParticleOption(ParticleTypes.ITEM, stack);
            var x = this.getX() + this.getLookAngle().x / 2.0D;
            var z = this.getZ() + this.getLookAngle().z / 2.0D;
            this.level.addParticle(particle, x, this.getY(), z, vec2.x, vec2.y + 0.05D, vec2.z);
        }
    }

    public void setFlag(Integer id, boolean value) {
        if (value)
            this.entityData.set(DATA_FLAGS_ID, this.entityData.get(DATA_FLAGS_ID) | id);
        else
            this.entityData.set(DATA_FLAGS_ID, this.entityData.get(DATA_FLAGS_ID) & ~id);
    }

    public boolean getFlag(Integer id) {
        return (this.entityData.get(DATA_FLAGS_ID) & id) != 0;
    }

    public boolean isFullyCrouched() {
        return this.getCrouchAmount() >= 3.0F;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FoxieFloatGoal(this));
        this.goalSelector.addGoal(0, new ClimbOnTopOfPowderSnowGoal(this, this.level));
        this.goalSelector.addGoal(1, new FoxieFaceplantGoal(this));
        this.goalSelector.addGoal(2, new FoxiePanicGoal(this, PANIC_MOVEMENT_SPEED_MULTIPLIER));
        // TODO: Custom breeding... Im not breedable like an animal... grrr! put that berries away! *grrrr*

        this.goalSelector.addGoal(3, new FoxieObeyDownCommandGoal(this));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 15.0F, 1.0F, false));

        // TODO: Foxie sits in "foxiemc:basket" goal
        // TODO: Foxie sits on bed goal
        // TODO: Foxie sits on block goal

        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, Player.class, 16.0F, 1.6D, 1.4D, this::isScaryHuman));
        this.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, Animal.class, 8.0F, 1.6D, 1.4D, this::isThreatening));

        this.goalSelector.addGoal(7, new FoxieStalkPreyGoal(this)); // TODO: doesnt stalk
        this.goalSelector.addGoal(8, new FoxiePounceGoal(this)); // TODO: doesnt pounce

        // TODO: foxie seeks shelter now, BUT somewhere deep down in caves. :/  
        // TODO: Tree should be enough
        this.goalSelector.addGoal(9, new FoxieSeekShelterGoal(this, SEEK_SHELTER_MOVEMENT_SPEED_MULTIPLIER));

        // TODO: killing prey doesnt reset food bar
        // To make it perfect: Prey is guaranteed to spawn it's drops, foxie holds it in her mouth and eats it after some time
        this.goalSelector.addGoal(10, new FoxieMeleeAttackGoal(this, ATTACK_MOVEMENT_SPEED_MULTIPLIER, FOLLOW_PREY_EVEN_IF_NOT_SEEN));

        // TODO: Foxie cant sleep at thunder
        this.goalSelector.addGoal(11, new FoxieSleepGoal(this)); // maybe only sleep at night  - bigger Cooldown

        // TODO: Foxie follow parent goal

        this.goalSelector.addGoal(12, new FoxieStrollThroughVillageGoal(this, STROLL_THROUGH_VILLAGE_INTERVAL));
        this.goalSelector.addGoal(13, new FoxieEatBerriesGoal(this, EAT_BERRIES_SPEED_MULTIPLIER, BERRIES_SEARCH_RANGE, 3));
        this.goalSelector.addGoal(14, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(15, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(16, new FoxieSearchForItemsGoal(this));
        this.goalSelector.addGoal(17, new FoxieLookAtPlayerGoal(this, PLAYER_LOOK_DISTANCE));
        this.goalSelector.addGoal(18, new FoxiePerchAndSearchGoal(this));
        this.targetSelector.addGoal(19, new FoxieDefendTrustedTargetGoal(this));
    }

    private void setPreyGoals() {

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Animal.class, 10, false, false, this::isPrey));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, false, false, Turtle.BABY_ON_LAND_SELECTOR));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractFish.class, 20, false, false, e -> e instanceof AbstractSchoolingFish));

    }

    public void tick() {
        super.tick();
        if (this.isEffectiveAi()) {
            var isInWater = this.isInWater();
            if (isInWater || this.getTarget() != null || this.level.isThundering())
                this.setFlag(FoxieStates.SLEEPING, false);

            if (isInWater || this.getFlag(FoxieStates.SLEEPING) || this.getFlag(FoxieStates.COMMAND_DOWN))
                this.setFlag(FoxieStates.SITTING, false);

            if (this.getFlag(FoxieStates.FACEPLANTED) && this.level.random.nextFloat() < 0.2F) {
                var pos = this.blockPosition();
                var state = this.level.getBlockState(pos);
                this.level.levelEvent(2001, pos, Block.getId(state));
            }
        }

        if (this.getFlag(FoxieStates.INTERESTED))
            this.interestedAngle += (1.0F - this.interestedAngle) * 0.4F;
        else
            this.interestedAngle += (0.0F - this.interestedAngle) * 0.4F;

        // Don't do crouching animation when you're downed.
        if (this.getFlag(FoxieStates.COMMAND_DOWN))
            return;

        if (this.getFlag(FoxieStates.CROUCHING))
            this.setCrouchAmount(Math.min(3.0F, this.getCrouchAmount() + 0.2F));
        else this.setCrouchAmount(0.0F);
    }


    public void aiStep() {
        if (!this.level.isClientSide)
            this.doServerAIStep();

        if (this.getFlag(FoxieStates.SLEEPING) || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }

        super.aiStep();

        if (this.getFlag(FoxieStates.DEFENDING) && this.random.nextFloat() < 0.05F)
            this.playSound(SoundEvents.FOX_AGGRO, 1.0F, 1.0F);
    }

    private InteractionResult clientMobInteract(Player player, ItemStack stack) {
        if (this.isOwnedBy(player) || this.isTame() || stack.is(Items.BONE) && !this.isTame())
            return InteractionResult.CONSUME;
        else return InteractionResult.PASS;
    }

    private InteractionResult serverMobInteractWithTamed(Player player, ItemStack stack) {
        // Give foxie some food
        // TODO: Refuse to eat when tummy's full, even if health is slightly lower
        if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
            var food_data = Objects.requireNonNull(stack.getFoodProperties(this));
            this.heal((float) food_data.getNutrition());
            if (!player.getAbilities().instabuild)
                stack.shrink(1);

            this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());
            return InteractionResult.SUCCESS;
        }

        // Wake foxie up, get up (this is currently both, down command and sleeping, TODO: Change that! c:)
        if (this.getFlag(FoxieStates.SLEEPING)) {
            this.setFlag(FoxieStates.SLEEPING, false);
            this.setFlag(FoxieStates.COMMAND_DOWN, false);
            // immediately show interest for something when getting up again
            this.setFlag(FoxieStates.INTERESTED, true);
            return InteractionResult.SUCCESS;
        }

        // Foxie lay down buddy!
        if (!this.getFlag(FoxieStates.COMMAND_DOWN)) {
            this.clearStates();
            this.setFlag(FoxieStates.COMMAND_DOWN, true);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public void tryTame(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild)
            stack.shrink(1);

        var success = this.random.nextInt(5) == 0;
        if (!success || net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
            this.level.broadcastEntityEvent(this, (byte) 6);
            return;
        }

        this.tame(player);
        this.navigation.stop();
        this.setTarget(null);
        this.setOrderedToSit(true);
        this.level.broadcastEntityEvent(this, (byte) 7);
    }

    private InteractionResult serverMobInteract(Player player, ItemStack stack, InteractionHand hand) {
        if (this.isTame())
            return this.serverMobInteractWithTamed(player, stack);

        if (stack.is(Items.CHICKEN)) {
            this.tryTame(player, stack);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        var stack = player.getItemInHand(hand);

        if (this.level.isClientSide)
            return this.clientMobInteract(player, stack);
        else
            return this.serverMobInteract(player, stack, hand);
    }

    // TODO: This has to be more precise. For example foxie should prefer always the better food. 
    // It can't be that foxie likes rotten flesh over chicken for example! 
    // I need some sort of Ranking here ;) [That can become a player item as well, some sort of receipt]
    private boolean canEat(ItemStack items) {
        return items
                .getItem()
                .isEdible() &&
                this.onGround &&
                this.getTarget() == null &&
                !this.getFlag(FoxieStates.SLEEPING);
    }

    public void doServerAIStep() {
        if (!this.isAlive()) return;
        if (!this.isEffectiveAi()) return;

        ++this.ticksSinceEaten;

        var itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!this.canEat(itemstack)) {
            var entity = this.getTarget();
            if (entity != null && entity.isAlive())
                return;

            this.setFlag(FoxieStates.CROUCHING, false);
            this.setFlag(FoxieStates.INTERESTED, false);
            return;
        }

        if (this.ticksSinceEaten > 600) {
            var stack = itemstack.finishUsingItem(this.level, this);
            if (!stack.isEmpty())
                this.setItemSlot(EquipmentSlot.MAINHAND, stack);

            this.ticksSinceEaten = 0;
            return;
        }

        if (this.ticksSinceEaten > 560 && this.random.nextFloat() < 0.1F) {
            this.playSound(this.getEatingSound(itemstack), 1.0F, 1.0F);
            this.level.broadcastEntityEvent(this, (byte) 45);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel world, @NotNull AgeableMob foxie) {
        return EntityInit.FOXIE.get().create(world);
    }
}
