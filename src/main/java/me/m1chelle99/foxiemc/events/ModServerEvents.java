package me.m1chelle99.foxiemc.events;

import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModServerEvents {
    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Foxie foxie)
            foxie.aiControl.onHurt();
    }
}
