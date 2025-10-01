package com.alexthw.ars_hex;

import com.alexthw.ars_hex.glyphs.EffectSoulShatter;
import com.alexthw.ars_hex.malum.perks.MagicProficencyPerk;
import com.alexthw.ars_hex.malum.perks.SoulWardPerk;
import com.alexthw.ars_hex.malum.perks.SpiritSpoilsPerk;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
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

    public static void init() {
        registerCompatGlyphs();
        registerCompatPerks();
    }

    public static void registerCompatPerks() {
        if (ModList.get().isLoaded("malum")) {
            PerkRegistry.registerPerk(SoulWardPerk.INSTANCE);
            PerkRegistry.registerPerk(MagicProficencyPerk.INSTANCE);
            PerkRegistry.registerPerk(SpiritSpoilsPerk.INSTANCE);
        }
        //placeholders below
        if (ModList.get().isLoaded("hexerei")) {
            //PerkRegistry.registerPerk(HexereiCompat.HEXEREI_PERK);
        }
        if (ModList.get().isLoaded("irons_spellbooks")) {
            //PerkRegistry.registerPerk(ISSCompat.ISS_PERK);
        }
    }

}
