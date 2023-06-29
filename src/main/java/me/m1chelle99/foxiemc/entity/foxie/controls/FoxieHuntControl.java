package me.m1chelle99.foxiemc.entity.foxie.controls;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;

public class FoxieHuntControl {
    public LivingEntity prey;
    public ItemEntity foodItem;
    public BlockPos berries;

    public boolean foundFood() {
        return prey != null
            || foodItem != null
            || berries != null;
    }
}
