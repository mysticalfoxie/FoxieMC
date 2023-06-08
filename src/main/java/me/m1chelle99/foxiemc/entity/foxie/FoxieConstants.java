package me.m1chelle99.foxiemc.entity.foxie;

public final class FoxieConstants {
    // Attributes 
    public static final Double MOVEMENT_SPEED = 0.3D;
    public static final Double MAX_HEALTH = 10.0D;
    public static final Double FOLLOW_RANGE = 40.0D;
    public static final Double ATTACK_DAMAGE = 3.0D;

    // AI Modifiers    
    public static final Double MS_PANIC_MULTIPLIER = 2.5D;
    public static final Double EAT_BERRIES_SPEED_MULTIPLIER = 1.2D;
    public static final int BERRIES_SEARCH_RANGE = 20;
    public static final Double SEEK_SHELTER_MOVEMENT_SPEED_MULTIPLIER = 1.25D;
    public static final boolean FOLLOW_PREY_EVEN_IF_NOT_SEEN = true; // foxie has a good nose <3 
    public static final Double ATTACK_MOVEMENT_SPEED_MULTIPLIER = 1.5D;
    public static final int WAIT_TIME_BEFORE_SLEEP = 140;
    public static final int STROLL_THROUGH_VILLAGE_INTERVAL = 200;
    public static final Double ALERTING_RANGE = 15.0D;
    public static final Float PLAYER_LOOK_DISTANCE = 32.0F;

    // Hunger Ticks
    public static final Integer TICKS_UNTIL_SLIGHT_HUNGER = 1500;
    public static final Integer TICKS_UNTIL_HEAVY_HUNGER = 2500;

    // Hunger States
    public static final Integer HUNGER_NONE = 0;
    public static final Integer HUNGER_LIGHT = 1;
    public static final Integer HUNGER_HEAVY = 2;

    // Commands
    public static final Byte COMMAND_NONE = 0;
    public static final Byte COMMAND_SIT = 1;

    // Activities
    public static final Byte ACTIVITY_NONE = 0;
    public static final Byte ACTIVITY_PANIC = 1;
    public static final Byte ACTIVITY_SEEK_SHELTER = 2;
    public static final Byte ACTIVITY_SLEEP = 3;
    public static final Byte ACTIVITY_OBEY = 4;
    public static final Byte ACTIVITY_HUNT = 5;
    public static final Byte ACTIVITY_STRAY = 6;
}
