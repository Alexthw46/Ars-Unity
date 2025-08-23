package com.alexthw.ars_hex;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class HexConfigs {

    public static class Common {

        public final ModConfigSpec.ConfigValue<Boolean> IronsDamageBonusMerge;
        public final ModConfigSpec.ConfigValue<Double> IronsDamageBonusScaling;
        public final ModConfigSpec.ConfigValue<Double> IronsSchoolDamageBonusScaling;

        public Common(ModConfigSpec.Builder builder) {

            IronsDamageBonusMerge = builder
                    .comment("If true, the general damage bonuses from Irons Spellbooks will be added to Ars Nouveau spells.")
                    .define("IronsDamageBonusMerge", false);

            IronsDamageBonusScaling = builder
                    .comment("Scaling factor for damage bonuses from Irons Spellbooks when applied to Ars Nouveau spells.")
                    .defineInRange("IronsDamageBonusScaling", 0, 0.0, 100.0);

            IronsSchoolDamageBonusScaling = builder
                    .comment("Scaling factor for school-specific damage bonuses from Irons Spellbooks when applied to Ars Nouveau spells.")
                    .defineInRange("IronsDamageBonusScaling", 1, 0.0, 100.0);
        }

    }

    public static final Common COMMON;
    public static final ModConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading configEvent) {

    }

}
