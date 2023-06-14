package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import net.minecraft.world.entity.ai.control.LookControl;

public final class FoxieLookControl extends LookControl {
    private final Foxie _foxie;

    public FoxieLookControl(Foxie foxie) {
        super(foxie);
        this._foxie = foxie;
    }

    public void tick() {
        if (!this._foxie.aiControl.canLook()) return;

        super.tick();
    }
}
