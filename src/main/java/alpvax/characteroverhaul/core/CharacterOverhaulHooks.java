package alpvax.characteroverhaul.core;

import java.util.ArrayList;
import java.util.List;

import alpvax.characteroverhaul.api.Reference;
import alpvax.characteroverhaul.api.character.CharacterBase;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.capabilities.CapabilityCharacterHandler;
import alpvax.characteroverhaul.capabilities.SerializeableCapabilityProvider;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
}
