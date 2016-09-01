package alpvax.characteroverhaul.api.client.gui.widgets;

import alpvax.characteroverhaul.api.character.modifier.ICharacterModifier;

public abstract class CharacterModifierButton<T extends ICharacterModifier>
{
	private final T modifier;
	/** Button width in pixels */
	public int width;
	/** Button height in pixels */
	public int height;
	/** The x position of this control. */
	public int xPosition;
	/** The y position of this control. */
	public int yPosition;

	public CharacterModifierButton(T modifier)
	{
		this.modifier = modifier;
	}

	public T getModifier()
	{
		return modifier;
	}

	public abstract void draw();
}
