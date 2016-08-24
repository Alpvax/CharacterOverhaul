package alpvax.characteroverhaul.api.modifier.player;

import java.util.List;

import alpvax.characteroverhaul.api.CharacterOverhaulReference;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifierHandler;
import alpvax.characteroverhaul.api.character.modifier.PerkModifier;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

public class PlayerRaceHandler implements ICharacterModifierHandler<PlayerRace>
{
	private final ICharacter character;
	private PlayerRace race = null;

	public PlayerRaceHandler(ICharacter attached)
	{
		character = attached;
	}

	@Override
	public NBTTagString serializeNBT()
	{
		return new NBTTagString(race.getRegistryName().toString());
	}

	@Override
	public void deserializeNBT(NBTBase nbt)
	{
		race = PlayerRace.REGISTRY.getObject(new ResourceLocation(((NBTTagString)nbt).getString()));
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
	public ICharacter getCharacter()
	{
		return character;
	}

	@Override
	public boolean setModifier(PlayerRace modifier)
	{
		if(modifier == PlayerRace.STEVE)
		{
			return false;
		}
		race = modifier;
		return true;
	}

	@Override
	public List<PerkModifier> getPerkModifiers()
	{
		return race.getPerkModifiers();
	}

}
