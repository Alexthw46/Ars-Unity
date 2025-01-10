package com.alexthw.ars_hex;

import com.alexthw.ars_hex.glyphs.EffectSoulShatter;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class ArsNouveauRegistry {

    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>(); //this will come handy for datagen

    private static void registerMalumGlyphs() {
        register(EffectSoulShatter.INSTANCE);
    }

    public static void registerCompatGlyphs() {
        if (ModList.get().isLoaded("malum")) {
            registerMalumGlyphs();
        }
    }

    //    public static void registerSounds(){
//        SpellSoundRegistry.registerSpellSound(ModRegistry.EXAMPLE_SPELL_SOUND);
//    }

    public static void register(AbstractSpellPart spellPart) {
        GlyphRegistry.registerSpell(spellPart);
        registeredSpells.add(spellPart);
    }
}
