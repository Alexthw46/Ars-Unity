package com.alexthw.ars_hex.datagen;

import com.alexthw.ars_hex.ArsHex;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.datagen.LangDatagen;
import com.hollingsworth.arsnouveau.common.items.Glyph;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AHLangDatagen extends LangDatagen {

    public AHLangDatagen(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        add("item.ars_hex.archwood_staff", "Archwood Staff");
        add("item.ars_hex.enchanter_scythe", "Enchanter's Scythe");
        add("item.ars_hex.archwood_broom", "Archwood Broom");
        add("entity.ars_hex.archwood_broom", "Enchanter's Broom");
        add("item.ars_hex.magebloom_brush", "Magebloom Broom Brush");
        add("item.ars_hex.wet_magebloom_brush", "Wet Magebloom Broom Brush");
        add("tooltip.ars_hex.magebloom_brush", "Might recharge itself with the mana of the rider");
        add("tooltip.hexerei.key_for_spell", "Press %s to cast the inscribed spell while riding it.");
        for (Supplier<Glyph> supplier : GlyphRegistry.getGlyphItemMap().values()) {
            Glyph glyph = supplier.get();
            AbstractSpellPart spellPart = glyph.spellPart;
            ResourceLocation registryName = glyph.spellPart.getRegistryName();
            if (registryName.getNamespace().equals(ArsHex.MODID)) {
                add("ars_hex.glyph_desc." + registryName.getPath(), spellPart.getBookDescription());
                add("ars_hex.glyph_name." + registryName.getPath(), spellPart.getName());

                Map<AbstractAugment, String> augmentDescriptions = new HashMap<>();
                spellPart.addAugmentDescriptions(augmentDescriptions);

                for (AbstractAugment augment : augmentDescriptions.keySet()) {
                    add("ars_nouveau.augment_desc." + registryName.getPath() + "_" + augment.getRegistryName().getPath(), augmentDescriptions.get(augment));
                }
            }
        }


        add("ars_hex.thread_of", "Thread of %s");
        add("ars_hex.tablet_of", "Tablet of %s");
        for (var sup : PerkRegistry.getPerkItemMap().values()) {
            var perk = sup.perk;
            ResourceLocation registryName = perk.getRegistryName();
            if (registryName.getNamespace().equals(ArsHex.MODID)) {
                add("ars_hex.perk_desc." + registryName.getPath(), perk.getLangDescription());
                add("ars_hex.perk_name." + registryName.getPath(), perk.getLangName());
                add("item.ars_hex." + registryName.getPath(), perk.getLangName());
            }
        }

        // Hexerei particles
        add("ars_hex.particle.broom_leaves", "Hexerei Broom Leaves");
        add("ars_hex.particle.broom_leaves_2", "Hexerei Broom Leaves 2");
        add("ars_hex.particle.broom_leaves_3", "Hexerei Broom Leaves 3");
        add("ars_hex.particle.moon_leaves", "Hexerei Moon Brush Leaves");
        add("ars_hex.particle.moon_leaves_2", "Hexerei Moon Brush Leaves 2");
        add("ars_hex.particle.moon_leaves_3", "Hexerei Moon Brush Leaves 3");
        add("ars_hex.particle.star_brush", "Hexerei Moon Brush Stars");
        add("ars_hex.particle.blood_spell", "Hexerei Blood");
        add("ars_hex.particle.fog_spell", "Hexerei Fog");
        add("ars_hex.particle.owl_teleport", "Hexerei Owl Teleport");
        add("ars_hex.particle.owl_teleport_barn", "Hexerei Owl Teleport Barn");
        add("ars_hex.particle.owl_teleport_snow", "Hexerei Owl Teleport Snow");


        // ISS particles
        add("ars_hex.particle.iss_wisp", "IronSS Wisp");
        add("ars_hex.particle.iss_snowflake", "IronSS Snowflake");
        add("ars_hex.particle.iss_electricity", "IronSS Electricity");
        add("ars_hex.particle.iss_fire", "IronSS Fire");
        add("ars_hex.particle.iss_firefly", "IronSS Firefly");

    }

}
