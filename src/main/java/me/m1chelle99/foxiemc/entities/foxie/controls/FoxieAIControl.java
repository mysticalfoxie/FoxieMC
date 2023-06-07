package me.m1chelle99.foxiemc.entities.foxie.controls;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FoxieAIControl {


    private final Foxie foxie;
    
    private boolean _isPanic;

    public FoxieAIControl(Foxie foxie) {
        this.foxie = foxie;
    }

    public boolean canEat() {
        return !this.foxie.stateControl.isSleeping();
    }

    public boolean isPanic() { return this._isPanic; }
    
    public boolean 
    
    public void setPanic(boolean value) { this._isPanic = value; }
    
    public void trust(@Nullable UUID id) {
        if (id != null && !this.foxie.stateControl.isTrusted(id))
            this.foxie.stateControl.setTrusted(id);
    }

    public boolean isTrusted(UUID player) {
        return this.foxie.stateControl.isTrusted(player);
    
}
