package com.alexthw.ars_hex.hexerei.broom;

import com.alexthw.ars_hex.hexerei.HexereiModels;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.setup.config.Config;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import net.joefoxe.hexerei.client.renderer.entity.ModEntityTypes;
import net.joefoxe.hexerei.client.renderer.entity.custom.BroomEntity;
import net.joefoxe.hexerei.client.renderer.entity.model.BroomStickBaseModel;
import net.joefoxe.hexerei.item.ModItems;
import net.joefoxe.hexerei.item.custom.BroomItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.alexthw.ars_hex.ArsHex.prefix;


public class ArchwoodBroomStick extends BroomItem implements ICasterTool {
    String woodType;

    public ArchwoodBroomStick(String woodType, Properties properties) {
        super(woodType, properties);
        this.woodType = woodType;
    }

    @Override
    public BroomEntity getBroom(Level world, ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        EnchanterBroomEntity broom = new EnchanterBroomEntity(ModEntityTypes.BROOM.get(), world);
        if (tag.contains("floatMode")) {
            broom.itemHandler.deserializeNBT(world.registryAccess(), tag.getCompound("Inventory"));
        } else {
            broom.itemHandler.setStackInSlot(2, new ItemStack(ModItems.BROOM_BRUSH.get()));
        }

        Item var6 = stack.getItem();
        if (var6 instanceof ArchwoodBroomStick broomItem) {
            broom.setBroomType(broomItem.woodType);
        }

        broom.isItem = true;
        broom.selfItem = stack.copy();
        if (stack.get(DataComponents.CUSTOM_NAME) != null) {
            broom.setCustomName(stack.getHoverName());
        }

        if (stack.get(DataComponentRegistry.SPELL_CASTER) != null) {
            broom.setSpellCaster(stack.get(DataComponentRegistry.SPELL_CASTER));
        }
        return broom;
    }

    @OnlyIn(Dist.CLIENT)
    public void bakeModels() {
        EntityModelSet context = Minecraft.getInstance().getEntityModels();
        this.model = new HexereiModels.ArchwoodStick(context.bakeLayer(HexereiModels.ArchwoodStick.LAYER_LOCATION));
        this.outter_model = new BroomStickBaseModel(context.bakeLayer(BroomStickBaseModel.POWER_LAYER_LOCATION));
        this.texture = prefix("textures/entity/archwood_broom_stick.png");
        this.dye_texture = prefix("textures/entity/archwood_broom_stick.png");
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip2, @NotNull TooltipFlag flagIn) {
        if (Screen.hasShiftDown() || !Config.GLYPH_TOOLTIPS.get())
            getInformation(stack, context, tooltip2, flagIn);
        super.appendHoverText(stack, context, tooltip2, flagIn);
    }

}
