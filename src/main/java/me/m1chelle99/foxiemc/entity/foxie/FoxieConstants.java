package me.m1chelle99.foxiemc.entity.foxie;

@SuppressWarnings("unused") // TODO: Remove for release
public final class FoxieConstants {
    // Attributes 
    public static final Double MAX_HEALTH = 10.0D;
    public static final Double FOLLOW_RANGE = 40.0D;
    public static final Double ATTACK_DAMAGE = 3.0D;

    // AI Modifiers    
    public static final int BERRIES_SEARCH_RANGE = 20;
    public static final boolean FOLLOW_PREY_EVEN_IF_NOT_SEEN = true;
    public static final int WAIT_TIME_BEFORE_SLEEP = 140;
    public static final int STROLL_THROUGH_VILLAGE_INTERVAL = 200;
    public static final Double ALERTING_RANGE = 15.0D;
    public static final Float PLAYER_LOOK_DISTANCE = 32.0F;
    public static final int PICKUP_DELAY = 1000;

    // Hunger Ticks
    public static final Integer TICKS_UNTIL_SLIGHT_HUNGER = 4_500; // 4.5 h
    public static final Integer TICKS_UNTIL_HEAVY_HUNGER = 6_000; // 6 h

    // Hunger States
    public static final Integer HUNGER_NONE = 0;
    public static final Integer HUNGER_LIGHT = 1;
    public static final Integer HUNGER_HEAVY = 2;

    // Behavior towards players
    public static final int STALK_PLAYER_DISTANCE = 25;
    public static final int AVOID_PLAYER_DISTANCE = 13;
    public static final int MAX_DIST_TO_OWNER = 8; 
    public static final int PREFERRED_DIST_TO_OWNER = 3;
}
