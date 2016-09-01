package alpvax.characteroverhaul.api.client.gui;

import java.util.List;

import alpvax.characteroverhaul.api.character.modifier.ICharacterModifier;

public class SimpleCharCreationPage<T extends ICharacterModifier> implements ICharacterCreationPage
{
	private List<T> modifiers;

	public SimpleCharCreationPage(List<T> allModifiers)
	{
		modifiers = allModifiers;
	}
}
