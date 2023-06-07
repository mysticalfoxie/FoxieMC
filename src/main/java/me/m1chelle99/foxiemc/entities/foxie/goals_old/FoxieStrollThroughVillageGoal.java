package me.m1chelle99.foxiemc.entities.foxie.goals_old;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.controls.FoxieAIControl;
import net.minecraft.world.entity.ai.goal.StrollThroughVillageGoal;

public class FoxieStrollThroughVillageGoal extends StrollThroughVillageGoal {
    private final Foxie foxie;

    public FoxieStrollThroughVillageGoal(Foxie foxie, int p_28755_) {
        super(foxie, p_28755_);
        this.foxie = foxie;
    }

    public void start() {
        foxie.clearStates();
        super.start();
    }

    public boolean canUse() {
        return super.canUse() && this.canFoxieMove();
    }

    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.canFoxieMove();
    }

    private boolean canFoxieMove() {
        return !foxie.getFlag(FoxieAIControl.SLEEPING)
                && !foxie.getFlag(FoxieAIControl.SITTING)
                && !foxie.getFlag(FoxieAIControl.DEFENDING)
                && !foxie.getFlag(FoxieAIControl.COMMAND_DOWN)
                && foxie.getTarget() == null;
    }
}
