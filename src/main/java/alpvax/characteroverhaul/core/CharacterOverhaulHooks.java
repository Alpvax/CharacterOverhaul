package alpvax.characteroverhaul.core;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import alpvax.characteroverhaul.api.Reference;
import alpvax.characteroverhaul.api.character.CharacterBase;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.capabilities.CapabilityCharacterHandler;
import alpvax.characteroverhaul.capabilities.SerializeableCapabilityProvider;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent;
import net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CharacterOverhaulHooks
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void attachCapabilities(AttachCapabilitiesEvent.Entity event)
	{
		if(!hasCapability(event, CapabilityCharacterHandler.CHARACTER_CAPABILITY, null))
		{
			event.addCapability(Reference.CAPABILITY_CHARACTER_KEY, new SerializeableCapabilityProvider<ICharacter>(new CharacterBase(event.getEntity()), CapabilityCharacterHandler.CHARACTER_CAPABILITY));
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
	public void onRespawn(PlayerEvent.Clone event)
	{
		ICharacter oldCharacter = event.getOriginal().getCapability(CapabilityCharacterHandler.CHARACTER_CAPABILITY, null);
		ICharacter newCharacter = event.getEntityPlayer().getCapability(CapabilityCharacterHandler.CHARACTER_CAPABILITY, null);
		oldCharacter.cloneTo(newCharacter);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDrawPotions(PotionShiftEvent event)
	{
		//TODO:if potions display disabled
		event.setCanceled(true);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawInventory(DrawScreenEvent.Pre event)
	{
		if(event.getGui() instanceof InventoryEffectRenderer)
		{
			InventoryEffectRenderer gui = (InventoryEffectRenderer)event.getGui();
			//TODO:if potions display disabled
			ObfuscationReflectionHelper.setPrivateValue(InventoryEffectRenderer.class, gui, false, "hasActivePotionEffects");//TODO:Obfuscated names
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClickPotions(MouseInputEvent.Pre event)
	{
		if(event.getGui() instanceof InventoryEffectRenderer)
		{
			InventoryEffectRenderer gui = (InventoryEffectRenderer)event.getGui();
			//TODO:if potions display not disabled

			int i = Mouse.getEventX() * gui.width / gui.mc.displayWidth;
			int j = gui.height - Mouse.getEventY() * gui.height / gui.mc.displayHeight - 1;
			EntityPlayerSP player = gui.mc.thePlayer;
			if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && i < gui.guiLeft && j > gui.guiTop && j < gui.guiTop + gui.ySize)
			{
				player.openGui(CharacterOverhaul.instance, Reference.GUI_EFFECTS, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
			}
		}
	}
}
