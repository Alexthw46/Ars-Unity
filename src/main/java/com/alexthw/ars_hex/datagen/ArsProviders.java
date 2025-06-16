package com.alexthw.ars_hex.datagen;

import com.alexthw.ars_hex.ArsHex;
import com.alexthw.ars_hex.glyphs.EffectSoulShatter;
import com.alexthw.ars_hex.malum.MalumCompat;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hollingsworth.arsnouveau.common.crafting.recipes.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.GlyphRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.ImbuementRecipe;
import com.hollingsworth.arsnouveau.common.datagen.*;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.mojang.serialization.JsonOps;
import com.sammy.malum.MalumMod;
import com.sammy.malum.registry.common.item.MalumItems;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.alexthw.ars_hex.datagen.Setup.provider;
import static com.hollingsworth.arsnouveau.setup.registry.RegistryHelper.getRegistryName;

public class ArsProviders {

    static String root = ArsHex.MODID;

    public static class GlyphProvider extends GlyphRecipeProvider {

        public GlyphProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public void collectJsons(CachedOutput cache) {

            Path output = this.generator.getPackOutput().getOutputFolder();
            add(get(EffectSoulShatter.INSTANCE).withItem(ItemsRegistry.MANIPULATION_ESSENCE).withItem(MalumItems.WICKED_SPIRIT.get()).withItem(MalumItems.SOUL_STAINED_STEEL_SWORD.get()));

            for (GlyphRecipe recipe : recipes) {
                Path path = getScribeGlyphPath(output, recipe.output.getItem());
                JsonElement wrapped = wrapModCondition(GlyphRecipe.CODEC.encodeStart(JsonOps.INSTANCE, recipe).getOrThrow(), MalumMod.MALUM);
                saveStable(cache, wrapped, path);
            }
        }

        protected static Path getScribeGlyphPath(Path pathIn, Item glyph) {
            return pathIn.resolve("data/" + root + "/recipe/" + getRegistryName(glyph).getPath() + ".json");
        }

        @Override
        public @NotNull String getName() {
            return "Example Glyph Recipes";
        }
    }

    private static JsonElement wrapModCondition(JsonElement element, String modid) {
        // wrap with a neoforge:condition block
        var condition = new JsonObject();
        condition.addProperty("type", "neoforge:mod_loaded");
        condition.addProperty("modid", modid);
        var array = new JsonArray();
        array.add(condition);
        element.getAsJsonObject().add("neoforge:conditions", array);
        return element;
    }

    public static class EnchantingAppProvider extends ApparatusRecipeProvider {

        public EnchantingAppProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public void collectJsons(CachedOutput cache) {

            recipes.add(builder()
                    .withReagent(MalumItems.SOUL_STAINED_STEEL_SCYTHE.get())
                    .withPedestalItem(Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD))
                    .withPedestalItem(RecipeDatagen.SOURCE_GEM_BLOCK)
                    .withPedestalItem(3, RecipeDatagen.ARCHWOOD_LOG)
                    .withResult(MalumCompat.ENCHANTER_SCYTHE.get())
                    .keepNbtOfReagent(true)
                    .build()
            );


            Path output = this.generator.getPackOutput().getOutputFolder();
            for (ApparatusRecipeBuilder.RecipeWrapper<? extends EnchantingApparatusRecipe> g : recipes) {
                if (g != null) {
                    Path path = getRecipePath(output, g.id().getPath());
                    JsonElement wrapped = wrapModCondition(g.serialize(), MalumMod.MALUM);
                    saveStable(cache, wrapped, path);
                }
            }

        }

        protected static Path getRecipePath(Path pathIn, String str) {
            return pathIn.resolve("data/" + root + "/recipe/" + str + ".json");
        }

        @Override
        public @NotNull String getName() {
            return "Example Apparatus";
        }
    }

    public static class ImbuementProvider extends ImbuementRecipeProvider {

        public ImbuementProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public @NotNull CompletableFuture<?> run(@NotNull CachedOutput pOutput) {
            collectJsons(pOutput);
            List<CompletableFuture<?>> futures = new ArrayList<>();
            return provider.thenCompose((registry) -> {
                for (ImbuementRecipe g : recipes) {
                    Path path = getRecipePath(output, g.id.getPath());
                    futures.add(DataProvider.saveStable(pOutput, registry, ImbuementRecipe.CODEC, g, path));
                }
                return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            });
        }

        @Override
        public void collectJsons(CachedOutput cache) {

            /*
            recipes.add(new ImbuementRecipe("example_focus", Ingredient.of(Items.AMETHYST_SHARD), new ItemStack(ItemsRegistry.SUMMONING_FOCUS, 1), 5000)
                    .withPedestalItem(ItemsRegistry.WILDEN_TRIBUTE)
            );
            */
        }

        protected Path getRecipePath(Path pathIn, String str) {
            return pathIn.resolve("data/" + root + "/recipe/" + str + ".json");
        }

        @Override
        public @NotNull String getName() {
            return "Example Imbuement";
        }

    }

}
