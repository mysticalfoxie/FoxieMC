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

    public FoxieLookAtPlayerGoal(Foxie foxie) {
        this._foxie = foxie;
        this._context = TargetingConditions
            .forNonCombat()
            .range(FoxieConstants.STALK_PLAYER_DISTANCE)
            .selector(this::canLookAtPlayer);
    }

    private int cooldown;
    private int duration;
    private Player player;

    public boolean canUse() {
        if (!this._foxie.aiControl.canLookAtPlayer())
            return false;

        this.findPlayer();

        return this.player != null;
    }

    public boolean canContinueToUse() {
        if (!this._foxie.aiControl.canLookAtPlayer())
            return false;

        if (this.player == null)
            return false;

        if (!this.player.isAlive()) return false;
        return !this.player.isSpectator();
    }

    public void start() {
        var random = this._foxie.getRandom();
        this.duration = random.nextInt(20, 120);
    }

    public void stop() {
        this.duration = 0;
        this.cooldown = 0;
        this.player = null;
    }

    public void tick() {
        if (this.cooldown > 0 && this.duration <= 0) {
            this.cooldown--;
            return;
        }

        if (this.duration <= 0) {
            this.cooldown = this._foxie.getRandom().nextInt(50, 120);
            this.duration = this._foxie.getRandom().nextInt(20, 100);
            return;
        }

        this.duration--;

        var player_eyes = this.player.getEyePosition();
        this._foxie
            .getLookControl()
            .setLookAt(player_eyes.x, player_eyes.y, player_eyes.z);
    }

    private boolean canLookAtPlayer(LivingEntity entity) {
        if (!(entity instanceof Player)) return false;
        if (entity.isSleeping()) return false;
        if (entity.isSpectator()) return false;
        var distance = this._foxie.distanceTo(entity);
        return !entity.isCrouching() || !(distance > 12.5);
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
