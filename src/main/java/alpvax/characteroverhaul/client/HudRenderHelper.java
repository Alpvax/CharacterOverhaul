package alpvax.characteroverhaul.client;

import javax.annotation.Nullable;

import alpvax.characteroverhaul.api.ability.Ability;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.core.CharacterOverhaulMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public enum HudRenderHelper
{
	INSTANCE(Minecraft.getMinecraft());

	public static final ResourceLocation ABILITY_SLOTS = new ResourceLocation(CharacterOverhaulMod.MOD_ID, "textures/gui/ability_hotbar.png");

	private final Minecraft mc;

	private HudRenderHelper(Minecraft mc)
	{
		this.mc = mc;
	}

	public void renderAbilities(ScaledResolution sr)
	{
		int height = GuiIngameForge.left_height = GuiIngameForge.right_height = Math.max(GuiIngameForge.left_height, GuiIngameForge.right_height) + 24;
		Ability[] hotbar = getCharacter().getHotbarAbilities();
		int num = hotbar.length;
		int pad = num < 9 ? (num > 1 ? (182 - (num * 22)) / (num - 1) : 0) : 2;//Hotbarwidth - abilities width, minimum 2px
		int startx = (sr.getScaledWidth() / 2 - (pad * (num - 1) + num * 22) / 2);
		int starty = sr.getScaledHeight() - height + 10;
		for(int i = 0; i < num; i++)
		{
			renderHotbarSlot(hotbar[i], startx + i * (pad + 22), starty);
		}
	}

	public void renderHotbarSlot(@Nullable Ability ability, int x, int y)
	{
		mc.getTextureManager().bindTexture(ABILITY_SLOTS);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 22, 22, 32F, 32F);//No texture scaling
		if(ability != null)
		{
			mc.getTextureManager().bindTexture(ability.getIconTexture());
			Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 16, 16, 16, 16);
			int cooldown = ability.getCooldown();
			if(cooldown > 0)
			{
				if(cooldown >= ability.getMaxCooldown())
				{
					drawRect(x + 3, y + 3, x + 19, y + 19, 0xC85E5E5E);//Fully greyed out
				}
				else
				{
					float f = 16 * (float)cooldown / ability.getMaxCooldown();
					drawRect(x + 3, y + 19 - f, x + 19, y + 19, 0xC85E5E5E);
				}
				float secs = (float)cooldown / 20;
				String text = secs > 1F ? Integer.toString(MathHelper.floor(secs)) : String.format("%.1f", secs);
				drawCenteredString(mc.fontRenderer, text, x + 11, y + 11, 0xC8D6D6D6, false, true, 1F);
			}
			drawCenteredString(mc.fontRenderer, ability.getDisplayName().getFormattedText(), x + 11, y + 23, 0xFFFFFF, true, false, 0.5F);
		}
	}

	/**
	 * Draws a solid color rectangle with the specified coordinates and color.
	 */
	public static void drawRect(float left, float top, float right, float bottom, int color)
	{
		if(left < right)
		{
			float i = left;
			left = right;
			right = i;
		}

		if(top < bottom)
		{
			float j = top;
			top = bottom;
			bottom = j;
		}

		float f3 = (color >> 24 & 255) / 255.0F;
		float f = (color >> 16 & 255) / 255.0F;
		float f1 = (color >> 8 & 255) / 255.0F;
		float f2 = (color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.color(f, f1, f2, f3);
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(left, bottom, 0.0D).endVertex();
		vertexbuffer.pos(right, bottom, 0.0D).endVertex();
		vertexbuffer.pos(right, top, 0.0D).endVertex();
		vertexbuffer.pos(left, top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public void drawCenteredString(FontRenderer fontRendererIn, String text, float x, float y, int colour, boolean dropShadow, boolean centerVertical, float scale)
	{
		float i = x;
		float j = centerVertical ? y - 3.5F : y;
		GlStateManager.pushMatrix();
		if(scale != 1.0F)
		{
			GlStateManager.scale(scale, scale, scale);
			i /= scale;
			j /= scale;
		}
		fontRendererIn.drawString(text, i - (float)fontRendererIn.getStringWidth(text) / 2, j, colour, dropShadow);
		GlStateManager.popMatrix();
	}

	private ICharacter getCharacter()
	{//TODO:Spectate
		return mc.player.getCapability(ICharacter.CAPABILITY, null);
	}

	@SubscribeEvent
	public static void onRenderOverlay(RenderGameOverlayEvent.Pre event)
	{
		/*TODO: render effects instead of potions
		if(event.getType() == ElementType.POTION_ICONS && !CharacterConfig.client.renderPotionsOnHud)
		{
			event.setCanceled(true);
			for(Effect effect : mc.player.getCapability(ICharacter.CAPABILITY, null).getEffects())
			{
				if(effect.shouldRenderOnHUD())
				{
					//TODO:renderOnHUD(event.getResolution());
					//mc.renderEngine.bindTexture(effect.getIconTexture());
				}
			}
		}*/
		if(event.getType() == ElementType.HOTBAR)
		{
			ScaledResolution scaledresolution = new ScaledResolution(INSTANCE.mc);
			INSTANCE.renderAbilities(scaledresolution);
		}
	}
}
