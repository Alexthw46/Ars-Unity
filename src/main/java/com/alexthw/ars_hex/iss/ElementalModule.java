package com.alexthw.ars_hex.iss;

import alexthw.ars_elemental.common.items.armor.ElementalArmor;
import com.alexthw.ars_hex.ArsHex;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import static com.alexthw.ars_hex.iss.ISSCompat.schoolAttributes;
import static com.alexthw.ars_hex.iss.ISSCompat.schoolToSchoolMap;

public class ElementalModule {
    public static void addArmorModifiers(ItemAttributeModifierEvent event) {
        if (event.getItemStack().getItem() instanceof ElementalArmor armor) {
            SchoolType type = schoolToSchoolMap.get(armor.getSchool());
            if (type == null || !schoolAttributes.containsKey(type)) return;
            var attribute = schoolAttributes.get(type).getFirst();
            event.addModifier(attribute, new AttributeModifier(ArsHex.prefix("iss_compat_elemental_" + armor.getType().getName()), 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.bySlot(armor.getType().getSlot()));
        }
    }
}
