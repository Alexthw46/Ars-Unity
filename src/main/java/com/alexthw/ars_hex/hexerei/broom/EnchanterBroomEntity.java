package com.alexthw.ars_hex.hexerei.broom;

import com.alexthw.ars_hex.hexerei.HexereiCompat;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellCaster;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.IWrappedCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.PlayerCaster;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.common.datagen.BlockTagProvider;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.util.ANCodecs;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import net.joefoxe.hexerei.client.renderer.entity.custom.BroomEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class EnchanterBroomEntity extends BroomEntity {
    private @Nullable SpellCaster spellCaster;

    public EnchanterBroomEntity(EntityType<BroomEntity> broomEntityEntityType, Level world) {
        super(broomEntityEntityType, world);
    }

    public EnchanterBroomEntity(Level world, double x, double y, double z) {
        super(HexereiCompat.ARCHWOOD_BROOM_ENTITY.get(), world);
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.speedMultiplier = this.getBroomType().speedMultiplier();
    }

    @Override
    public @NotNull EntityType<?> getType() {
        return HexereiCompat.ARCHWOOD_BROOM_ENTITY.get();
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

    @Override
    public @NotNull ItemStack getPickResult() {
        ItemStack broom = super.getPickResult();
        // put the spellcaster in the itemstack
        broom.set(DataComponentRegistry.SPELL_CASTER, spellCaster);
        return broom;
    }

}
