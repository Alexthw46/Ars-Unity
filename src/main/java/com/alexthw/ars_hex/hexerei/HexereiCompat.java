package com.alexthw.ars_hex.hexerei;

import alexthw.ars_elemental.documentation.AEDocumentation;
import alexthw.ars_elemental.registry.ModItems;
import com.alexthw.ars_hex.hexerei.broom.ArchwoodBroomStick;
import com.alexthw.ars_hex.hexerei.broom.EnchanterBroomEntity;
import com.alexthw.ars_hex.hexerei.broom.MagebloomBrush;
import com.alexthw.sauce.ArsNouveauRegistry;
import com.alexthw.sauce.common.recipe.ElementalArmorRecipe;
import com.alexthw.sauce.documentation.AEArmorEntry;
import com.hollingsworth.arsnouveau.api.documentation.ReloadDocumentationEvent;
import com.hollingsworth.arsnouveau.api.documentation.builder.DocEntryBuilder;
import com.hollingsworth.arsnouveau.api.documentation.entry.TextEntry;
import com.hollingsworth.arsnouveau.api.particle.PropertyParticleType;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.ParticleTypeProperty;
import com.hollingsworth.arsnouveau.api.registry.SpellCasterRegistry;
import com.hollingsworth.arsnouveau.api.spell.SpellCaster;
import com.hollingsworth.arsnouveau.client.gui.SpellTooltip;
import com.hollingsworth.arsnouveau.client.particle.WrappedProvider;
import com.hollingsworth.arsnouveau.setup.config.Config;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import com.mojang.datafixers.util.Either;
import net.joefoxe.hexerei.client.renderer.entity.BroomType;
import net.joefoxe.hexerei.client.renderer.entity.custom.BroomEntity;
import net.joefoxe.hexerei.client.renderer.entity.render.BroomRenderer;
import net.joefoxe.hexerei.data.recipes.MixingCauldronRecipe;
import net.joefoxe.hexerei.item.custom.BroomItemRenderer;
import net.joefoxe.hexerei.particle.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.alexthw.ars_hex.ArsHex.prefix;
import static com.alexthw.ars_hex.registry.ModRegistry.*;
import static com.hollingsworth.arsnouveau.api.registry.DocumentationRegistry.GETTING_STARTED;
import static com.hollingsworth.arsnouveau.setup.registry.Documentation.addPage;
import static net.joefoxe.hexerei.data.recipes.ModRecipeTypes.MIXING_CAULDRON_TYPE;

public class HexereiCompat {

    public static DeferredHolder<EntityType<?>, EntityType<BroomEntity>> ARCHWOOD_BROOM_ENTITY;

