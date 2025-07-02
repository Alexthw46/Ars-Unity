package com.alexthw.ars_hex.iss;

import com.hollingsworth.arsnouveau.api.event.SpellDamageEvent.Pre;
import com.hollingsworth.arsnouveau.api.particle.PropertyParticleType;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.ParticleTypeProperty;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.client.particle.WrappedProvider;
import com.hollingsworth.arsnouveau.common.armor.AnimatedMagicArmor;
import com.mojang.datafixers.util.Pair;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.particle.*;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;

import static com.alexthw.ars_hex.registry.ModRegistry.PARTICLES;

public class ISSCompat {

    public static Map<SpellSchool, SchoolType> schoolToSchoolMap = new HashMap<>();

    public static Map<SchoolType, Pair<Holder<Attribute>, Holder<Attribute>>> schoolAttributes = new HashMap<>();

    public static void init(IEventBus modEventBus) {
        WISP_PARTICLE = PARTICLES.register("wisp_iss", PropertyParticleType::new);
        SNOWFLAKE_PARTICLE = PARTICLES.register("snowflake_iss", PropertyParticleType::new);
        ELECTRICITY_PARTICLE = PARTICLES.register("electricity_iss", PropertyParticleType::new);
        FIRE_PARTICLE = PARTICLES.register("fire_iss", PropertyParticleType::new);
        FIREFLY_PARTICLE = PARTICLES.register("firefly_iss", PropertyParticleType::new);
    }

    public static void postInit() {
        schoolToSchoolMap.put(SpellSchools.ELEMENTAL_EARTH, SchoolRegistry.NATURE.get());
        schoolToSchoolMap.put(SpellSchools.ELEMENTAL_FIRE, SchoolRegistry.FIRE.get());
        schoolToSchoolMap.put(SpellSchools.ELEMENTAL_WATER, SchoolRegistry.ICE.get());
        schoolToSchoolMap.put(SpellSchools.ELEMENTAL_AIR, SchoolRegistry.LIGHTNING.get());
        schoolToSchoolMap.put(SpellSchools.CONJURATION, SchoolRegistry.EVOCATION.get());

        schoolAttributes.put(SchoolRegistry.NATURE.get(), Pair.of(AttributeRegistry.NATURE_SPELL_POWER, AttributeRegistry.NATURE_MAGIC_RESIST));
        schoolAttributes.put(SchoolRegistry.FIRE.get(), Pair.of(AttributeRegistry.FIRE_SPELL_POWER, AttributeRegistry.FIRE_MAGIC_RESIST));
        schoolAttributes.put(SchoolRegistry.ICE.get(), Pair.of(AttributeRegistry.ICE_SPELL_POWER, AttributeRegistry.ICE_MAGIC_RESIST));
        schoolAttributes.put(SchoolRegistry.LIGHTNING.get(), Pair.of(AttributeRegistry.LIGHTNING_SPELL_POWER, AttributeRegistry.LIGHTNING_MAGIC_RESIST));

        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(WISP_PARTICLE.get(), true));
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(SNOWFLAKE_PARTICLE.get(), true));
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(ELECTRICITY_PARTICLE.get(), true));
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(FIRE_PARTICLE.get(), true));
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(FIREFLY_PARTICLE.get(), false));

    }

    @OnlyIn(Dist.CLIENT)
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(WISP_PARTICLE.get(), (sprites -> new WrappedProvider(ParticleRegistry.WISP_PARTICLE.get(), WispParticle.Provider::new)));
        event.registerSpriteSet(SNOWFLAKE_PARTICLE.get(), (sprites -> new WrappedProvider(ParticleRegistry.SNOWFLAKE_PARTICLE.get(), SnowflakeParticle.Provider::new)));
        event.registerSpriteSet(ELECTRICITY_PARTICLE.get(), (sprites -> new WrappedProvider(ParticleRegistry.ELECTRICITY_PARTICLE.get(), ElectricityParticle.Provider::new)));
        event.registerSpriteSet(FIRE_PARTICLE.get(), (sprites -> new WrappedProvider(ParticleRegistry.FIRE_PARTICLE.get(), FireParticle.Provider::new)));
        event.registerSpriteSet(FIREFLY_PARTICLE.get(), (sprites -> new WrappedProvider(ParticleRegistry.FIREFLY_PARTICLE.get(), FireflyParticle.Provider::new)));
    }

    public static void damageTweaksArs(Pre event) {
        if (!(event.target instanceof LivingEntity living) || event.context.getCurrentIndex() <= 0) return;
        AbstractSpellPart part = event.context.getSpell().get(event.context.getCurrentIndex() - 1);
        var schools = part.spellSchools;
        for (SpellSchool school : schools) {
            if (schoolToSchoolMap.get(school) != null) {
                SchoolType type = schoolToSchoolMap.get(school);
                var damageBuff = type.getPowerFor(event.caster);
                var damageRes = type.getResistanceFor(living);
                event.damage = (float) (event.damage * (1 + damageBuff - damageRes));
            }
        }
    }

    public static void damageTweaksEISS(ItemAttributeModifierEvent event) {
        if (event.getItemStack().getItem() instanceof AnimatedMagicArmor && ModList.get().isLoaded("ars_elemental")) {
            ElementalModule.addArmorModifiers(event);
        }
    }

    public static void initDocs() {

    }

    public static DeferredHolder<ParticleType<?>, PropertyParticleType> WISP_PARTICLE,
            SNOWFLAKE_PARTICLE, ELECTRICITY_PARTICLE,
            FIRE_PARTICLE, FIREFLY_PARTICLE;


}
