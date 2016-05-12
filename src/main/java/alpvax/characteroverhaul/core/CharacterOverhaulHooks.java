package alpvax.characteroverhaul.core;

import static alpvax.characteroverhaul.api.Reference.CAPABILITY_CHARACTER_KEY;

import alpvax.characteroverhaul.api.character.CharacterBase;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.capabilities.CapabilityCharacterHandler;
import alpvax.characteroverhaul.capabilities.SerializeableCapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CharacterOverhaulHooks
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void attachCapabilities(AttachCapabilitiesEvent.Entity event)
	{
		if(!event.getEntity().hasCapability(CapabilityCharacterHandler.CHARACTER_CAPABILITY, null) && !event.getCapabilities().keySet().contains(CAPABILITY_CHARACTER_KEY))
		{
			event.addCapability(CAPABILITY_CHARACTER_KEY, new SerializeableCapabilityProvider<ICharacter>(new CharacterBase(event.getEntity()), CapabilityCharacterHandler.CHARACTER_CAPABILITY));
		}
	}

	@SubscribeEvent
	public void onRespawn(PlayerEvent.Clone event)
	{
		ICharacter oldCharacter = event.getOriginal().getCapability(CapabilityCharacterHandler.CHARACTER_CAPABILITY, null);
		ICharacter newCharacter = event.getEntityPlayer().getCapability(CapabilityCharacterHandler.CHARACTER_CAPABILITY, null);
		oldCharacter.cloneTo(newCharacter);
	}
}
