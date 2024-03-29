package me.m1chelle99.foxiemc.entity.foxie.goals.panic;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.helper.EntityHelper;
import me.m1chelle99.foxiemc.helper.Pathfinder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class FoxieAttackedPanicGoal extends FoxieAbstractPanicGoal {
    private LivingEntity attacker;

    public FoxieAttackedPanicGoal(Foxie foxie) {
        super(foxie);
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this._foxie.getLastHurtByMob() != null;
    }

    @Override
    public void start() {
        this.attacker = this._foxie.getLastHurtByMob();
        super.start();
    }

    @Override
    public void stop() {
        this.attacker = null;
        super.stop();
    }

    @Override
    public void setNewTarget() {
        var position = Pathfinder.getRandomPositionAway(
                this._foxie,
                this.attacker,
                6, 7, 5);
        if (position == null)
            return;

        this._target = Vec3.atCenterOf(position);
    }

    @Override
    public void setCooldown() {
        this._cooldown = EntityHelper.getRandomTicksWithin(this._foxie, 10, 25);
    }
}
