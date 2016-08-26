package alpvax.characteroverhaul.api.modifier.player;

import alpvax.characteroverhaul.api.CharacterOverhaulReference;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifierHandler;
import alpvax.characteroverhaul.api.character.modifier.RegistryCharModHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;

public class PlayerRaceHandler extends RegistryCharModHandler<PlayerRace>
{
	public PlayerRaceHandler(ICharacter attached)
	{
		super(attached);
	}

	@Override
	public int compareTo(ICharacterModifierHandler<?> arg0)
	{
		return -1;//Should always be first in the list
	}

	@Override
	public ResourceLocation getKey()
	{
		return CharacterOverhaulReference.MODIFIER_RACE_KEY;
	}

	@Override
	protected FMLControlledNamespacedRegistry<PlayerRace> getRegistry()//Override to save trying to find and cast
	{
		return PlayerRace.REGISTRY;
	}

	@Override
	public boolean setModifier(PlayerRace modifier)
	{
		if(modifier == PlayerRace.STEVE)
		{
			return false;
		}
		return super.setModifier(modifier);
	}
}
