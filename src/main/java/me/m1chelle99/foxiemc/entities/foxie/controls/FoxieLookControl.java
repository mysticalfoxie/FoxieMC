package me.m1chelle99.foxiemc.entities.foxie.controls;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.FoxieStates;
import net.minecraft.world.entity.ai.control.LookControl;

public class FoxieLookControl extends LookControl {
    private final Foxie foxie;

    public FoxieLookControl(Foxie foxie) {
        super(foxie);
        this.foxie = foxie;
    }

    protected boolean resetXRotOnTick() {
        return !foxie.getFlag(FoxieStates.POUNCING)
                && !foxie.getFlag(FoxieStates.CROUCHING)
                && !foxie.getFlag(FoxieStates.INTERESTED)
                && !foxie.getFlag(FoxieStates.FACEPLANTED);
    }

    public void tick() {
        if (!foxie.getFlag(FoxieStates.SLEEPING))
            super.tick();
    }
}
