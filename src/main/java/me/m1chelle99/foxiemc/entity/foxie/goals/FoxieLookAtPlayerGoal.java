package me.m1chelle99.foxiemc.entity.foxie.goals;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class FoxieLookAtPlayerGoal extends Goal {

    public FoxieLookAtPlayerGoal(Foxie foxie) {
        this._foxie = foxie;
        this._context = TargetingConditions
            .forNonCombat()
            .range(FoxieConstants.STALK_PLAYER_DISTANCE);
    }
    
    private final Foxie _foxie;
    private final TargetingConditions _context;

    public boolean canUse() {
        if (!this._foxie.aiControl.canLookAtPlayer()) return false;
    }
}
