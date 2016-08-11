package alpvax.characteroverhaul.api.modifier.player;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifier;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerClass implements ICharacterModifier
{
	@Override
	public boolean isValidForCharacter(ICharacter character)
	{
		return character.getAttachedObject() instanceof EntityPlayer;
	}

	@Override
	public void onAttach(ICharacter character)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onDetach(ICharacter character)
	{
		// TODO Auto-generated method stub

	}

}
