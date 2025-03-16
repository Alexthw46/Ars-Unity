package com.alexthw.ars_hex.iss;

import com.hollingsworth.arsnouveau.api.event.SpellDamageEvent.Pre;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.armor.AnimatedMagicArmor;
import com.mojang.datafixers.util.Pair;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import java.util.HashMap;
import java.util.Map;

public class ISSCompat {

    public static Map<SpellSchool, SchoolType> schoolToSchoolMap = new HashMap<>();

    public static Map<SchoolType, Pair<Holder<Attribute>, Holder<Attribute>>> schoolAttributes = new HashMap<>();

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
}
