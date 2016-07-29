package alpvax.characteroverhaul.api.ability;

import java.util.UUID;

import alpvax.characteroverhaul.api.effect.IEffectProvider;

public interface IAbility
{
	/**
	 * If this returns true, this effect will have to be equipped in an ability slot for it to have an effect.<br>
	 * Otherwise it will appear on the effects screen instead and will have an effect without being equipped.
	 */
	public boolean requiresAbilitySlot();

	public UUID getId();

	/**
	 * Called every tick, use it to passively enable/disable the ability.
	 */
	public void tick();

	public IEffectProvider getProvider();

	/**
	 * Called when the ability is added to the character.
	 */
	public void onAttach();

	/**
	 * Called when the ability is removed from the character.
	 */
	public void onRemove();

	public boolean isActive();

	/**
	 * Called to start the ability. Use it to add ICharacterEffects
	 */
	public void trigger();

	/**
	 * Called to stop the ability. Use it to remove ICharacterEffects.
	 */
	public void reset();
}
