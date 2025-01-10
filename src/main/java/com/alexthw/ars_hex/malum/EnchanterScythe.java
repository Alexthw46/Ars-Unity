package com.alexthw.ars_hex.malum;

import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.mana.IManaDiscountEquipment;
import com.hollingsworth.arsnouveau.api.spell.SpellCaster;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import com.sammy.malum.common.item.curiosities.weapons.scythe.MagicScytheItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class EnchanterScythe extends MagicScytheItem implements ICasterTool, IManaDiscountEquipment {
    public EnchanterScythe(Tier tier, float attackDamageIn, float attackSpeedIn, float magicDamageIn, Item.Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, magicDamageIn, new Item.Properties().stacksTo(1).component(DataComponentRegistry.SPELL_CASTER, new SpellCaster()));
    }


//    @Override
//    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int p_77663_4_, boolean p_77663_5_) {
//        super.inventoryTick(stack, world, entity, p_77663_4_, p_77663_5_);
//        if (entity instanceof Player player)
//            RepairingPerk.attemptRepair(stack, player);
//    }
//
//    @Override
//    public boolean isScribedSpellValid(ISpellCaster caster, Player player, InteractionHand hand, ItemStack stack, Spell spell) {
//        return spell.recipe.stream().noneMatch(s -> s instanceof AbstractCastMethod);
//    }

    @Override
    public void sendInvalidMessage(Player player) {
        PortUtil.sendMessageNoSpam(player, Component.translatable("ars_nouveau.sword.invalid"));
    }
//
//    @Override
//    public boolean setSpell(ISpellCaster caster, Player player, InteractionHand hand, ItemStack stack, Spell spell) {
//        ArrayList<AbstractSpellPart> recipe = new ArrayList<>();
//        recipe.add(MethodTouch.INSTANCE);
//        recipe.addAll(spell.recipe);
//        recipe.add(AugmentAmplify.INSTANCE);
//        spell.recipe = recipe;
//        return ICasterTool.super.setSpell(caster, player, hand, stack, spell);
//    }
//
//    @Override
//    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity entity) {
//        ISpellCaster caster = getSpellCaster(stack);
//        IWrappedCaster wrappedCaster = entity instanceof Player player ? new PlayerCaster(player) : new LivingCaster(entity);
//        SpellContext context = new SpellContext(entity.level(), caster.modifySpellBeforeCasting(target.level(), entity, InteractionHand.MAIN_HAND, caster.getSpell()), entity, wrappedCaster);
//        SpellResolver resolver = entity instanceof Player ? new SpellResolver(context) : new EntitySpellResolver(context);
//        EntityHitResult entityRes = new EntityHitResult(target);
//        resolver.onCastOnEntity(stack, entityRes.getEntity(), InteractionHand.MAIN_HAND);
//        return super.hurtEnemy(stack, target, entity);
//    }
//
//    @Override
//    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip2, @NotNull TooltipFlag flagIn) {
//        getInformation(stack, worldIn, tooltip2, flagIn);
//        super.appendHoverText(stack, worldIn, tooltip2, flagIn);
//    }

    @Override
    public int getManaDiscount(ItemStack i) {
        return AugmentAmplify.INSTANCE.getCastingCost();
    }
}