    public static void init(IEventBus modEventBus) {

        // Register items
        ARCHWOOD_BROOM = ITEMS.register("archwood_broom", () -> new ArchwoodBroomStick("archwood", new Item.Properties().stacksTo(1).component(DataComponentRegistry.SPELL_CASTER, new SpellCaster())));
        MAGEBLOOM_BRUSH = ITEMS.register("magebloom_brush", () -> new MagebloomBrush(new Item.Properties().durability(100)));
        WET_MAGEBLOOM_BRUSH = ITEMS.register("wet_magebloom_brush", () -> new Item(new Item.Properties()) {
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
                tooltip.add(Component.translatable("tooltip.hexerei.wet_broom_brush").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(10066329))));
                super.appendHoverText(stack, context, tooltip, flagIn);
            }
        });
        // Register entity types
        ARCHWOOD_BROOM_ENTITY = ENTITY_TYPES.register("archwood_broom", () -> EntityType.Builder.of((EntityType<BroomEntity> broomEntityEntityType, Level world) -> new EnchanterBroomEntity(broomEntityEntityType, world), MobCategory.MISC).sized(1.175F, 0.3625F).setShouldReceiveVelocityUpdates(true).setTrackingRange(10).updateInterval(1).build(prefix("archwood_broom").toString()));

        // Register listeners
        NeoForge.EVENT_BUS.addListener(HexereiCompat::registerTooltipComponents);

        // Register particle types
        BROOM_LEAVES_1 = PARTICLES.register("broom_leaves", PropertyParticleType::new);
        BROOM_LEAVES_2 = PARTICLES.register("broom_leaves_2", PropertyParticleType::new);
        BROOM_LEAVES_3 = PARTICLES.register("broom_leaves_3", PropertyParticleType::new);
        FOG = PARTICLES.register("fog_spell", PropertyParticleType::new);
        BLOOD = PARTICLES.register("blood_spell", PropertyParticleType::new);
        OWL_1 = PARTICLES.register("owl_teleport", PropertyParticleType::new);
        OWL_2 = PARTICLES.register("owl_teleport_barn", PropertyParticleType::new);
        OWL_3 = PARTICLES.register("owl_teleport_snow", PropertyParticleType::new);
        BROOM_MOON_1 = PARTICLES.register("moon_leaves", PropertyParticleType::new);
        BROOM_MOON_2 = PARTICLES.register("moon_leaves_2", PropertyParticleType::new);
        BROOM_MOON_3 = PARTICLES.register("moon_leaves_3", PropertyParticleType::new);
        STAR_BRUSH = PARTICLES.register("star_brush", PropertyParticleType::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BROOM_LEAVES_1.get(), (sprites -> new WrappedProvider(ModParticleTypes.BROOM_3.get(), BroomParticle.Factory::new)));
        event.registerSpriteSet(BROOM_LEAVES_2.get(), (sprites -> new WrappedProvider(ModParticleTypes.BROOM_4.get(), BroomParticle.Factory::new)));
        event.registerSpriteSet(BROOM_LEAVES_3.get(), (sprites -> new WrappedProvider(ModParticleTypes.BROOM_5.get(), BroomParticle.Factory::new)));
        event.registerSpriteSet(FOG.get(), (sprites -> new WrappedProvider(ModParticleTypes.FOG.get(), FogParticle.Factory::new)));
        event.registerSpriteSet(BLOOD.get(), (sprites -> new WrappedProvider(ModParticleTypes.BLOOD.get(), BloodParticle.Factory::new)));
        event.registerSpriteSet(OWL_1.get(), (sprites -> new WrappedProvider(ModParticleTypes.OWL_TELEPORT.get(), OwlTeleportParticle.Factory::new)));
        event.registerSpriteSet(OWL_2.get(), (sprites -> new WrappedProvider(ModParticleTypes.OWL_TELEPORT_BARN.get(), OwlTeleportParticle.Factory::new)));
        event.registerSpriteSet(OWL_3.get(), (sprites -> new WrappedProvider(ModParticleTypes.OWL_TELEPORT_SNOWY.get(), OwlTeleportParticle.Factory::new)));
        event.registerSpriteSet(BROOM_MOON_1.get(), (sprites -> new WrappedProvider(ModParticleTypes.MOON_BRUSH_2.get(), MoonBroomParticle.Factory::new)));
        event.registerSpriteSet(BROOM_MOON_2.get(), (sprites -> new WrappedProvider(ModParticleTypes.MOON_BRUSH_3.get(), MoonBroomParticle.Factory::new)));
        event.registerSpriteSet(BROOM_MOON_3.get(), (sprites -> new WrappedProvider(ModParticleTypes.MOON_BRUSH_4.get(), MoonBroomParticle.Factory::new)));
        event.registerSpriteSet(STAR_BRUSH.get(), (sprites -> new WrappedProvider(ModParticleTypes.STAR_BRUSH.get(), StarBroomParticle.Provider::new)));
    }

    @OnlyIn(Dist.CLIENT)
    public static void layerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HexereiModels.MagebloomBrush.LAYER_LOCATION, HexereiModels.MagebloomBrush::createBodyLayerNone);
        event.registerLayerDefinition(HexereiModels.ArchwoodStick.LAYER_LOCATION, HexereiModels.ArchwoodStick::createBodyLayerNone);
    }

    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        final BroomItemRenderer broomRenderer = new BroomItemRenderer();
        event.registerItem(new IClientItemExtensions() {
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return broomRenderer.getRenderer();
            }
        }, ARCHWOOD_BROOM.get());

    }

    public static void registerTooltipComponents(RenderTooltipEvent.GatherComponents event) {
        if (event.getItemStack().getItem() instanceof ArchwoodBroomStick && SpellCasterRegistry.from(event.getItemStack()) instanceof SpellCaster caster) {
            if (!Screen.hasShiftDown() && Config.GLYPH_TOOLTIPS.get() && !caster.isSpellHidden() && !caster.getSpell().isEmpty())
                event.getTooltipElements().add(Either.right(new SpellTooltip(caster)));
        }
    }


    public static void postInit() {
        ArsNouveauRegistry.recipePageConsumers.add((holder, cir) -> {
            if (holder.value() instanceof MixingCauldronRecipe)
                //noinspection unchecked
                cir.getReturnValue().add(CauldronEntry.create((RecipeHolder<MixingCauldronRecipe>) holder));
        });
        BroomType.create("archwood", ARCHWOOD_BROOM.get(), 0.6f);
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(BROOM_LEAVES_1.get(), true));
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(BROOM_LEAVES_2.get(), true));
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(BROOM_LEAVES_3.get(), true));
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(FOG.get(), true));
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(BLOOD.get(), true));
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(OWL_1.get(), true));
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(OWL_2.get(), true));
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(OWL_3.get(), true));
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(BROOM_MOON_1.get(), true));
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(BROOM_MOON_2.get(), true));
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(BROOM_MOON_3.get(), true));
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(STAR_BRUSH.get(), true));
    }

    public static DeferredHolder<Item, ? extends Item> ARCHWOOD_BROOM, MAGEBLOOM_BRUSH, WET_MAGEBLOOM_BRUSH;

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ARCHWOOD_BROOM_ENTITY.get(), BroomRenderer::new);
    }

    public static DeferredHolder<ParticleType<?>, PropertyParticleType> BROOM_LEAVES_1, BROOM_LEAVES_2, BROOM_LEAVES_3,
            OWL_1, OWL_2, OWL_3,
            BROOM_MOON_1, BROOM_MOON_2, BROOM_MOON_3,
            FOG, BLOOD, STAR_BRUSH;

    public static void initDocs() {
        addPage(new DocEntryBuilder(GETTING_STARTED, "hexerei_compat")
                .withIcon(ARCHWOOD_BROOM.get())
                .withTextPage("ars_hex.page.archwood_broom")
                .withCraftingPages(prefix("archwood_broom_from_mixing_cauldron"), ARCHWOOD_BROOM.get())
                .withTextPage("ars_hex.page.magebloom_brush")
                .withCraftingPages(prefix("wet_magebloom_brush_from_mixing_cauldron"), WET_MAGEBLOOM_BRUSH.get())
        );
    }

}