package com.alexthw.ars_hex.malum.perks;

import com.hollingsworth.arsnouveau.api.perk.Perk;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.registry.common.LodestoneAttributes;

import static com.alexthw.ars_hex.ArsHex.prefix;

public class MagicProficencyPerk extends Perk {

    public static final MagicProficencyPerk INSTANCE = new MagicProficencyPerk(prefix("thread_magic_proficiency"));

    public MagicProficencyPerk(ResourceLocation key) {
        super(key);
    }

    @Override
    public @NotNull ItemAttributeModifiers applyAttributeModifiers(ItemAttributeModifiers modifiers, ItemStack stack, int slotValue, EquipmentSlotGroup equipmentSlotGroup) {
        return modifiers.withModifierAdded(LodestoneAttributes.MAGIC_PROFICIENCY, new AttributeModifier(INSTANCE.getRegistryName(), 0.1 * slotValue, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), equipmentSlotGroup);
    }

    @Override
    public String getLangName() {
        return "Magic Proficiency";
    }

    public String getLangDescription() {
        return "Increases your magic proficiency, boosting the magic damage of many spellcasting types.";
    }

}
