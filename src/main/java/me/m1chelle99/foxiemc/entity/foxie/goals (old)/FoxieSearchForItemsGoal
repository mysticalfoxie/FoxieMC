//package me.m1chelle99.foxiemc.entity.foxie.goals_old;
//
//import me.m1chelle99.foxiemc.entities.foxie.Foxie;
//import me.m1chelle99.foxiemc.entities.foxie.controls.FoxieAIControl;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.entity.ai.goal.Goal;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.item.ItemStack;
//
//import java.util.EnumSet;
//import java.util.List;
//
//public class FoxieSearchForItemsGoal extends Goal {
//    private final Foxie foxie;
//
//    public FoxieSearchForItemsGoal(Foxie foxie) {
//        this.foxie = foxie;
//        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
//    }
//
//    public boolean canUse() {
//        if (foxie.getFlag(FoxieAIControl.COMMAND_DOWN))
//            return false;
//
//        if (!foxie.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty())
//            return false;
//
//        if (foxie.getTarget() != null || foxie.getLastHurtByMob() != null)
//            return false;
//
//        if (!foxie.canMove())
//            return false;
//
//        if (foxie.getRandom().nextInt(reducedTickDelay(10)) != 0)
//            return false;
//
//        var list = foxie.level.getEntitiesOfClass(ItemEntity.class, foxie.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), foxie::canPickupItem);
//        return !list.isEmpty() && foxie.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
//    }
//
//    public void tick() {
//        List<ItemEntity> list = foxie.level.getEntitiesOfClass(ItemEntity.class, foxie.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), foxie::canPickupItem);
//        ItemStack itemstack = foxie.getItemBySlot(EquipmentSlot.MAINHAND);
//        if (itemstack.isEmpty() && !list.isEmpty())
//            foxie.getNavigation().moveTo(list.get(0), 1.2F);
//    }
//
//    public void start() {
//        List<ItemEntity> list = foxie.level.getEntitiesOfClass(ItemEntity.class, foxie.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), foxie::canPickupItem);
//        if (!list.isEmpty())
//            foxie.getNavigation().moveTo(list.get(0), 1.2F);
//    }
//}
