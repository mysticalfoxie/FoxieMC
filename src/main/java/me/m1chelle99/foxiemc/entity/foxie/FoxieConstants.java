package me.m1chelle99.foxiemc.entity.foxie;

@SuppressWarnings("unused") // TODO: Remove for release
public final class FoxieConstants {
    // Attributes 
    public static final Double MOVEMENT_SPEED = 0.3D;
    public static final Double MAX_HEALTH = 10.0D;
    public static final Double FOLLOW_RANGE = 40.0D;
    public static final Double ATTACK_DAMAGE = 3.0D;

    // AI Modifiers    
    public static final Double MS_PANIC_MULTIPLIER = 1.5D;
    public static final Double EAT_BERRIES_SPEED_MULTIPLIER = 1.15D;
    public static final int BERRIES_SEARCH_RANGE = 20;
    public static final Double SEEK_SHELTER_MOVEMENT_SPEED_MULTIPLIER = 1.3D;
    public static final Double AVOID_PLAYER_MOVEMENT_SPEED_MULTIPLIER = 1.15D;
    public static final Double SEARCH_SLEEP_MOVEMENT_SPEED_MULTIPLIER = .85D;
    public static final Double AVOID_FLUID_MOVEMENT_SPEED_MULTIPLIER = 1.8D;
    public static final boolean FOLLOW_PREY_EVEN_IF_NOT_SEEN = true;
    public static final Double ATTACK_MOVEMENT_SPEED_MULTIPLIER = 1.25D;
    public static final int WAIT_TIME_BEFORE_SLEEP = 140;
    public static final int STROLL_THROUGH_VILLAGE_INTERVAL = 200;
    public static final Double ALERTING_RANGE = 15.0D;
    public static final Float PLAYER_LOOK_DISTANCE = 32.0F;
    public static final int PICKUP_DELAY = 300;

    // Hunger Ticks
    public static final Integer TICKS_UNTIL_SLIGHT_HUNGER = 4_500; // 4.5 h
    public static final Integer TICKS_UNTIL_HEAVY_HUNGER = 6_000; // 6 h

    // Hunger States
    public static final Integer HUNGER_NONE = 0;
    public static final Integer HUNGER_LIGHT = 1;
    public static final Integer HUNGER_HEAVY = 2;

    // Commands
    public static final Byte COMMAND_NONE = 0;
    public static final Byte COMMAND_SIT = 1;

    // Behavior towards players
    public static final int STALK_PLAYER_DISTANCE = 25;
    public static final int AVOID_PLAYER_DISTANCE = 13;

    // Activities
    public static final Byte ACTIVITY_NONE = 0;
    public static final Byte ACTIVITY_PANIC = 1;
    public static final Byte ACTIVITY_AVOID_FLUID = 2;
    public static final Byte ACTIVITY_AVOID_LAVA = 3;
    public static final Byte ACTIVITY_AVOID_PLAYER = 4;
    public static final Byte ACTIVITY_SEEK_SHELTER = 5;
    public static final Byte ACTIVITY_SEARCH_FOR_SLEEP = 6;
    public static final Byte ACTIVITY_SLEEP = 7;
    public static final Byte ACTIVITY_OBEY = 8;
    public static final Byte ACTIVITY_HUNT = 9;
    public static final Byte ACTIVITY_STRAY = 10;
}
