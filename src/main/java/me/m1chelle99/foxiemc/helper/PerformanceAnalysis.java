package me.m1chelle99.foxiemc.helper;

import me.m1chelle99.foxiemc.FoxieMCMod;

import java.util.function.Function;

public final class PerformanceAnalysis {
    public static void runMonitored(String label, Runnable function) {
        var start = System.nanoTime();
        function.run();
        var end = System.nanoTime();
        var duration = end - start;
        var milliseconds = duration / 1_000_000.0;
        
        FoxieMCMod.LOGGER.warn(label + ": " + milliseconds + " milliseconds");
    }

    public static <T> T execMonitored(
        String label, 
        Function<Void, T> function
    ) {
        var start = System.nanoTime();
        var result = function.apply(null);
        var end = System.nanoTime();
        var duration = end - start;
        var milliseconds = duration / 1_000_000.0;
        
        FoxieMCMod.LOGGER.warn(label + ": " + milliseconds + " milliseconds");
        
        return result;
    }
}
