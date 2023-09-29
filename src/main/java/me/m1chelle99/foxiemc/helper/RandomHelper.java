package me.m1chelle99.foxiemc.helper;

import java.util.Random;

public final class RandomHelper {
    public static float nextFloat(float min, float max) {
        var random = new Random();
        var range = max - min;
        var result = random.nextFloat() * range;
        return result + min;
    }
}
