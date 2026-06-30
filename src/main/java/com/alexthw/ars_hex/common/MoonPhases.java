package com.alexthw.ars_hex.common;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public class MoonPhases {
    public MoonPhases() {
    }

    public enum MoonCondition implements StringRepresentable {
        NONE("none"),
        NEW_MOON("new_moon"),
        WAXING_CRESCENT("waxing_crescent"),
        FIRST_QUARTER("first_quarter"),
        WAXING_GIBBOUS("waxing_gibbous"),
        FULL_MOON("full_moon"),
        WANING_GIBBOUS("waning_gibbous"),
        LAST_QUARTER("last_quarter"),
        WANING_CRESCENT("waning_crescent");

        private final String name;

        public static final Codec<MoonCondition> CODEC = StringRepresentable.fromEnum(MoonCondition::values);
        public static final StreamCodec<RegistryFriendlyByteBuf, MoonCondition> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, MoonCondition::getName, MoonCondition::getMoonCondition);


        MoonCondition(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static MoonCondition getMoonCondition(String str) {
            for (MoonCondition condition : values()) {
                if (condition.name.equals(str)) {
                    return condition;
                }
            }

            return NONE;
        }

        private static @NotNull MoonCondition getMoonCondition(int phase) {
            switch (phase) {
                case 0 -> {
                    return FULL_MOON;
                }
                case 1 -> {
                    return WANING_GIBBOUS;
                }
                case 2 -> {
                    return LAST_QUARTER;
                }
                case 3 -> {
                    return WANING_CRESCENT;
                }
                case 4 -> {
                    return NEW_MOON;
                }
                case 5 -> {
                    return WAXING_CRESCENT;
                }
                case 6 -> {
                    return FIRST_QUARTER;
                }
                default -> {
                    return WAXING_GIBBOUS;
                }
            }
        }

        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}
