package com.alexthw.ars_hex.datagen;

import alexthw.ars_elemental.registry.ModRegistry;
import com.alexthw.ars_hex.ArsHex;
import com.alexthw.sauce.registry.SauceTags;
import com.hollingsworth.arsnouveau.setup.registry.DamageTypesRegistry;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.datagen.DamageTypeTagGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.registry.common.tag.LodestoneDamageTypeTags;

import java.util.concurrent.CompletableFuture;

public class AHDamageTypeTagsProvider extends DamageTypeTagsProvider {

    public AHDamageTypeTagsProvider(DataGenerator generator, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), lookupProvider, ArsHex.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(SauceTags.AIR_DAMAGE).addOptional(ISSDamageTypes.LIGHTNING_MAGIC.location());
        tag(SauceTags.EARTH_DAMAGE).addOptional(ISSDamageTypes.NATURE_MAGIC.location());
        tag(SauceTags.FIRE_DAMAGE).addOptional(ISSDamageTypes.FIRE_MAGIC.location());
        tag(SauceTags.WATER_DAMAGE).addOptional(ISSDamageTypes.ICE_MAGIC.location());

        tag(DamageTypeTagGenerator.FIRE_MAGIC).addOptional(DamageTypesRegistry.FLARE.location()).addOptional(ModRegistry.MAGIC_FIRE.location());
        tag(DamageTypeTagGenerator.ICE_MAGIC).addOptional(DamageTypesRegistry.COLD_SNAP.location());
        tag(DamageTypeTagGenerator.LIGHTNING_MAGIC).addOptional(ModRegistry.SPARK.location());
        tag(DamageTypeTagGenerator.NATURE_MAGIC).addOptional(DamageTypesRegistry.CRUSH.location()).addOptional(ModRegistry.POISON.location());

        //TODO remove if Sammy; fixes tag issue
        tag(LodestoneDamageTypeTags.IS_MAGIC).addTag(Tags.DamageTypes.IS_MAGIC);
    }
}
