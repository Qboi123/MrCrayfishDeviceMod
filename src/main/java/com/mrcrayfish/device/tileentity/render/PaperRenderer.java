package com.mrcrayfish.device.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mrcrayfish.device.DeviceConfig;
import com.mrcrayfish.device.api.print.IPrint;
import com.mrcrayfish.device.api.print.PrintingManager;
import com.mrcrayfish.device.block.PaperBlock;
import com.mrcrayfish.device.init.DeviceBlocks;
import com.mrcrayfish.device.tileentity.PaperTileEntity;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.common.util.Constants;
import org.lwjgl.opengl.GL11;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * @author MrCrayfish
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PaperRenderer extends TileEntityRenderer<PaperTileEntity> {
    public PaperRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @SuppressWarnings("UnusedLabel")
    @Override
    public void render(PaperTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        mainMatrix: {
            // Translate to the tile entity's position. Then translate forwards a half by all 3 coordinates.
            matrixStackIn.translate(tileEntityIn.getPos().getX(), tileEntityIn.getPos().getY(), tileEntityIn.getPos().getZ());
            matrixStackIn.translate(0.5f, 0.5f, 0.5f);

            // Get block state of the paper tile entity.
            BlockState state = Objects.requireNonNull(tileEntityIn.getWorld()).getBlockState(tileEntityIn.getPos());

            // Check if the block of the tile entity is the paper block.
            if (state.getBlock() != DeviceBlocks.PAPER.get()) {
                // The block isn't a paper block, because of that we will return and don't go any further.
                break mainMatrix;
            }

            // Rotate from the tile entity's rotation.
            matrixStackIn.rotate(new Quaternion(state.get(PaperBlock.HORIZONTAL_FACING).getHorizontalIndex() * -90F + 180F, 0, 1, 0));
            matrixStackIn.rotate(new Quaternion(-tileEntityIn.getRotation(), 0f, 0f, 1f));

            // Translate backwards a half by all 3 coordinates.
            matrixStackIn.translate(-0.5f, -0.5f, -0.5f);

            // Get the print instance from the paper tile entity.
            IPrint print = tileEntityIn.getPrint();

            // Check if the print isn't null.
            if (print != null) {
                // Get the nbt tag from the print.
                CompoundNBT data = print.toTag();

                // Check if containing pixels.
                if (data.contains("pixels", Constants.NBT.TAG_INT_ARRAY) && data.hasKey("resolution", Constants.NBT.TAG_INT)) {
                    // Bind texture to the paper model texture.
                    Minecraft.getInstance().getTextureManager().bindTexture(PrinterRenderer.ModelPaper.TEXTURE);

                    // Check if the print was printed in 3D.
                    if (DeviceConfig.isRenderPrinted3D() && !data.getBoolean("cut")) {
                        // Draw a cuboid.
                        drawCuboid(0, 0, 0, 16, 16, 1);
                    }

                    // ???
                    matrixStackIn.translate(0f, 0f, DeviceConfig.isRenderPrinted3D() ? 0.0625f : 0.001f);

                    // Push matrix stack.
                    matrixStackIn.push();
                    printRender: {
                        IPrint.Renderer renderer = PrintingManager.getRenderer(print);
                        renderer.render(data);
                    }

                    GlStateManager.popMatrix();

                    matrixStackIn.push();
                    printRender: {
                        if (!DeviceConfig.isRenderPrinted3D() || !data.getBoolean("cut")) {
                            break printRender;
                        }
                        CompoundNBT tag = print.toTag();
                        drawPixels(tag.getIntArray("pixels"), tag.getInteger("resolution"), tag.getBoolean("cut"));
                    }
                    matrixStackIn.pop();
                }
            }
        }
        matrixStackIn.pop();
    }

    @Override
    public void render(PaperTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
    }

    private static void drawCuboid(double x, double y, double z, double width, double height, double depth)
    {
        x /= 16;
        y /= 16;
        z /= 16;
        width /= 16;
        height /= 16;
        depth /= 16;
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        drawQuad(x + (1 - width), y, z, x + width + (1 - width), y + height, z, Direction.NORTH);
        drawQuad(x + 1, y, z, x + 1, y + height, z + depth, Direction.EAST);
        drawQuad(x + width + 1 - (width + width), y, z + depth, x + width + 1 - (width + width), y + height, z, Direction.WEST);
        drawQuad(x + (1 - width), y, z + depth, x + width + (1 - width), y, z, Direction.DOWN);
        drawQuad(x + (1 - width), y + height, z, x + width + (1 - width), y, z + depth, Direction.UP);
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
    }

    private static void drawQuad(double xFrom, double yFrom, double zFrom, double xTo, double yTo, double zTo, Direction facing)
    {
        double textureWidth = Math.abs(xTo - xFrom);
        double textureHeight = Math.abs(yTo - yFrom);
        double textureDepth = Math.abs(zTo - zFrom);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        switch(facing.getAxis())
        {
            case X:
                buffer.pos(xFrom, yFrom, zFrom).tex(1 - xFrom + textureDepth, 1 - yFrom + textureHeight).endVertex();
                buffer.pos(xFrom, yTo, zFrom).tex(1 - xFrom + textureDepth, 1 - yFrom).endVertex();
                buffer.pos(xTo, yTo, zTo).tex(1 - xFrom, 1 - yFrom).endVertex();
                buffer.pos(xTo, yFrom, zTo).tex(1 - xFrom, 1 - yFrom + textureHeight).endVertex();
                break;
            case Y:
                buffer.pos(xFrom, yFrom, zFrom).tex(1 - xFrom + textureWidth, 1 - yFrom + textureDepth).endVertex();
                buffer.pos(xFrom, yFrom, zTo).tex(1 - xFrom + textureWidth, 1 - yFrom).endVertex();
                buffer.pos(xTo, yFrom, zTo).tex(1 - xFrom, 1 - yFrom).endVertex();
                buffer.pos(xTo, yFrom, zFrom).tex(1 - xFrom, 1 - yFrom + textureDepth).endVertex();
                break;
            case Z:
                buffer.pos(xFrom, yFrom, zFrom).tex(1 - xFrom + textureWidth, 1 - yFrom + textureHeight).endVertex();
                buffer.pos(xFrom, yTo, zFrom).tex(1 - xFrom + textureWidth, 1 - yFrom).endVertex();
                buffer.pos(xTo, yTo, zTo).tex(1 - xFrom, 1 - yFrom).endVertex();
                buffer.pos(xTo, yFrom, zTo).tex(1 - xFrom, 1 - yFrom + textureHeight).endVertex();
                break;
        }
        tessellator.draw();
    }

    private static void drawPixels(int[] pixels, int resolution, boolean cut)
    {
        double scale = 16 / (double) resolution;
        for(int i = 0; i < resolution; i++)
        {
            for(int j = 0; j < resolution; j++)
            {
                float a = (float) Math.floor((pixels[j + i * resolution] >> 24 & 255) / 255.0F);
                if(a < 1.0F)
                {
                    if(cut) continue;
                    GlStateManager.color(1.0F, 1.0F, 1.0F);
                }
                else
                {
                    float r = (float) (pixels[j + i * resolution] >> 16 & 255) / 255.0F;
                    float g = (float) (pixels[j + i * resolution] >> 8 & 255) / 255.0F;
                    float b = (float) (pixels[j + i * resolution] & 255) / 255.0F;
                    GlStateManager.color(r, g, b, a);
                }
                drawCuboid(j * scale - (resolution - 1) * scale, -i * scale + (resolution - 1) * scale, -1, scale, scale, 1);
            }
        }
    }
}
