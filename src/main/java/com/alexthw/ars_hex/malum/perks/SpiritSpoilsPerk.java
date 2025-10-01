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

public class SpiritSpoilsPerk extends Perk {

    public static final SpiritSpoilsPerk INSTANCE = new SpiritSpoilsPerk(prefix("thread_soul_spoils"));

    public SpiritSpoilsPerk(ResourceLocation id) {
        super(id);
    }

    @Override
    public @NotNull ItemAttributeModifiers applyAttributeModifiers(ItemAttributeModifiers modifiers, ItemStack stack, int slotValue, EquipmentSlotGroup equipmentSlotGroup) {
        return modifiers.withModifierAdded(MalumAttributes.SPIRIT_SPOILS, new AttributeModifier(INSTANCE.getRegistryName(), slotValue, AttributeModifier.Operation.ADD_VALUE), equipmentSlotGroup);
    }

    @Override
    public String getLangName() {
        return "Spirit Spoils";
    }

    public String getLangDescription() {
        return "Increases the spirits released when killing enemies with the proper methods.";
    }

}
