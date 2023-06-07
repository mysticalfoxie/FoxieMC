package me.m1chelle99.foxiemc.entities.foxie;

import me.m1chelle99.foxiemc.entities.foxie.controls.*;
import me.m1chelle99.foxiemc.entities.foxie.goals_old.*;
import me.m1chelle99.foxiemc.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
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
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Foxie extends TamableAnimal {
    public static final String ID = "foxie";
    public final FoxieAIControl aiControl;
    public final FoxieMouthControl mouthControl;
    public final FoxieStateControl stateControl;

    public final FoxieActivityControl activityControl;
    public final FoxieHungerControl hungerControl;
    private float interestedAngle;
    private ItemEntity _spittedItem;

    public Foxie(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);

        this.moveControl = new FoxieMoveControl(this);
        this.lookControl = new FoxieLookControl(this);
        this.aiControl = new FoxieAIControl(this);
        this.stateControl = new FoxieStateControl(this);
        this.mouthControl = new FoxieMouthControl(this);
        this.hungerControl = new FoxieHungerControl(this);
        this.activityControl = new FoxieActivityControl(this);

        this.setPathfindingMalus(BlockPathTypes.DANGER_OTHER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 0.0F);

        this.setCanPickUpLoot(true);
        this.setTame(false);
    }

    @SubscribeEvent
    public static void onDamageReceived(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Foxie)) return;
        var foxie = (Foxie) event.getEntity();
        foxie.stateControl.onHurt(event);
    }

    public static AttributeSupplier.Builder getFoxieAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MOVEMENT_SPEED, FoxieConstants.MOVEMENT_SPEED)
                .add(Attributes.MAX_HEALTH, FoxieConstants.MAX_HEALTH)
                .add(Attributes.FOLLOW_RANGE, FoxieConstants.FOLLOW_RANGE)
                .add(Attributes.ATTACK_DAMAGE, FoxieConstants.ATTACK_DAMAGE)
                .add(Attributes.ATTACK_KNOCKBACK);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return EntityDimensions.scalable(.65F, .65F);
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

    public void runTo(@NotNull Vec3 position, double multiplier) {
        this.getNavigation().moveTo(position.x, position.y, position.z, multiplier);
    }

    public Vec3 getRandomTargetWithin(int distance) {
        return DefaultRandomPos.getPos(this, distance, 4);
    }

    public int getRandomTicksWithin(float min_seconds, float max_seconds) {
        var min_ticks = Math.round(min_seconds * 20);
        var max_ticks = Math.round(max_seconds * 20);
        return this.getRandom().nextInt(min_ticks, max_ticks);
    }

    protected SoundEvent getAmbientSound() {
        if (this.getFlag(FoxieAIControl.SLEEPING))
            return SoundEvents.FOX_SLEEP;

        if (this.level.isDay() || !(this.random.nextFloat() < 0.1F))
            return SoundEvents.FOX_AMBIENT;

        var playerList = this.level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(16.0D, 16.0D, 16.0D), EntitySelector.NO_SPECTATORS);
        if (playerList.isEmpty())
            return SoundEvents.FOX_SCREECH;

        return SoundEvents.FOX_AMBIENT;
    }

    protected void pickUpItem(@NotNull ItemEntity item) {
        this.mouthControl.pickupItem(item);
    }

    // TODO: Doesnt have something todo with the leash offset perse, buuuuuuut! to write that idea down:
    // Foxie should run almost aside of you when on leash. You don't allow foxie too much freedom with this.
    public @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0D, 0.55F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
    }

    protected void dropEquipment() { // Forge: move extra drops to dropEquipment to allow them to be captured by LivingDropsEvent
        super.dropEquipment();
        this.mouthControl.drop();
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


    protected void defineSynchedData() {
        super.defineSynchedData();
        this.stateControl.defineStates();
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.stateControl.readSaveData(compound);
    }

    public boolean canTakeItem(@NotNull ItemStack stack) {
        return this.mouthControl.canTakeItem(stack) && super.canTakeItem(stack);
    }

    protected void onOffspringSpawnedFromEgg(Player player, @NotNull Mob mob) {
        var foxie = (Foxie) mob;
        foxie.aiControl.trust(player.getUUID());
    }

    public boolean canHoldItem(@NotNull ItemStack stack) {
        return this.mouthControl.canTakeItem(stack) && super.canHoldItem(stack);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.stateControl.writeSaveData(compound);
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

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FoxieFloatGoal(this));
        this.goalSelector.addGoal(0, new ClimbOnTopOfPowderSnowGoal(this, this.level));
        this.goalSelector.addGoal(1, new FoxieFaceplantGoal(this));
        this.goalSelector.addGoal(2, new FoxiePanicGoal(this, FoxieConstants.MS_PANIC_MULTIPLIER));
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
        this.goalSelector.addGoal(9, new FoxieSeekShelterGoal(this, FoxieConstants.SEEK_SHELTER_MOVEMENT_SPEED_MULTIPLIER));

        // TODO: killing prey doesnt reset food bar
        // To make it perfect: Prey is guaranteed to spawn it's drops, foxie holds it in her mouth and eats it after some time
        this.goalSelector.addGoal(10, new FoxieMeleeAttackGoal(this, FoxieConstants.ATTACK_MOVEMENT_SPEED_MULTIPLIER, FoxieConstants.FOLLOW_PREY_EVEN_IF_NOT_SEEN));

        // TODO: Foxie cant sleep at thunder
        this.goalSelector.addGoal(11, new FoxieSleepGoal(this)); // maybe only sleep at night  - bigger Cooldown

        // TODO: Foxie follow parent goal

        this.goalSelector.addGoal(12, new FoxieStrollThroughVillageGoal(this, FoxieConstants.STROLL_THROUGH_VILLAGE_INTERVAL));
        this.goalSelector.addGoal(13, new FoxieEatBerriesGoal(this, FoxieConstants.EAT_BERRIES_SPEED_MULTIPLIER, FoxieConstants.BERRIES_SEARCH_RANGE, 3));
        this.goalSelector.addGoal(14, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(15, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(16, new FoxieSearchForItemsGoal(this));
        this.goalSelector.addGoal(17, new FoxieLookAtPlayerGoal(this, FoxieConstants.PLAYER_LOOK_DISTANCE));
        this.goalSelector.addGoal(18, new FoxiePerchAndSearchGoal(this));
        this.targetSelector.addGoal(19, new FoxieDefendTrustedTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Animal.class, 10, false, false, this::isPrey));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, false, false, Turtle.BABY_ON_LAND_SELECTOR));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractFish.class, 20, false, false, e -> e instanceof AbstractSchoolingFish));
    }

    public void tick() {
        super.tick();
        if (this.isEffectiveAi()) {
            var isInWater = this.isInWater();
            if (isInWater || this.getTarget() != null || this.level.isThundering())
                this.setFlag(FoxieAIControl.SLEEPING, false);

            if (isInWater || this.getFlag(FoxieAIControl.SLEEPING) || this.getFlag(FoxieAIControl.COMMAND_DOWN))
                this.setFlag(FoxieAIControl.SITTING, false);

            if (this.getFlag(FoxieAIControl.FACEPLANTED) && this.level.random.nextFloat() < 0.2F) {
                var pos = this.blockPosition();
                var state = this.level.getBlockState(pos);
                this.level.levelEvent(2001, pos, Block.getId(state));
            }
        }

        if (this.getFlag(FoxieAIControl.INTERESTED))
            this.interestedAngle += (1.0F - this.interestedAngle) * 0.4F;
        else
            this.interestedAngle += (0.0F - this.interestedAngle) * 0.4F;

        // Don't do crouching animation when you're downed.
        if (this.getFlag(FoxieAIControl.COMMAND_DOWN))
            return;

        if (this.getFlag(FoxieAIControl.CROUCHING))
            this.setCrouchAmount(Math.min(3.0F, this.getCrouchAmount() + 0.2F));
        else this.setCrouchAmount(0.0F);
    }


    public void aiStep() {
        if (!this.level.isClientSide)
            this.doServerAIStep();

        if (this.getFlag(FoxieAIControl.SLEEPING) || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }

        super.aiStep();

        if (this.getFlag(FoxieAIControl.DEFENDING) && this.random.nextFloat() < 0.05F)
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
        if (this.getFlag(FoxieAIControl.SLEEPING) || this.getFlag(FoxieAIControl.COMMAND_DOWN)) {
            this.setFlag(FoxieAIControl.SLEEPING, false);
            this.setFlag(FoxieAIControl.COMMAND_DOWN, false);
            // immediately show interest for something when getting up again
            this.setFlag(FoxieAIControl.INTERESTED, true);
            return InteractionResult.SUCCESS;
        }

        // Foxie lay down buddy!
        if (!this.getFlag(FoxieAIControl.COMMAND_DOWN)) {
            this.clearStates();
            this.setFlag(FoxieAIControl.COMMAND_DOWN, true);
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
                !this.getFlag(FoxieAIControl.SLEEPING) &&
                !this.getFlag(FoxieAIControl.COMMAND_DOWN);
    }

    public void doServerAIStep() {
        if (!this.isAlive()) return;
        if (!this.isEffectiveAi()) return;


        var itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);

        if (!itemstack.isEmpty() && ticksSinceEaten > TICKS_UNTIL_HUNGER && !this.canEat(itemstack)) {
            this.spitOutItem(itemstack);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);

            var target = this.getTarget();
            if (target != null && target.isAlive())
                return;

            this.setFlag(FoxieAIControl.CROUCHING, false);
            this.setFlag(FoxieAIControl.INTERESTED, false);
            return;
        }

        if (ticksSinceEaten > TICKS_UNTIL_HUNGER) {
            var stack = itemstack.finishUsingItem(this.level, this);
            if (!stack.isEmpty())
                this.setItemSlot(EquipmentSlot.MAINHAND, stack);

            this.setTicksSinceLastFood(0);
            return;
        }

        if (ticksSinceEaten > TICKS_UNTIL_HUNGER - 100 && this.random.nextFloat() < 0.1F) {
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
