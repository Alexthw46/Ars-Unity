package com.alexthw.ars_hex.datagen;

import com.alexthw.ars_hex.ArsHex;
import com.alexthw.ars_hex.hexerei.HexereiCompat;
import com.alexthw.ars_hex.malum.MalumCompat;
import com.hollingsworth.arsnouveau.common.datagen.BlockTagProvider;
import com.hollingsworth.arsnouveau.common.datagen.ItemTagProvider;
import com.sammy.malum.registry.common.MalumTags;
import net.joefoxe.hexerei.block.ModBlocks;
import net.joefoxe.hexerei.item.ModItems;
import net.joefoxe.hexerei.util.HexereiTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AHItemTagProvider extends ItemTagsProvider {

    public AHItemTagProvider(DataGenerator pGenerator, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper, BlockTagsProvider blockTagsProvider) {
        super(pGenerator.getPackOutput(), lookupProvider, blockTagsProvider.contentsGetter(), ArsHex.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(HexereiTags.Items.BROOM_BRUSH).addOptional(HexereiCompat.MAGEBLOOM_BRUSH.getId());
        tag(ItemTags.DURABILITY_ENCHANTABLE).addOptionalTag(HexereiTags.Items.BROOM_BRUSH);
        tag(ItemTagProvider.SHARD_TAG).addOptional(ModItems.SELENITE_SHARD.getId());
        tag(MalumTags.ItemTags.MAGIC_CAPABLE_WEAPON).addOptional(MalumCompat.ENCHANTER_SCYTHE.getId());
        tag(Tags.Items.MELEE_WEAPON_TOOLS).addOptional(MalumCompat.ENCHANTER_SCYTHE.getId());
    }

    @Override
    public @NotNull String getName() {
        return "Ars Hex Item Tags";
    }

}

class AHBlockTagsProvider extends BlockTagsProvider {

    public AHBlockTagsProvider(DataGenerator gen, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(gen.getPackOutput(), provider, ArsHex.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        // add tags here
        tag(BlockTagProvider.BUDDING_BLOCKS).addOptional(ModBlocks.BUDDING_SELENITE.getId());
        tag(BlockTagProvider.CLUSTER_BLOCKS).addOptional(ModBlocks.SELENITE_CLUSTER.getId());
    }

}