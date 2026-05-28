package com.gugas749.abyssitems.items.weapons.marshknife;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MarshmelloKnifeRenderer extends GeoItemRenderer<MarshmelloKnifeItem> {
    public MarshmelloKnifeRenderer() {
        super(new MarshmelloKnifeModel());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack,
                             MultiBufferSource buffer, int light, int overlay) {
        poseStack.pushPose();

        switch (context) {
            case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND -> {
                poseStack.translate(-0.25, 1.25, -0.25);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                poseStack.scale(1.5f, 1.5f, 1.5f);
            }
            case FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND -> {
                poseStack.translate(0, 0.2, 0);
                poseStack.mulPose(Axis.XP.rotationDegrees(15));
                poseStack.scale(1f, 1f, 1f);
            }
            case GUI, FIXED -> {
                poseStack.mulPose(Axis.ZP.rotationDegrees(-45));
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
                poseStack.translate(0, 0, -0.57);
                poseStack.scale(1.5f, 1.5f, 1.5f);
            }
            case GROUND -> poseStack.scale(1f, 1f, 1f);
        }

        super.renderByItem(stack, context, poseStack, buffer, light, overlay);
        poseStack.popPose();
    }
}
