package com.alexthw.ars_hex;

import com.alexthw.ars_hex.hexerei.HexereiCompat;
import com.alexthw.ars_hex.iss.ISSCompat;
import com.alexthw.ars_hex.malum.MalumCompat;
import com.alexthw.ars_hex.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.documentation.ReloadDocumentationEvent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ArsHex.MODID)
public class ArsHex {
    public static final String MODID = "ars_hex";

    public ArsHex(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, HexConfigs.COMMON_SPEC);
        ModRegistry.registerRegistries(modEventBus);
        if (ModList.get().isLoaded("malum")) {
            MalumCompat.init();
        }
        if (ModList.get().isLoaded("hexerei")) {
            HexereiCompat.init(modEventBus);
        }
        ArsNouveauRegistry.init();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::layerDefinitions);
        modEventBus.addListener(this::registerClientExtensions);
        modEventBus.addListener(this::registerRenderers);
        modEventBus.addListener(EventPriority.LOWEST, this::registerParticles);
        if (ModList.get().isLoaded("irons_spellbooks")) {
            ISSCompat.init(modEventBus);
            NeoForge.EVENT_BUS.addListener(ISSCompat::damageTweaksArs);
            NeoForge.EVENT_BUS.addListener(ISSCompat::damageTweaksEISS);
        }
        NeoForge.EVENT_BUS.addListener(ArsHex::initDocs);
    }

    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private void setup(final FMLCommonSetupEvent event) {
        //ArsNouveauRegistry.registerSounds();
        if (ModList.get().isLoaded("malum")) {
            event.enqueueWork(MalumCompat::postInit);
        }
        if (ModList.get().isLoaded("hexerei")) {
            event.enqueueWork(HexereiCompat::postInit);
        }
        if (ModList.get().isLoaded("irons_spellbooks")) {
            event.enqueueWork(ISSCompat::postInit);
        }
    }

    private void layerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        if (ModList.get().isLoaded("hexerei")) {
            HexereiCompat.layerDefinitions(event);
        }
    }

    private void registerClientExtensions(RegisterClientExtensionsEvent event) {
        if (ModList.get().isLoaded("hexerei")) {
            HexereiCompat.registerClientExtensions(event);
        }
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        if (ModList.get().isLoaded("hexerei")) {
            HexereiCompat.registerRenderers(event);
        }
    }

    public static void initDocs(ReloadDocumentationEvent.AddEntries event) {
        if (ModList.get().isLoaded("hexerei")) {
            HexereiCompat.initDocs();
        }
        if (ModList.get().isLoaded("malum")) {
            MalumCompat.initDocs();
        }
        if (ModList.get().isLoaded("irons_spellbooks")) {
            ISSCompat.initDocs();
        }
    }

    public void registerParticles(RegisterParticleProvidersEvent event) {
        if (ModList.get().isLoaded("irons_spellbooks")) {
            ISSCompat.registerParticles(event);
        }
        if (ModList.get().isLoaded("hexerei")) {
            HexereiCompat.registerParticles(event);
        }
    }
}
