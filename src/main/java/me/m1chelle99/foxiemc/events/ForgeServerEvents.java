package me.m1chelle99.foxiemc.events;

import me.m1chelle99.foxiemc.FoxieMCMod;
import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.init.EntityInit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
    public static void onLivingSpawn(LivingSpawnEvent event) {
        if (!(event.getEntity() instanceof Fox fox)) 
            return;
        
        var position = fox.blockPosition();
        fox.remove(Entity.RemovalReason.DISCARDED);
        var foxie = EntityInit.FOXIE.get();
        var entity = foxie.create(fox.level);
        if (entity == null)
            return;
        
        entity.setPos(position.getX(), position.getY(), position.getZ());
        fox.level.addFreshEntity(entity);
    }

    @SubscribeEvent
    public static void onBiomeLoading(BiomeLoadingEvent event) {
        var spawners = event.getSpawns().getSpawner(MobCategory.CREATURE);
        spawners.removeIf(x -> x.type == EntityType.FOX);
        
        if (event.getCategory() != Biome.BiomeCategory.TAIGA)
            return;
        
        var foxie = EntityInit.FOXIE.get();
        var foxieSpawnInfo = new MobSpawnSettings.SpawnerData(foxie, 10, 2, 4);
        spawners.add(foxieSpawnInfo);
    }
}
