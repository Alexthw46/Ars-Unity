package com.alexthw.ars_hex.common;

import com.alexthw.ars_hex.ArsHex;
import com.alexthw.ars_hex.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.util.MathUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.Nullable;

public class RitualMoonPhase extends AbstractRitual {

    public static final ResourceLocation ID = ArsHex.prefix("ritual_lunar_phase");

    @Override
    public boolean canStart(@Nullable Player player) {
        return !getConsumedItems().isEmpty();
    }

    @Override
    @SuppressWarnings("removal")
    protected void tick() {
        ParticleUtil.spawnRitualSkyEffect(this, tile, rand, new ParticleColor.IntWrapper(50 + rand.nextInt(50), 50 + rand.nextInt(50), 200 + rand.nextInt(55)));
        if (getWorld() instanceof ServerLevel world) {
            MoonPhases.MoonCondition desiredPhase = MoonPhases.MoonCondition.NEW_MOON;
            if (!getConsumedItems().isEmpty()) {
                ItemStack dial = getConsumedItems().getFirst();
                desiredPhase = dial.getOrDefault(ModRegistry.MOON_PHASE.get(), MoonPhases.MoonCondition.NEW_MOON);
            }

            // Time is global and only changes when set in the Overworld Dimension.
            world = world.getServer().overworld();
            // credits to Elucent for this trick
            long timeOfDay = world.getDayTime() % 24000L;
            int desiredMoonPhase = getMoonPhaseIndex(desiredPhase);
            if (timeOfDay < 13000L || world.getMoonPhase() != desiredMoonPhase) {
                world.setDayTime(world.getDayTime() + 300);
                for (ServerPlayer player : world.players()) {
                    player.connection.send(new ClientboundSetTimePacket(world.getGameTime(), world.getDayTime(), world.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)));
                }
            } else {
                //speed up since the target is reached
                incrementProgress();
            }
            if (world.getGameTime() % 20 == 0) {
                incrementProgress();
                if (getProgress() >= 38) {
                    if (timeOfDay < 13000L || world.getMoonPhase() != desiredMoonPhase) {
                        world.setDayTime(getNextNightWithMoonPhase(world, desiredPhase));
                        for (ServerPlayer player : world.players()) {
                            player.connection.send(new ClientboundSetTimePacket(world.getGameTime(), world.getDayTime(), world.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)));
                        }
                    }
                    setFinished();
                }
            }
        }

    }

    @Override
    public String getLangName() {
        return "Lunar Cycle";
    }

    @Override
    public String getLangDescription() {
        return "Sets the time to a night with the specified lunar phase. Use a Moon Dial as augment to set the desired phase of the moon.";
    }

    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        return stack.getItem() == ModRegistry.MOON_DIAL.get() && getConsumedItems().isEmpty();
    }

    @Override
    public ParticleColor getCenterColor() {
        return new ParticleColor(
                rand.nextInt(25),
                rand.nextInt(25),
                rand.nextInt(255));
    }

    private long getNextNightWithMoonPhase(ServerLevel world, MoonPhases.MoonCondition desiredPhase) {
        int targetPhase = getMoonPhaseIndex(desiredPhase);
        long targetDay = Math.floorDiv(world.getDayTime(), 24000L);

        while (getMoonPhaseIndex(targetDay) != targetPhase) {
            targetDay++;
        }

        return targetDay * 24000L + MathUtil.NIGHT_TIME;
    }

    private int getMoonPhaseIndex(long day) {
        return (int) ((day % 8L + 8L) % 8L);
    }

    private int getMoonPhaseIndex(MoonPhases.MoonCondition phase) {
        return switch (phase) {
            case FULL_MOON -> 0;
            case WANING_GIBBOUS -> 1;
            case LAST_QUARTER -> 2;
            case WANING_CRESCENT -> 3;
            case NEW_MOON -> 4;
            case WAXING_CRESCENT -> 5;
            case FIRST_QUARTER -> 6;
            case WAXING_GIBBOUS, NONE -> 7;
        };
    }
}
