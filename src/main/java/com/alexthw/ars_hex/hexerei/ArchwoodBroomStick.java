package com.alexthw.ars_hex.hexerei;

import net.joefoxe.hexerei.client.renderer.entity.model.BroomStickBaseModel;
import net.joefoxe.hexerei.item.custom.BroomItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static com.alexthw.ars_hex.ArsHex.prefix;


public class ArchwoodBroomStick extends BroomItem {
    public ArchwoodBroomStick(String woodType, Properties properties) {
        super(woodType, properties);
    }

    @OnlyIn(Dist.CLIENT)
    public void bakeModels() {
        EntityModelSet context = Minecraft.getInstance().getEntityModels();
        this.model = new HexereiModels.ArchwoodStick(context.bakeLayer(HexereiModels.ArchwoodStick.LAYER_LOCATION));
        this.outter_model = new BroomStickBaseModel(context.bakeLayer(BroomStickBaseModel.POWER_LAYER_LOCATION));
        this.texture = prefix("textures/entity/archwood_broom_stick.png");
        this.dye_texture = prefix("textures/entity/archwood_broom_stick.png");
    }

}
