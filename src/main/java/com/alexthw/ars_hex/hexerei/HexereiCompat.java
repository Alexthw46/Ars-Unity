package com.alexthw.ars_hex.hexerei;

import net.joefoxe.hexerei.client.renderer.entity.BroomType;
import net.joefoxe.hexerei.item.custom.BroomItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.alexthw.ars_hex.registry.ModRegistry.ITEMS;


public class HexereiCompat {

    public static void init() {
        ARCHWOOD_BROOM = ITEMS.register("archwood_broom", () -> new ArchwoodBroomStick("archwood", new Item.Properties().stacksTo(1)));
        MAGEBLOOM_BRUSH = ITEMS.register("magebloom_brush", () -> new MagebloomBrush(new Item.Properties().durability(100)));
        WET_MAGEBLOOM_BRUSH = ITEMS.register("wet_magebloom_brush", () -> new Item(new Item.Properties()) {
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
                tooltip.add(Component.translatable("tooltip.hexerei.wet_broom_brush").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(10066329))));
                super.appendHoverText(stack, context, tooltip, flagIn);
            }
        });

    }

    @OnlyIn(Dist.CLIENT)
    public static void layerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HexereiModels.MagebloomBrush.LAYER_LOCATION, HexereiModels.MagebloomBrush::createBodyLayerNone);
        event.registerLayerDefinition(HexereiModels.ArchwoodStick.LAYER_LOCATION, HexereiModels.ArchwoodStick::createBodyLayerNone);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        final BroomItemRenderer broomRenderer = new BroomItemRenderer();
        event.registerItem(new IClientItemExtensions() {
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return broomRenderer.getRenderer();
            }
        }, ARCHWOOD_BROOM.get());

    }

    public static void postInit() {
        BroomType.create("archwood", ARCHWOOD_BROOM.get(), 0.6f);
    }

    public static DeferredHolder<Item, ? extends Item> ARCHWOOD_BROOM, MAGEBLOOM_BRUSH, WET_MAGEBLOOM_BRUSH;

}
