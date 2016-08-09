package alpvax.characteroverhaul.api.character.modifier;

import alpvax.characteroverhaul.api.character.ICharacter;
import net.minecraft.util.ResourceLocation;

public interface ICharacterModifierHandler<T extends ICharacterModifier> extends Comparable<ICharacterModifierHandler<?>>
{
	/**
	 * @return the key this is attached to the character with.
	 */
	public ResourceLocation getKey();

	/**
	 * @return the character this is attached to.
	 */
	public ICharacter getCharacter();

	/**
	 * Set the current modifier for this key.
	 * @param modifier
	 * @return true if the modifier was changed, false if it couldn't be set.
	 */
	public boolean setModifier(T modifier);

	/**
	 * Use to modify the character
	 */
	public void applyModifier();

	public void removeModifier();

	@Override
	public default int compareTo(ICharacterModifierHandler<?> o)
	{
		return getKey().toString().compareToIgnoreCase(o.getKey().toString());
	}
}
