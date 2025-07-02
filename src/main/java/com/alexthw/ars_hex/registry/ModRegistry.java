package com.alexthw.ars_hex.registry;

import com.hollingsworth.arsnouveau.setup.registry.CreativeTabRegistry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import static com.alexthw.ars_hex.ArsHex.MODID;
import static net.minecraft.core.registries.Registries.*;

public class ModRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(SOUND_EVENT, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ENTITY_TYPE, MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(PARTICLE_TYPE, MODID);


    public static void registerRegistries(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        PARTICLES.register(bus);
        ENTITY_TYPES.register(bus);
        SOUNDS.register(bus);
        bus.addListener(ModRegistry::doTabsStuff);
    }

//    //this is an example of how to register a sound. You also need to add the sound to the sound.json file, referencing your ogg files, and a texture for the button under textures/sounds.
//    //this example will use one of the existing sounds randomly
//    public static DeferredHolder<SoundEvent, SoundEvent> EXAMPLE_FAMILY = SOUNDS.register("example_sound", () -> makeSound("example_sound"));
//    public static SpellSound EXAMPLE_SPELL_SOUND = new SpellSound(ModRegistry.EXAMPLE_FAMILY, Component.literal("Example"), prefix("example_random_sound"));

    static SoundEvent makeSound(@NotNull String name) {
        return SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, name));
    }

    private static void doTabsStuff(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == CreativeTabRegistry.BLOCKS.get()) {
            for (var item : ITEMS.getEntries()) {
                event.accept(item::get);
            }
        }
    }
}
