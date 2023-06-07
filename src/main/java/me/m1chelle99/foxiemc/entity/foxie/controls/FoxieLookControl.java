package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import net.minecraft.world.entity.ai.control.LookControl;

public class FoxieLookControl extends LookControl {
    private final Foxie foxie;

    public FoxieLookControl(Foxie foxie) {
        super(foxie);
        this.foxie = foxie;
    }

    protected boolean resetXRotOnTick() {
        return !foxie.getFlag(FoxieAIControl.POUNCING)
                && !foxie.getFlag(FoxieAIControl.CROUCHING)
                && !foxie.getFlag(FoxieAIControl.INTERESTED)
                && !foxie.getFlag(FoxieAIControl.FACEPLANTED);
    }

    public void tick() {
        if (!foxie.getFlag(FoxieAIControl.SLEEPING))
            super.tick();
    }
}
