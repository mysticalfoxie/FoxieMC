package me.m1chelle99.foxiemc.entity.foxie.goals;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.FoxieConstants;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class FoxieLookAtPlayerGoal extends Goal {
    private final Foxie _foxie;
    private final TargetingConditions _context;
    private int duration;
    private Player player;

    public FoxieLookAtPlayerGoal(Foxie foxie) {
        this._foxie = foxie;
        this._context = TargetingConditions
            .forNonCombat()
            .range(FoxieConstants.STALK_PLAYER_DISTANCE)
            .selector(this::canLookAtPlayer);
    }


    public boolean canUse() {
        if (!this._foxie.aiControl.canLookAtPlayer())
            return false;

        this.findPlayer();
        if (this.player == null)
            return false;

        return this._foxie.getRandom().nextFloat() <= 0.05F;
    }

    public boolean canContinueToUse() {
        if (!this._foxie.aiControl.canLookAtPlayer())
            return false;

        if (this.player == null)
            return false;

        if (!this.player.isAlive())
            return false;

        if (this.player.isSpectator())
            return false;

        if (this.duration <= 0)
            return false;

        var distance = this._foxie.distanceTo(player);
        return distance <= FoxieConstants.STALK_PLAYER_DISTANCE;
    }

    public void start() {
        var random = this._foxie.getRandom();
        this.duration = random.nextInt(40, 100);
    }

    public void stop() {
        this.player = null;
    }

    public void tick() {
        var player_eyes = this.player.getEyePosition();
        this._foxie
            .getLookControl()
            .setLookAt(player_eyes.x, player_eyes.y, player_eyes.z);

        this.duration--;
    }

    private boolean canLookAtPlayer(LivingEntity entity) {
        if (!(entity instanceof Player)) return false;
        if (entity.isSleeping()) return false;
        if (entity.isSpectator()) return false;
        var distance = this._foxie.distanceTo(entity);
        return distance < FoxieConstants.STALK_PLAYER_DISTANCE;
    }

    private void findPlayer() {
        var level = this._foxie.level;
        this.player = level.getNearestPlayer(
            this._context,
            this._foxie,
            this._foxie.getX(),
            this._foxie.getY(),
            this._foxie.getZ()
        );
    }
}
