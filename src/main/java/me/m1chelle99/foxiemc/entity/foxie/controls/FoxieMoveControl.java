package me.m1chelle99.foxiemc.entity.foxie.controls;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import net.minecraft.world.entity.ai.control.MoveControl;

public final class FoxieMoveControl extends MoveControl {
    private final Foxie _foxie;

    public FoxieMoveControl(Foxie foxie) {
        super(foxie);
        this._foxie = foxie;
    }

    public void tick() {
        if (!_foxie.aiControl.canMove()) return;

        super.tick();
    }
}
