package me.m1chelle99.foxiemc.events;

import me.m1chelle99.foxiemc.FoxieMCMod;
import me.m1chelle99.foxiemc.data.EntityTypeTagsProvider;
import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.init.EntityInit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber(
        modid = FoxieMCMod.ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.DEDICATED_SERVER
)
public final class ForgeServerEvents {
    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Foxie foxie)
            foxie.aiControl.onHurt();
    }

    @SubscribeEvent
    public void onDataGenerating(GatherDataEvent event) {
        var generator = event.getGenerator();
        var provider = event.getLookupProvider();
        var helper = event.getExistingFileHelper();
        var entityProvider = new EntityTypeTagsProvider(generator.getPackOutput(), provider, FoxieMCMod.ID, helper);
        generator.addProvider(true, entityProvider);
        FoxieMCMod.LOGGER.info("Start tagging foxies in 2023, what a disaster");
    }

    @SubscribeEvent
    public static void onLivingSpawn(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Fox fox))
            return;

        var position = fox.blockPosition();
        removeEntity(fox);
        var foxie = EntityInit.FOXIE.get();
        var level = fox.level();
        var entity = foxie.create(level);
        if (entity == null)
            return;

        entity.setPos(position.getX(), position.getY(), position.getZ());
        level.addFreshEntity(entity);
    }

    private static void removeEntity(Entity entity) {
        // Setting it immediately to invisible
        entity.setInvisible(true);

        // And remove it after all the minecraft business logic
        var task = new TimerTask() {
            @Override public void run() {
                entity.setRemoved(Entity.RemovalReason.DISCARDED);
            }
        };

        new Timer().schedule(task, 0);

        // Quickfix for a bug where a fox is being rendered on clientside, but doesn't exist on the server.
        // This way the client gets notified about an invisible fox and gets deleted right in the next tick.
        // Feel free to reach out to me if you read this and know a better way <3 ty :3
    }
}
