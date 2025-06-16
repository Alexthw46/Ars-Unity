package com.alexthw.ars_hex.malum;

import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.mana.IManaDiscountEquipment;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.IWrappedCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.PlayerCaster;
import com.hollingsworth.arsnouveau.client.gui.SpellTooltip;
import com.hollingsworth.arsnouveau.common.perk.RepairingPerk;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.config.Config;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import com.sammy.malum.common.item.curiosities.weapons.scythe.MagicScytheItem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.systems.item.LodestoneItemProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnchanterScythe extends MagicScytheItem implements ICasterTool, IManaDiscountEquipment {
    public EnchanterScythe(Tier tier, float attackDamageIn, float attackSpeedIn, float magicDamageIn, LodestoneItemProperties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, magicDamageIn, builderIn.component(DataComponentRegistry.SPELL_CASTER.get(), new SpellCaster()));
    }


    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int p_77663_4_, boolean p_77663_5_) {
        super.inventoryTick(stack, world, entity, p_77663_4_, p_77663_5_);
        if (entity instanceof Player player)
            RepairingPerk.attemptRepair(stack, player);
    }

    @Override
    public boolean isScribedSpellValid(AbstractCaster<?> caster, Player player, InteractionHand hand, ItemStack stack, Spell spell) {
        return spell.unsafeList().stream().noneMatch(s -> s instanceof AbstractCastMethod);
    }

    @Override
    public void sendInvalidMessage(Player player) {
        PortUtil.sendMessageNoSpam(player, Component.translatable("ars_nouveau.sword.invalid"));
    }

    @Override
    public void scribeModifiedSpell(AbstractCaster<?> caster, Player player, InteractionHand hand, ItemStack stack, Spell.Mutable spell) {
        ArrayList<AbstractSpellPart> recipe = new ArrayList<>();
        recipe.add(MethodTouch.INSTANCE);
        recipe.addAll(spell.recipe);
        spell.recipe = recipe;
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, LivingEntity target, @NotNull LivingEntity entity) {
        AbstractCaster<?> caster = getSpellCaster(stack);
        IWrappedCaster wrappedCaster = entity instanceof Player player ? new PlayerCaster(player) : new LivingCaster(entity);
        SpellContext context = new SpellContext(entity.level(), caster.modifySpellBeforeCasting((ServerLevel) target.level(), entity, InteractionHand.MAIN_HAND, caster.getSpell()), entity, wrappedCaster, stack);
        SpellResolver resolver = entity instanceof Player ? new SpellResolver(context) : new EntitySpellResolver(context);
        EntityHitResult entityRes = new EntityHitResult(target);
        resolver.onCastOnEntity(stack, entityRes.getEntity(), InteractionHand.MAIN_HAND);
        return super.hurtEnemy(stack, target, entity);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip2, @NotNull TooltipFlag flagIn) {
        if (Screen.hasShiftDown() || !Config.GLYPH_TOOLTIPS.get())
            getInformation(stack, context, tooltip2, flagIn);
        super.appendHoverText(stack, context, tooltip2, flagIn);
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack pStack) {
        AbstractCaster<?> caster = getSpellCaster(pStack);
        if (caster != null && !Screen.hasShiftDown() && Config.GLYPH_TOOLTIPS.get() && !caster.isSpellHidden() && !caster.getSpell().isEmpty())
            return Optional.of(new SpellTooltip(caster));
        return Optional.empty();
    }

    @Override
    public int getManaDiscount(ItemStack i, Spell spell) {
        // check if the recipe contains anima glyphs
        double sum = 0;
        for (AbstractSpellPart part : spell.unsafeList()) {
            if (SpellSchools.NECROMANCY.isPartOfSchool(part))
                sum += 0.2 * part.getCastingCost();
        }
        return Mth.ceil(sum);
    }

}
