package me.m1chelle99.foxiemc.entities.foxie.controls;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class FoxieAIControl {


    private final Foxie foxie;

    public FoxieAIControl(Foxie foxie) {
        this.foxie = foxie;
    }

    public boolean canEat() {
        return !this.foxie.stateControl.isSleeping();
    }

    public void trust(@Nullable UUID id) {
        if (id != null && !this.foxie.stateControl.isTrusted(id))
            this.foxie.stateControl.setTrusted(id);
    }

    public boolean trusts(UUID player) {
        return this.foxie.stateControl.isTrusted(player);
    }

}
