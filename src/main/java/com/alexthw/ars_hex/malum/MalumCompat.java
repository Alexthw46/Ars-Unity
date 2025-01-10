package com.alexthw.ars_hex.malum;

import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.common.light.LightManager;
import com.hollingsworth.arsnouveau.setup.registry.EnchantmentRegistry;
import com.sammy.malum.common.entity.scythe.ScytheBoomerangEntity;
import com.sammy.malum.registry.common.entity.EntityRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.alexthw.ars_hex.registry.ModRegistry.ITEMS;
import static com.hollingsworth.arsnouveau.common.event.ReactiveEvents.castSpell;

public class MalumCompat {

    public static void postInit() {
        LightManager.register(EntityRegistry.NATURAL_SPIRIT.get(), (p) -> 8);

        LightManager.register(EntityRegistry.ETHERIC_NITRATE.get(), (p) -> 15);
        LightManager.register(EntityRegistry.VIVID_NITRATE.get(), (p) -> 15);
    }

    public static void init() {
        NeoForge.EVENT_BUS.register(MalumCompat.class);

        ENCHANTER_SCYTHE = ITEMS.register("enchanter_scythe", () -> new EnchanterScythe(Tiers.NETHERITE, -4F, 0F, 3.0F, new Item.Properties()));

    }

    public static DeferredHolder<Item, ? extends Item> ENCHANTER_SCYTHE;


    @SubscribeEvent
    public static void workaround(LivingDamageEvent.Post event) {
        if (event.getSource().getDirectEntity() instanceof ScytheBoomerangEntity scytheBoomerang)
            if (scytheBoomerang.getItem().getItem() instanceof ICasterTool && scytheBoomerang.getOwner() instanceof LivingEntity owner) {
                scytheBoomerang.getItem().getItem().hurtEnemy(scytheBoomerang.getItem(), event.getEntity(), owner);
            } else if (event.getSource().getEntity() instanceof LivingEntity living && living.level().holder(EnchantmentRegistry.REACTIVE_ENCHANTMENT).isPresent() && scytheBoomerang.getItem().getEnchantmentLevel(living.level().holderOrThrow(EnchantmentRegistry.REACTIVE_ENCHANTMENT)) > 0) {
                castSpell(living, scytheBoomerang.getItem());
            }
    }
}
