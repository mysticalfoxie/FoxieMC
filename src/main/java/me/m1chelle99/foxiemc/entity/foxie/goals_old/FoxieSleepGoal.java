//package me.m1chelle99.foxiemc.entity.foxie.goals_old;
//
//import me.m1chelle99.foxiemc.entities.foxie.Foxie;
//import me.m1chelle99.foxiemc.entities.foxie.controls.FoxieAIControl;
//import net.minecraft.world.entity.ai.goal.Goal;
//
//import java.util.EnumSet;
//
//public class FoxieSleepGoal extends FoxieBehaviorGoal {
//    private static final int WAIT_TIME_BEFORE_SLEEP = reducedTickDelay(Foxie.WAIT_TIME_BEFORE_SLEEP);
//    private int countdown = foxie.getRandom().nextInt(WAIT_TIME_BEFORE_SLEEP);
//
//    public FoxieSleepGoal(Foxie foxie) {
//        super(foxie);
//        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
//    }
//
//    @Override
//    public boolean canUse() {
//        // TODO: Foxie should sleep even when being commanded to stay downed. -> Just get back to this state after sleeping
//        if (foxie.getFlag(FoxieAIControl.COMMAND_DOWN))
//            return false;
//
//        if (foxie.xxa != 0.0F || foxie.yya != 0.0F || foxie.zza != 0.0F)
//            return false;
//
//        return this.canSleep() || foxie.isSleeping();
//    }
//
//    @Override
//    public boolean canContinueToUse() {
//        return this.canSleep();
//    }
//
//    private boolean canSleep() {
//        if (this.countdown <= 0)
//            return foxie.level.isDay() && this.hasShelter() && !this.alertable() && !foxie.isInPowderSnow;
//
//        --this.countdown;
//        return false;
//    }
//
//    @Override
//    public void stop() {
//        this.countdown = foxie.getRandom().nextInt(WAIT_TIME_BEFORE_SLEEP);
//        foxie.clearStates();
//    }
//
//    @Override
//    public void start() {
//        foxie.setFlag(FoxieAIControl.SITTING, false);
//        foxie.setFlag(FoxieAIControl.CROUCHING, false);
//        foxie.setFlag(FoxieAIControl.INTERESTED, false);
//        foxie.setJumping(false);
//        foxie.setFlag(FoxieAIControl.SLEEPING, true);
//        foxie.getNavigation().stop();
//        foxie.getMoveControl().setWantedPosition(foxie.getX(), foxie.getY(), foxie.getZ(), 0.0D);
//    }
//}
