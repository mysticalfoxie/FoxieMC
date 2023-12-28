package me.m1chelle99.foxiemc.entity.foxie.goals;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.constants.FoxieMovementSpeed;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;

public class FoxieAvoidPlayerGoal extends Goal {

    private final Foxie _foxie;
    private final TargetingConditions _context = TargetingConditions
            .forNonCombat()
            .range(15)
            .selector(this::isThreatening);
    private Player player;

    public FoxieAvoidPlayerGoal(Foxie foxie) {
        this._foxie = foxie;
    }

    @Override
    public boolean canUse() {
        if (this._foxie.ownerControl.isTame())
            return false;

        if (!this._foxie.aiControl.canAvoidPlayer())
            return false;

        this.findScaryPlayer();
        return this.player != null;
    }

    @Override
    public boolean canContinueToUse() {
        return !this._foxie.getNavigation().isDone();
    }

    @Override
    public void start() {
        var player = this.player.position();
        var position = DefaultRandomPos.getPosAway(this._foxie, 10, 4, player);
        if (position == null)
            return;

        var mod = FoxieMovementSpeed.AVOID_PLAYER;
        this._foxie.runTo(position, mod);
    }
    
    public void stop() {
        this._foxie.getNavigation().stop();
    }

    @Override
    public void tick() {
        double mod = FoxieMovementSpeed.AVOID_PLAYER;
        if (this._foxie.distanceToSqr(this.player) < 15) {
            this._foxie.getNavigation().setSpeedModifier(mod * 1.5);
            return;
        }

        if (this._foxie.distanceToSqr(this.player) < 2) {
            this._foxie.getNavigation().setSpeedModifier(mod * 2);
            return;
        }

        this._foxie.getNavigation().setSpeedModifier(mod);
    }

    private void findScaryPlayer() {
        var level = this._foxie.level;
        this.player = level.getNearestPlayer(
                this._context,
                this._foxie,
                this._foxie.getX(),
                this._foxie.getY(),
                this._foxie.getZ()
        );
    }

    private boolean isThreatening(LivingEntity entity) {
        if (!(entity instanceof Player)) return false;

        if (this._foxie.ownerControl.isTame() &&
                this._foxie.isOwnedBy(entity))
            return false;

        if (!entity.isCrouching())
            return true;

        return this._foxie.distanceToSqr(entity) < 1.5F;
    }
}
