package me.m1chelle99.foxiemc.entities.foxie.goals.panic;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.FoxieConstants;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class FoxieAttackedPanicGoal extends FoxieAbstractPanicGoal {
    private LivingEntity attacker;

    public FoxieAttackedPanicGoal(Foxie foxie) {
        super(foxie);
    }

    @Override
    public boolean canUse() { return super.canUse() && this.foxie.getLastHurtByMobTimestamp() == 0; }
    
    @Override
    public void start() {
        this.attacker = this.foxie.getLastHurtByMob();
        super.start();
    }
    
    @Override
    public void stop() {
        this.attacker = null;
        super.stop();
    }

    @Override
    public void setNewTarget() {
        this.target = DefaultRandomPos.getPosAway(this.foxie, 6, 7, this.attacker.position());
    }

    @Override
    public void setCooldown() {
        this.cooldown = this.foxie.getRandomTicksWithin(6, 15);
    }
}
