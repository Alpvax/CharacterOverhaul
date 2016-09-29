package alpvax.characteroverhaul.core;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import alpvax.characteroverhaul.api.CharacterOverhaulReference;
import alpvax.characteroverhaul.api.character.IAffected;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.config.Config;
import alpvax.characteroverhaul.api.effect.ICharacterEffect;
import alpvax.characteroverhaul.capabilities.AffectedCapabilityProvider;
import alpvax.characteroverhaul.capabilities.CharacterCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent;
import net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class CharacterOverhaulHooks
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void attachCapabilities(AttachCapabilitiesEvent.Entity event)
	{
		Entity e;
		if((e = event.getEntity()) instanceof EntityPlayer && !hasCapability(event, ICharacter.CAPABILITY, null))
		{
			event.addCapability(CharacterOverhaulReference.CAPABILITY_CHARACTER_KEY, new CharacterCapabilityProvider(e));
		}
		if(!hasCapability(event, IAffected.CAPABILITY, null))
		{
			event.addCapability(CharacterOverhaulReference.CAPABILITY_AFFECTED_KEY, new AffectedCapabilityProvider(e));
		}
	}

	private boolean hasCapability(AttachCapabilitiesEvent event, Capability<?> capability, EnumFacing facing)
	{
		List<ICapabilityProvider> list = new ArrayList<>(event.getCapabilities().values());
		list.add(0, (ICapabilityProvider)event.getObject());//Insert event object as first element.
		for(ICapabilityProvider p : list)
		{
			if(p.hasCapability(capability, facing))
			{
				return true;
			}
		}
		return false;
	}

	@SubscribeEvent
	/**
	 * Ticks all Entities and TileEntities, as well as any ItemStacks they contain (recursively)
	 */
	public void tickCharacters(WorldTickEvent event)
	{
		if(event.phase == Phase.START && !event.world.isRemote)
		{
			for(Entity e : event.world.loadedEntityList)
			{
				tickCharacterEffects(e);
			}
			for(TileEntity tile : event.world.loadedTileEntityList)
			{
				tickCharacterEffects(tile);
			}
		}
	}

	/**
	 * Recursively ticks all effects of this provider, and any items it contains
	 * @param provider
	 */
	private void tickCharacterEffects(ICapabilityProvider provider)
	{
		if(provider.hasCapability(ICharacter.CAPABILITY, null))
		{
			for(ICharacterEffect effect : provider.getCapability(ICharacter.CAPABILITY, null).getEffects())
			{
				effect.tick();
			}
		}
		if(provider.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
		{
			IItemHandler items = provider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			for(int i = 0; i < items.getSlots(); i++)
			{
				ItemStack stack = items.getStackInSlot(i);
				if(stack != null)
				{
					tickCharacterEffects(stack);
				}
			}
		}
	}

	@SubscribeEvent
	public void onRespawn(PlayerEvent.Clone event)
	{
		ICharacter oldCharacter = event.getOriginal().getCapability(ICharacter.CAPABILITY, null);
		ICharacter newCharacter = event.getEntityPlayer().getCapability(ICharacter.CAPABILITY, null);
		newCharacter.cloneFrom(oldCharacter);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDrawPotions(PotionShiftEvent event)
	{
		if(!Config.renderPotionsInInventory)
		{
			event.setCanceled(true);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent.Pre event)
	{
		if(event.getType() == ElementType.POTION_ICONS && !Config.renderPotionsOnHud)
		{
			event.setCanceled(true);
			for(ICharacterEffect effect : Minecraft.getMinecraft().thePlayer.getCapability(ICharacter.CAPABILITY, null).getEffects())
			{
				effect.renderOnHUD(event.getResolution());
			}
		}
		/*TODO:Render abilities
		if(event.getType() == ElementType.HOTBAR)
		{

		}*/
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawInventory(DrawScreenEvent.Pre event)
	{
		if(event.getGui() instanceof InventoryEffectRenderer && !Config.renderPotionsInInventory)
		{
			InventoryEffectRenderer gui = (InventoryEffectRenderer)event.getGui();
			ObfuscationReflectionHelper.setPrivateValue(InventoryEffectRenderer.class, gui, false, "hasActivePotionEffects");//TODO:Obfuscated names
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClickPotions(MouseInputEvent.Pre event)
	{
		if(event.getGui() instanceof InventoryEffectRenderer && Config.renderPotionsInInventory)
		{
			InventoryEffectRenderer gui = (InventoryEffectRenderer)event.getGui();

			int i = Mouse.getEventX() * gui.width / gui.mc.displayWidth;
			int j = gui.height - Mouse.getEventY() * gui.height / gui.mc.displayHeight - 1;
			EntityPlayerSP player = gui.mc.thePlayer;
			if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && i < gui.guiLeft && j > gui.guiTop && j < gui.guiTop + gui.ySize)
			{
				player.openGui(CharacterOverhaul.instance, CharacterOverhaulReference.GUI_EFFECTS, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
			}
		}
	}
}
