package com.alexthw.ars_hex.hexcast;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.alexthw.ars_hex.registry.ModRegistry.ITEMS;

public class HexcastCompat {

    public static void init() {
        //NeoForge.EVENT_BUS.register(HexcastCompat.class);

        ARCHWOOD_STAFF = ITEMS.register("archwood_staff", () -> new Item(new Item.Properties()));
    }

    public static DeferredHolder<Item, ? extends Item> ARCHWOOD_STAFF;

}
