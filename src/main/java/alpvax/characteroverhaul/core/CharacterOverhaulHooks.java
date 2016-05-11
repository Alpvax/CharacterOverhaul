package alpvax.characteroverhaul.core;

import static alpvax.characteroverhaul.api.Reference.CAPABILITY_CHARACTER_KEY;

import alpvax.characteroverhaul.capabilities.CapabilityCharacterHandler;
import alpvax.characteroverhaul.capabilities.SerializeableCapabilityProvider;
import alpvax.characteroverhaul.character.CharacterPlayer;
import alpvax.characteroverhaul.character.ICharacter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CharacterOverhaulHooks
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void attachCapabilities(AttachCapabilitiesEvent.Entity event)
	{
		if(!event.getEntity().hasCapability(CapabilityCharacterHandler.CHARACTER_CAPABILITY, null) && !event.getCapabilities().keySet().contains(CAPABILITY_CHARACTER_KEY))
		{
			event.addCapability(CAPABILITY_CHARACTER_KEY, new SerializeableCapabilityProvider<ICharacter>(new CharacterPlayer((EntityPlayer)event.getEntity()), CapabilityCharacterHandler.CHARACTER_CAPABILITY));
		}
	}
}
