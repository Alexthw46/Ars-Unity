package com.alexthw.ars_hex.datagen;

import com.alexthw.ars_hex.ArsHex;
import com.alexthw.ars_hex.malum.MalumCompat;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DamageTypesProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, DamageTypesProvider::bootstrap);


    public static void bootstrap(BootstrapContext<DamageType> ctx) {
        ctx.register(MalumCompat.SOUL_SHATTER, new DamageType("soul_shatter", 0.1F));
    }

    public DamageTypesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(ArsHex.MODID));
    }

    @Override
    @NotNull
    public String getName() {
        return "Ars Hex-Unity's Damage Type Data";
    }
}