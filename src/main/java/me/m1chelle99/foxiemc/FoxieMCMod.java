package me.m1chelle99.foxiemc;

import com.mojang.logging.LogUtils;
import me.m1chelle99.foxiemc.init.EntityInit;
import me.m1chelle99.foxiemc.init.ItemInit;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FoxieMCMod.ID)
public class FoxieMCMod {
    public static final String ID = "foxiemc";
    public static final Logger LOGGER = LogUtils.getLogger();


    public FoxieMCMod() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        EntityInit.ENTITIES.register(bus);
        ItemInit.ITEMS.register(bus);
    }
}
