package com.alexthw.ars_hex.datagen;

import com.alexthw.ars_hex.ArsHex;
import com.alexthw.ars_hex.hexerei.HexereiCompat;
import net.joefoxe.hexerei.util.HexereiTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AHItemTagProvider extends ItemTagsProvider {

    public AHItemTagProvider(DataGenerator pGenerator, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider pBlockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator.getPackOutput(), lookupProvider, pBlockTagsProvider.contentsGetter(), ArsHex.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(HexereiTags.Items.BROOM_BRUSH).addOptional(HexereiCompat.MAGEBLOOM_BRUSH.getId());

    }

    @Override
    public @NotNull String getName() {
        return "Ars Hex Item Tags";
    }

}