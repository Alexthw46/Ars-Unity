package com.alexthw.ars_hex.datagen;

import com.alexthw.ars_hex.ArsHex;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
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
        add("item.ars_hex.magebloom_brush", "Magebloom Broom Brush");
        add("item.ars_hex.wet_magebloom_brush", "Wet Magebloom Broom Brush");
        add("tooltip.ars_hex.magebloom_brush", "Might recharge itself with the mana of the rider");

        for (Supplier<Glyph> supplier : GlyphRegistry.getGlyphItemMap().values()) {
            Glyph glyph = supplier.get();
            AbstractSpellPart spellPart = glyph.spellPart;
            ResourceLocation registryName = glyph.spellPart.getRegistryName();
            if(registryName.getNamespace().equals(ArsHex.MODID)) {
                add("ars_hex.glyph_desc." + registryName.getPath(), spellPart.getBookDescription());
                add("ars_hex.glyph_name." + registryName.getPath(), spellPart.getName());

                Map<AbstractAugment, String> augmentDescriptions = new HashMap<>();
                spellPart.addAugmentDescriptions(augmentDescriptions);

                for(AbstractAugment augment : augmentDescriptions.keySet()){
                    add("ars_nouveau.augment_desc." + registryName.getPath() + "_" + augment.getRegistryName().getPath(), augmentDescriptions.get(augment));
                }
            }
        }
    }

}
