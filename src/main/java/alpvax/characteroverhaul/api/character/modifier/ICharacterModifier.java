package alpvax.characteroverhaul.api.character.modifier;

import alpvax.characteroverhaul.api.character.ICharacter;

public interface ICharacterModifier
{
	public String getType();

	public boolean isValidForCharacter(ICharacter character);

	public void onAttach(ICharacter character);

	public void onDetach(ICharacter character);
}
