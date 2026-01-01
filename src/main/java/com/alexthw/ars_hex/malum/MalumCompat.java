package com.alexthw.ars_hex.malum;

import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.common.light.LightManager;
import com.hollingsworth.arsnouveau.setup.registry.EnchantmentRegistry;

import com.sammy.malum.common.entity.scythe.ScytheBoomerang;
import com.sammy.malum.registry.common.entity.MalumEntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import team.lodestar.lodestone.systems.item.LodestoneItemProperties;

import static com.alexthw.ars_hex.registry.ModRegistry.ITEMS;
import static com.hollingsworth.arsnouveau.common.event.ReactiveEvents.castSpell;

public class MalumCompat {

    public static void postInit() {
        LightManager.register(MalumEntityTypes.NATURAL_SPIRIT.get(), (p) -> 8);

        LightManager.register(MalumEntityTypes.ETHERIC_NITRATE.get(), (p) -> 15);
        LightManager.register(MalumEntityTypes.VIVID_NITRATE.get(), (p) -> 15);
    }

    public static void init() {
        NeoForge.EVENT_BUS.register(MalumCompat.class);

        ENCHANTER_SCYTHE = ITEMS.register("enchanter_scythe", () -> new EnchanterScythe(Tiers.NETHERITE, -4F, 0F, 3.0F, new LodestoneItemProperties().stacksTo(1)));

    }

    public static DeferredHolder<Item, ? extends Item> ENCHANTER_SCYTHE;


    @SubscribeEvent
    public static void workaround(LivingDamageEvent.Post event) {
        if (event.getSource().getDirectEntity() instanceof ScytheBoomerang scytheBoomerang)
            if (scytheBoomerang.getItem().getItem() instanceof ICasterTool && scytheBoomerang.getOwner() instanceof LivingEntity owner) {
                scytheBoomerang.getItem().getItem().hurtEnemy(scytheBoomerang.getItem(), event.getEntity(), owner);
            } else if (event.getSource().getEntity() instanceof LivingEntity living && living.level().holder(EnchantmentRegistry.REACTIVE_ENCHANTMENT).isPresent() && scytheBoomerang.getItem().getEnchantmentLevel(living.level().holderOrThrow(EnchantmentRegistry.REACTIVE_ENCHANTMENT)) > 0) {
                castSpell(living, scytheBoomerang.getItem());
            }
    }

    public static void initDocs() {

    }
}
