package me.m1chelle99.foxiemc.entity.foxie.goals_old;

import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.controls.FoxieAIControl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;

public class FoxieObeyDownCommandGoal extends Goal {
    private final Foxie foxie;
    private int timeUntilSound = 0;

    public FoxieObeyDownCommandGoal(Foxie foxie) {
        this.foxie = foxie; // screech / ambient
    }

    @Override
    public boolean canUse() {
        // TODO: Animation for going down on all paws
        return foxie.getFlag(FoxieAIControl.COMMAND_DOWN);
    }

    @Override
    public void start() {
        // TODO: Crouch amount doesn't work with current model. => Temporary solved with sleep in Model definition 
        foxie.getNavigation().stop();
        foxie.setFlag(FoxieAIControl.SLEEPING, true);

        if (foxie.getRandom().nextBoolean())
            foxie.playSound(SoundEvents.FOX_AMBIENT, .75F, .75F);

        timeUntilSound = foxie.getRandom().nextInt(75, 200);
    }

    @Override
    public void stop() {
        foxie.setFlag(FoxieAIControl.SLEEPING, false);
    }

    @Override
    public void tick() {
        super.tick();

        if (timeUntilSound <= 0) {
            foxie.playSound(SoundEvents.FOX_AMBIENT, 1.0F, 1.0F);
            timeUntilSound = foxie.getRandom().nextInt(75, 200);
            return;
        }

        timeUntilSound--;
    }
}
