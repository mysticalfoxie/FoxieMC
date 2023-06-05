package me.m1chelle99.foxiemc.entities.foxie.goals;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.FoxieStates;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;

public class FoxieSeekShelterGoal extends FleeSunGoal {
    private final Foxie foxie;
    private int interval = reducedTickDelay(100);

    public FoxieSeekShelterGoal(Foxie foxie, double speed_modifier) {
        super(foxie, speed_modifier);
        this.foxie = foxie;
    }

    public boolean canUse() {
        if (foxie.getFlag(FoxieStates.COMMAND_DOWN))
            return false;
        
        if (foxie.getFlag(FoxieStates.SLEEPING) || this.mob.getTarget() != null)
            return false;

        if (foxie.level.isRaining() || foxie.level.isThundering())
            return true;

        else if (this.interval > 0) {
            --this.interval;
            return false;
        }

        this.interval = 100;
        var pos = this.mob.blockPosition();
        return foxie.level.isDay()
                && foxie.level.canSeeSky(pos)
                && !((ServerLevel) foxie.level).isVillage(pos)
                && this.setWantedPos();
    }

    public void start() {
        foxie.clearStates();
        super.start();
    }
}
