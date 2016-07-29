package alpvax.characteroverhaul.client.gui;

import alpvax.characteroverhaul.api.CharacterOverhaulReference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

public class GuiCharacter extends GuiScreen
{
	/** The location of the inventory background texture */
	public static final ResourceLocation BACKGROUND = new ResourceLocation(CharacterOverhaulReference.MOD_ID, "textures/gui/character/inventory.png");
	private boolean buildTexture = false;

	@Override
	public void initGui()
	{
		buttonList.clear();
		buildTexture = mc.getTextureManager().getTexture(BACKGROUND) == TextureUtil.MISSING_TEXTURE;
		super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if(buildTexture)
		{

		}
		else
		{
			/*TODO:Missing texture
			this.mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
			int i = this.guiLeft;
			int j = this.guiTop;
			this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
			drawEntityOnScreen(i + 51, j + 75, 30, (float)(i + 51) - this.oldMouseX, (float)(j + 75 - 50) - this.oldMouseY, this.mc.thePlayer);
			 */
		}
	}

	protected void drawTab()
	{
		if(buildTexture)
		{

		}
		else
		{

		}
	}
}
