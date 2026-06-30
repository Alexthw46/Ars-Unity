package com.alexthw.ars_hex.eidolon;

import alexthw.eidolon_repraised.client.particle.SlashParticleData;
import alexthw.eidolon_repraised.client.particle.SlashParticleType;
import com.hollingsworth.arsnouveau.api.particle.PropertyParticleOptions;
import com.hollingsworth.arsnouveau.api.particle.PropertyParticleType;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.ColorProperty;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.ParticleTypeProperty;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static com.alexthw.ars_hex.registry.ModRegistry.PARTICLES;

public class EidolonCompat {

    //STILL WIP
    public static DeferredHolder<ParticleType<?>, PropertyParticleType> SLASH_PARTICLE;

    public static void init(IEventBus modEventBus) {
        SLASH_PARTICLE = PARTICLES.register("slash_particle", PropertyParticleType::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(SLASH_PARTICLE.get(), sprites -> new SlashWrappedProvider(
                sprites,
                SlashParticleType.Factory::new
        ));
    }

    public static void postInit() {
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(SLASH_PARTICLE.get(), true));
    }

    public static void initDocs() {
    }

    @OnlyIn(Dist.CLIENT)
    private static final class SlashWrappedProvider implements ParticleProvider<PropertyParticleOptions> {
        private final ParticleProvider<SlashParticleData> particleProvider;

        private SlashWrappedProvider(SpriteSet spriteSet, Function<SpriteSet, ParticleProvider<SlashParticleData>> particleProviderFactory) {
            this.particleProvider = particleProviderFactory.apply(spriteSet);
        }

        @Override
        public Particle createParticle(PropertyParticleOptions data, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SlashParticleData slashData = SlashParticleData.create(data.getType());
            slashData.angle(180F)
                    .width(0.35F)
                    .radius(0.25F)
                    .highlight(0.2F)
                    .lifetime(6);
            var particleData = ParticleTypeProperty.PARTICLE_TYPES.get(data.getType());
            if (particleData != null && particleData.acceptsColor()) {
                ColorProperty colorProperty = data.colorProp();
                if (!colorProperty.isTintDisabled()) {
                    ParticleColor color = colorProperty.particleColor;
                    slashData.color(color.getRed(), color.getGreen(), color.getBlue());
                }
            }
            return particleProvider.createParticle(slashData, level, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
