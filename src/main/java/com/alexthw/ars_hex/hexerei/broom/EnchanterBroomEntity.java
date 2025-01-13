package com.alexthw.ars_hex.hexerei.broom;

import com.hollingsworth.arsnouveau.api.spell.SpellCaster;
import com.hollingsworth.arsnouveau.common.util.ANCodecs;
import net.joefoxe.hexerei.client.renderer.entity.custom.BroomEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class EnchanterBroomEntity extends BroomEntity {
    private @Nullable SpellCaster spellCaster;

    public EnchanterBroomEntity(Level worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EnchanterBroomEntity(EntityType entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public boolean save(CompoundTag compound) {
        if (spellCaster != null) {
            compound.put("spell_caster", ANCodecs.encode(SpellCaster.CODEC.codec(), spellCaster));
        }
        return super.save(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("spell_caster")) {
            spellCaster = ANCodecs.decode(SpellCaster.CODEC.codec(), compound.get("spell_caster"));
        }
    }

    public void setSpellCaster(@Nullable SpellCaster spellCaster) {
        this.spellCaster = spellCaster;
    }

    public @Nullable SpellCaster getSpellCaster() {
        return spellCaster;
    }

}
