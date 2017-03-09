package alpvax.characteroverhaul.core;

import java.util.ArrayList;
import java.util.List;

import alpvax.characteroverhaul.api.CharacterOverhaulReference;
import alpvax.characteroverhaul.api.ability.Ability;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.effect.Effect;
import alpvax.characteroverhaul.capabilities.CharacterCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class CharacterOverhaulHooks
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		Entity e = event.getObject();
		if(e instanceof EntityPlayer && !hasCapability(event, ICharacter.CAPABILITY, null))
		{
			event.addCapability(CharacterOverhaulReference.CAPABILITY_CHARACTER_KEY, new CharacterCapabilityProvider(e));
		}
	}

	private boolean hasCapability(AttachCapabilitiesEvent<?> event, Capability<?> capability, EnumFacing facing)
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

	/**
	 * Ticks all Entities and TileEntities, as well as any ItemStacks they contain (recursively)
	 */
	@SubscribeEvent
	public void tickCharacters(WorldTickEvent event)
	{
		if(event.phase == Phase.START)
		{
			for(Entity e : event.world.loadedEntityList)
			{
				tickProvider(e, event.side);
			}
			for(TileEntity tile : event.world.loadedTileEntityList)
			{
				tickProvider(tile, event.side);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void tickPlayer(ClientTickEvent event)
	{
		EntityPlayer player;
		if(event.phase == Phase.START && (player = Minecraft.getMinecraft().player) != null)
		{
			tickProvider(player, event.side);
		}
	}

	/**
	 * Recursively ticks all effects and abilities of this provider, and any items it contains
	 * @param provider
	 * @param side
	 */
	private void tickProvider(ICapabilityProvider provider, Side side)
	{
		if(provider.hasCapability(ICharacter.CAPABILITY, null))
		{
			ICharacter character = provider.getCapability(ICharacter.CAPABILITY, null);
			for(Effect effect : character.getEffects())
			{
				if(effect.isTriggered())
				{
					effect.tickEffect();
				}
				if(side == Side.SERVER)
				{
					effect.tickTriggers();
				}
			}
			for(Ability ability : character.getAllAbilities())
			{
				ability.update();
			}
		}
		if(provider.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
		{
			IItemHandler items = provider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			for(int i = 0; i < items.getSlots(); i++)
			{
				ItemStack stack = items.getStackInSlot(i);
				if(!stack.isEmpty())
				{
					tickProvider(stack, side);
				}
			}
		}
	}

	/*@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDrawPotions(PotionShiftEvent event)
	{
		if(!CharacterConfig.client.renderPotionsInInventory)
		{
			event.setCanceled(true);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawInventory(DrawScreenEvent.Pre event)
	{
		if(event.getGui() instanceof InventoryEffectRenderer && !CharacterConfig.client.renderPotionsInInventory)
		{
			InventoryEffectRenderer gui = (InventoryEffectRenderer)event.getGui();
			ObfuscationReflectionHelper.setPrivateValue(InventoryEffectRenderer.class, gui, false, "hasActivePotionEffects");//TODO:Obfuscated names
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClickPotions(MouseInputEvent.Pre event)
	{
		if(event.getGui() instanceof InventoryEffectRenderer && CharacterConfig.client.renderPotionsInInventory)
		{
			InventoryEffectRenderer gui = (InventoryEffectRenderer)event.getGui();
	
			int i = Mouse.getEventX() * gui.width / gui.mc.displayWidth;
			int j = gui.height - Mouse.getEventY() * gui.height / gui.mc.displayHeight - 1;
			EntityPlayerSP player = gui.mc.player;
			if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && i < gui.guiLeft && j > gui.guiTop && j < gui.guiTop + gui.ySize)
			{
				EnumCharacterTab.EFFECTS.openGui(player);
			}
		}
	}*/
}
