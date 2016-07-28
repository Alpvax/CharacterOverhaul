package alpvax.characteroverhaul.api.effect;

import java.util.UUID;

public interface ICharacterEffect
{
	public UUID getId();

	/**
	 * If this returns true, this effect will have to be equipped in an ability slot for it to have an effect.<br>
	 * Otherwise it will appear on the effects screen instead and will have an effect without being equipped.
	 */
	public boolean requiresAbilitySlot();

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
