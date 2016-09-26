package alpvax.characteroverhaul.api.client.gui;

import java.util.List;

import alpvax.characteroverhaul.api.character.ICharacter;

public interface ICharacterCreationPageHandler
{
	public List<ICharacterCreationPage> getPages(ICharacter character);
}
