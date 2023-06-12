package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import net.minecraft.world.entity.ai.control.LookControl;

public class FoxieLookControl extends LookControl {
    private final Foxie foxie;

    public FoxieLookControl(Foxie foxie) {
        super(foxie);
        this.foxie = foxie;
    }

    public void tick() {
        if (!this.foxie.aiControl.canLook()) return;

        super.tick();
    }
}
