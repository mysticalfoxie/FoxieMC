package me.m1chelle99.foxiemc.entities.foxie.goals;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.FoxieStates;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;

public class FoxieLookAtPlayerGoal extends LookAtPlayerGoal {
    private final Foxie foxie;

    public FoxieLookAtPlayerGoal(Foxie foxie, float max_distance) {
        super(foxie, Player.class, max_distance);
        this.foxie = foxie;
    }

    public boolean canUse() {
        return super.canUse() && !foxie.getFlag(FoxieStates.FACEPLANTED) && !foxie.getFlag(FoxieStates.INTERESTED);
    }

    public boolean canContinueToUse() {
        return super.canContinueToUse() && !foxie.getFlag(FoxieStates.FACEPLANTED) && !foxie.getFlag(FoxieStates.INTERESTED);
    }
}
