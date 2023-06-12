package me.m1chelle99.foxiemc.events;

import me.m1chelle99.foxiemc.FoxieMCMod;
import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
    modid = FoxieMCMod.ID,
    bus = Mod.EventBusSubscriber.Bus.FORGE,
    value = Dist.DEDICATED_SERVER
)
public class ForgeServerEvents {
    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Foxie foxie)
            foxie.aiControl.onHurt();
    }
}
