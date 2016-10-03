package alpvax.characteroverhaul.client.gui;

import alpvax.characteroverhaul.core.CharacterOverhaul;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCharTab extends Gui
{
	protected static final ResourceLocation TAB_TEXTURES = new ResourceLocation(CharacterOverhaul.MOD_ID, "textures/gui/tabs.png");

	/** The number of ticks required to completely open or close the tab */
	private static float SPEED = 10F;

	/** Tab width in pixels */
	private float width;
	/** Tab height in pixels */
	private int height;
	/** The x position of the right edge of the tab. */
	private float xPosition;
	/** The y position of this tab. */
	private int yPosition;

	private int textWidth = -1;

	/** The string displayed on this control. */
	private String displayString;
	private ResourceLocation icon;

	/** True if this tab is hovered over */
	protected boolean hovered = false;

	private boolean selected;

	public GuiCharTab(String key, int x, int y, int iconSize)
	{
		xPosition = x;
		yPosition = y;
		height = iconSize + 2;
		displayString = I18n.format("character." + key + ".tab");
		icon = new ResourceLocation(CharacterOverhaul.MOD_ID, "textures/gui/tab_" + key + ".png");
	}

	public boolean isSelected()
	{
		return selected;
	}

	protected float getWidth(float partialTicks)
	{
		float f = (textWidth) * (1F + partialTicks) / SPEED;
		return MathHelper.clamp_float(width + (hovered ? f : -f), height, height + textWidth);
	}

	public boolean isMouseOver(int mouseX, int mouseY)
	{
		return mouseX >= MathHelper.floor_float(xPosition - width) && mouseX < MathHelper.ceiling_float_int(xPosition)
				&& mouseY >= MathHelper.floor_float(yPosition) && mouseY < MathHelper.ceiling_float_int(yPosition + height);
	}

	public void drawTab(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		hovered = isMouseOver(mouseX, mouseY);
		FontRenderer fontrenderer = mc.fontRendererObj;
		if(textWidth < 0)
		{
			textWidth = fontrenderer.getStringWidth(displayString) + 2;
		}
		width = getWidth(partialTicks);
		mc.getTextureManager().bindTexture(TAB_TEXTURES);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		this.drawTexturedModalRect(xPosition, yPosition, 0F, isSelected() ? 0F : (float)height, width, height);

		mc.getTextureManager().bindTexture(icon);
		drawTexturedModalRect(xPosition - width + 1F, yPosition + 1F, 0, 0, height - 2, height - 2);
		int j = 0;//White
		drawCenteredString(fontrenderer, displayString, (int)(xPosition - textWidth / 2), yPosition + (height - 8) / 2, j);
	}

	/**
	 * Draws a textured rectangle using the texture currently bound to the TextureManager<br>
	 * handles float
	 */
	public void drawTexturedModalRect(float xCoord, float yCoord, float minU, float minV, float maxU, float maxV)
	{
		/*What are these for?
		float f = 0.00390625F;
		float f1 = 0.00390625F;*/
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos(xCoord + 0.0F, yCoord + maxV, zLevel).tex((minU + 0F) * 0.00390625F, (minV + maxV) * 0.00390625F).endVertex();
		vertexbuffer.pos(xCoord + maxU, yCoord + maxV, zLevel).tex((minU + maxU) * 0.00390625F, (minV + maxV) * 0.00390625F).endVertex();
		vertexbuffer.pos(xCoord + maxU, yCoord + 0.0F, zLevel).tex((minU + maxU) * 0.00390625F, (minV + 0F) * 0.00390625F).endVertex();
		vertexbuffer.pos(xCoord + 0.0F, yCoord + 0.0F, zLevel).tex((minU + 0F) * 0.00390625F, (minV + 0F) * 0.00390625F).endVertex();
		tessellator.draw();
	}
}
