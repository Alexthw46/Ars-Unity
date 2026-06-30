package com.alexthw.ars_hex.common;

import com.alexthw.ars_hex.registry.ModRegistry;
import com.hollingsworth.arsnouveau.common.items.ModItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MoonDial extends ModItem {

    private static final MoonPhases.MoonCondition[] SELECTABLE_PHASES = {
            MoonPhases.MoonCondition.NEW_MOON,
            MoonPhases.MoonCondition.WAXING_CRESCENT,
            MoonPhases.MoonCondition.FIRST_QUARTER,
            MoonPhases.MoonCondition.WAXING_GIBBOUS,
            MoonPhases.MoonCondition.FULL_MOON,
            MoonPhases.MoonCondition.WANING_GIBBOUS,
            MoonPhases.MoonCondition.LAST_QUARTER,
            MoonPhases.MoonCondition.WANING_CRESCENT
    };

    public MoonDial() {
        super(new Item.Properties()
                .stacksTo(1)
                .component(ModRegistry.MOON_PHASE, MoonPhases.MoonCondition.NEW_MOON));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip2, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip2, flagIn);
        tooltip2.add(Component.translatable("tooltip.ars_hex.moon_dial.cycle"));
        tooltip2.add(Component.translatable("tooltip.ars_hex.moon_dial.current", stack.getOrDefault(ModRegistry.MOON_PHASE.get(), MoonPhases.MoonCondition.NONE).getName()));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (player.isShiftKeyDown() && !level.isClientSide()) {
            ItemStack stack = player.getItemInHand(hand);
            MoonPhases.MoonCondition moonPhase = stack.getOrDefault(ModRegistry.MOON_PHASE.get(), MoonPhases.MoonCondition.NONE);

            int currentIndex = -1;
            for (int i = 0; i < SELECTABLE_PHASES.length; i++) {
                if (SELECTABLE_PHASES[i] == moonPhase) {
                    currentIndex = i;
                    break;
                }
            }

            MoonPhases.MoonCondition nextPhase = SELECTABLE_PHASES[(currentIndex + 1) % SELECTABLE_PHASES.length];
            stack.set(ModRegistry.MOON_PHASE.get(), nextPhase);
            //PortUtil.sendMessage(player, "Moon phase set to: " + nextPhase.getName());
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

}


