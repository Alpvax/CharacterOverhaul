package alpvax.characteroverhaul.api.effect;

import java.util.UUID;

public interface ICharacterEffect
{
	public UUID getId();

	/**
	 * Called every tick, use it to affect the character or other objects around the character.
	 */
	public void tick();

	public IEffectProvider getProvider();

	/**
	 * Called when the effect is added to the character.
	 */
	public void onAttach();

	/**
	 * Called when the effect is removed from the character.
	 */
	public void onRemove();
}
