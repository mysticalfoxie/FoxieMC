package me.m1chelle99.foxiemc.entity.foxie.goals;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
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

    public FoxieAvoidPlayerGoal(Foxie foxie) {
        this._foxie = foxie;
    }

    private Player player;

    @Override
    public boolean canUse() {
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

        var mod = FoxieConstants.AVOID_PLAYER_MOVEMENT_SPEED_MULTIPLIER;
        this._foxie.runTo(position, mod);
    }

    @Override
    public void tick() {
        if (this._foxie.distanceToSqr(this.player) < 15) {
            var mod = FoxieConstants.AVOID_PLAYER_MOVEMENT_SPEED_MULTIPLIER;
            this._foxie.getNavigation().setSpeedModifier(mod);
            return;
        }

        if (this._foxie.distanceToSqr(this.player) < 2) {
            var mod = FoxieConstants.AVOID_PLAYER_MOVEMENT_SPEED_MULTIPLIER;
            this._foxie.getNavigation().setSpeedModifier(mod * 2);
            return;
        }

        this._foxie.getNavigation().setSpeedModifier(1.0F);
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
