package com.alexthw.ars_hex.hexerei;

import com.google.gson.JsonObject;
import com.hollingsworth.arsnouveau.api.documentation.DocAssets;
import com.hollingsworth.arsnouveau.api.documentation.DocClientUtils;
import com.hollingsworth.arsnouveau.api.documentation.SinglePageCtor;
import com.hollingsworth.arsnouveau.api.documentation.entry.PedestalRecipeEntry;
import com.hollingsworth.arsnouveau.api.documentation.export.DocExporter;
import com.hollingsworth.arsnouveau.client.gui.documentation.BaseDocScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.joefoxe.hexerei.data.recipes.FluidMixingRecipe;
import net.joefoxe.hexerei.data.recipes.MixingCauldronRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.Arrays;

import static com.alexthw.ars_hex.ArsHex.prefix;
import static net.joefoxe.hexerei.tileentity.renderer.MixingCauldronRenderer.renderFluidGUI;

public class CauldronEntry extends PedestalRecipeEntry {
    RecipeHolder<? extends MixingCauldronRecipe> cauldronRecipe;

    FluidIngredient reagentFluidStack;

    public CauldronEntry(RecipeHolder<? extends MixingCauldronRecipe> recipe, BaseDocScreen parent, int x, int y, int width, int height) {
        super(parent, x, y, width, height);
        this.cauldronRecipe = recipe;
        this.title = Component.translatable("block.hexerei.mixing_cauldron");
        this.image = new DocAssets.BlitInfo(recipe.value().getHeatCondition() == FluidMixingRecipe.HeatCondition.NONE ? prefix("textures/gui/documentation/doc_detail_mixing_cauldron.png") : prefix("textures/gui/documentation/doc_detail_mixing_cauldron_heated.png"), 92, 127);
        if (recipe != null && recipe.value() != null) {
            this.outputStack = recipe.value().getOutput();
            this.ingredients = recipe.value().getIngredients();
            this.reagentFluidStack = recipe.value().getFluidIngredient();
        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

        if (!reagentFluidStack.isEmpty()) {
            int itemX = getX() + width / 2 - 6;
            int itemY = getY() + 24 + 20;
            // Temp fix for missing proper fluid rendering in doc screens
            setTooltipIfHovered(DocClientUtils.renderIngredient(guiGraphics, itemX, itemY, mouseX, mouseY, Ingredient.of(Arrays.stream(reagentFluidStack.getStacks()).map(i -> i.getFluid().getBucket().getDefaultInstance()))));
        }
    }

    public static SinglePageCtor create(RecipeHolder<? extends MixingCauldronRecipe> recipe) {
        return (parent, x, y, width, height) -> new CauldronEntry(recipe, parent, x, y, width, height);
    }

    @Override
    public void addExportProperties(JsonObject object) {
        super.addExportProperties(object);
        if (cauldronRecipe != null) {
            object.addProperty(DocExporter.RECIPE_PROPERTY, cauldronRecipe.id().toString());
        }
    }
}
