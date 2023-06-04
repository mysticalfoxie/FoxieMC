package me.m1chelle99.foxiemc.entities.foxie.controls;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import net.minecraft.world.entity.ai.control.MoveControl;

public class FoxieMoveControl extends MoveControl {
    private final Foxie foxie;

    public FoxieMoveControl(Foxie foxie) {
        super(foxie);
        this.foxie = foxie;
    }

    public void tick() {
        if (foxie.canMove())
            super.tick();
    }
}
