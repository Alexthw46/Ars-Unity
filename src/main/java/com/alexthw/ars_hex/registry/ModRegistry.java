package com.alexthw.ars_hex.registry;

import com.alexthw.ars_hex.common.MoonDial;
import com.alexthw.ars_hex.common.MoonPhases;
import com.alexthw.sauce.Sauce;
import com.hollingsworth.arsnouveau.setup.registry.CreativeTabRegistry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.alexthw.ars_hex.ArsHex.MODID;
import static net.minecraft.core.registries.Registries.ENTITY_TYPE;
import static net.minecraft.core.registries.Registries.PARTICLE_TYPE;
import static net.minecraft.core.registries.Registries.SOUND_EVENT;

public class ModRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(SOUND_EVENT, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ENTITY_TYPE, MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(PARTICLE_TYPE, MODID);
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Sauce.MODID);


    public static void registerRegistries(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        DATA_COMPONENT_TYPES.register(bus);
        PARTICLES.register(bus);
        ENTITY_TYPES.register(bus);
        SOUNDS.register(bus);
        bus.addListener(ModRegistry::doTabsStuff);
    }

    public static final DeferredHolder<Item, ? extends Item> MOON_DIAL;
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MoonPhases.MoonCondition>> MOON_PHASE;

    static {
        MOON_PHASE = DATA_COMPONENT_TYPES.register("moon_phase", () -> DataComponentType.<MoonPhases.MoonCondition>builder().persistent(MoonPhases.MoonCondition.CODEC).networkSynchronized(MoonPhases.MoonCondition.STREAM_CODEC).build());
        MOON_DIAL = ITEMS.register("moon_dial", MoonDial::new);
    }

    private static void doTabsStuff(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == CreativeTabRegistry.BLOCKS.get()) {
            for (var item : ITEMS.getEntries()) {
                event.accept(item::get);
            }
        }
    }
}
