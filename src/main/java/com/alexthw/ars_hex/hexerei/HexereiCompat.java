package com.alexthw.ars_hex.hexerei;

import com.alexthw.ars_hex.hexerei.broom.ArchwoodBroomStick;
import com.alexthw.ars_hex.hexerei.broom.EnchanterBroomEntity;
import com.alexthw.ars_hex.hexerei.broom.MagebloomBrush;
import com.hollingsworth.arsnouveau.api.registry.SpellCasterRegistry;
import com.hollingsworth.arsnouveau.api.spell.SpellCaster;
import com.hollingsworth.arsnouveau.client.gui.SpellTooltip;
import com.hollingsworth.arsnouveau.setup.config.Config;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import com.mojang.datafixers.util.Either;
import net.joefoxe.hexerei.client.renderer.entity.BroomType;
import net.joefoxe.hexerei.client.renderer.entity.custom.BroomEntity;
import net.joefoxe.hexerei.client.renderer.entity.render.BroomRenderer;
import net.joefoxe.hexerei.item.custom.BroomItemRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.alexthw.ars_hex.ArsHex.prefix;
import static com.alexthw.ars_hex.registry.ModRegistry.*;

public class HexereiCompat {

    public static DeferredHolder<EntityType<?>, EntityType<BroomEntity>> ARCHWOOD_BROOM_ENTITY;

    public static void init() {
        ARCHWOOD_BROOM = ITEMS.register("archwood_broom", () -> new ArchwoodBroomStick("archwood", new Item.Properties().stacksTo(1).component(DataComponentRegistry.SPELL_CASTER, new SpellCaster())));
        MAGEBLOOM_BRUSH = ITEMS.register("magebloom_brush", () -> new MagebloomBrush(new Item.Properties().durability(100)));
        WET_MAGEBLOOM_BRUSH = ITEMS.register("wet_magebloom_brush", () -> new Item(new Item.Properties()) {
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
                tooltip.add(Component.translatable("tooltip.hexerei.wet_broom_brush").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(10066329))));
                super.appendHoverText(stack, context, tooltip, flagIn);
            }
        });
        ARCHWOOD_BROOM_ENTITY = ENTITY_TYPES.register("archwood_broom", () -> EntityType.Builder.of((EntityType<BroomEntity> broomEntityEntityType, Level world) -> new EnchanterBroomEntity(broomEntityEntityType, world), MobCategory.MISC).sized(1.175F, 0.3625F).setShouldReceiveVelocityUpdates(true).setTrackingRange(10).updateInterval(1).build(prefix("archwood_broom").toString()));

        NeoForge.EVENT_BUS.addListener(HexereiCompat::registerTooltipComponents);
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

    @OnlyIn(Dist.CLIENT)
    public static void registerTooltipComponents(RenderTooltipEvent.GatherComponents event) {
        if (event.getItemStack().getItem() instanceof ArchwoodBroomStick && SpellCasterRegistry.from(event.getItemStack()) instanceof SpellCaster caster) {
            if (!Screen.hasShiftDown() && Config.GLYPH_TOOLTIPS.get() && !caster.isSpellHidden() && !caster.getSpell().isEmpty())
                event.getTooltipElements().add(Either.right(new SpellTooltip(caster)));
        }
    }


    public static void postInit() {
        BroomType.create("archwood", ARCHWOOD_BROOM.get(), 0.6f);
    }

    public static DeferredHolder<Item, ? extends Item> ARCHWOOD_BROOM, MAGEBLOOM_BRUSH, WET_MAGEBLOOM_BRUSH;

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ARCHWOOD_BROOM_ENTITY.get(), BroomRenderer::new);
    }
}