package me.m1chelle99.foxiemc.helper;

import me.m1chelle99.foxiemc.FoxieMCMod;

import java.util.function.Function;

public final class PerformanceAnalysis {
    public static void runMonitored(String label, Runnable function) {
        long reachableBlocksStartTime = System.nanoTime();
        function.run();
        long reachableBlocksEndTime = System.nanoTime();
        long reachableBlocksDuration = reachableBlocksEndTime - reachableBlocksStartTime;
        double durationInMilliseconds = reachableBlocksDuration / 1_000_000.0;
        FoxieMCMod.LOGGER.warn(label + ": " + durationInMilliseconds + " milliseconds");
    }

    public static <T> T execMonitored(String label, Function<Void, T> function) {
        long reachableBlocksStartTime = System.nanoTime();
        var result = function.apply(null);
        long reachableBlocksEndTime = System.nanoTime();
        long reachableBlocksDuration = reachableBlocksEndTime - reachableBlocksStartTime;
        double durationInMilliseconds = reachableBlocksDuration / 1_000_000.0;
        FoxieMCMod.LOGGER.warn(label + ": " + durationInMilliseconds + " milliseconds");
        return result;
    }
}
