package alpvax.characteroverhaul.api.client.gui.widgets;

import alpvax.characteroverhaul.api.character.modifier.ICharacterModifier;

public abstract class CharacterModifierEntry
{
	private final ICharacterModifier modifier;

	public CharacterModifierEntry(ICharacterModifier modifier)
	{
		this.modifier = modifier;
	}

	@SuppressWarnings("unchecked")
	public <T extends ICharacterModifier> T getModifier()
	{
		return (T)modifier;
	}

	public abstract void draw();
}
