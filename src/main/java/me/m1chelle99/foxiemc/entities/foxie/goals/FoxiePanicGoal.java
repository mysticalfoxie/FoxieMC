package me.m1chelle99.foxiemc.entities.foxie.goals;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.FoxieStates;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class FoxiePanicGoal extends PanicGoal {
    private final Foxie foxie;

    // TODO: Make foxie smarter! Currently foxie is just... huh? what happened. ouch. ;c damn... 
    public FoxiePanicGoal(Foxie foxie, double speed_modifier) {
        super(foxie, speed_modifier);
        this.foxie = foxie;
    }

    public boolean shouldPanic() {
        return !foxie.getFlag(FoxieStates.DEFENDING) && super.shouldPanic();
    }

    public void start() {
        foxie.clearStates();
    }
}
