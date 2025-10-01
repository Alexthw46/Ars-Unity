package com.alexthw.ars_hex.malum.perks;

import com.hollingsworth.arsnouveau.api.perk.Perk;
import com.sammy.malum.registry.common.MalumAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

import static com.alexthw.ars_hex.ArsHex.prefix;

public class SoulWardPerk extends Perk {

    public static final SoulWardPerk INSTANCE = new SoulWardPerk(prefix("thread_soul_ward"));

    public SoulWardPerk(ResourceLocation key) {
        super(key);
    }

    @Override
    public @NotNull ItemAttributeModifiers applyAttributeModifiers(ItemAttributeModifiers modifiers, ItemStack stack, int slotValue, EquipmentSlotGroup equipmentSlotGroup) {
        return modifiers
                .withModifierAdded(MalumAttributes.SOUL_WARD_CAPACITY, new AttributeModifier(INSTANCE.getRegistryName(), 2 * slotValue, AttributeModifier.Operation.ADD_VALUE), equipmentSlotGroup)
                .withModifierAdded(MalumAttributes.SOUL_WARD_INTEGRITY, new AttributeModifier(INSTANCE.getRegistryName(), 0.5 * slotValue, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), equipmentSlotGroup);
    }

    @Override
    public String getLangName() {
        return "Soul Ward";
    }

    @Override
    public String getLangDescription() {
        return "Increases the Soul Ward capacity and integrity.";
    }

}
