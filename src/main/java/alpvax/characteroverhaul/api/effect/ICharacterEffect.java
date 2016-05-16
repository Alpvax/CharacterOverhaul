package alpvax.characteroverhaul.api.effect;

import alpvax.characteroverhaul.api.character.ICharacter;

public interface ICharacterEffect
{
	/**
	 * Called to start the ability.<br>
	 * Should be used to add attribute modifiers etc.
	 * @param affected The object affected by the ability.
	 */
	public void trigger(ICharacter affected);

	/**
	 * Called to stop the ability. Must revert any changes from {@link #trigger}<br>
	 * Should be used to remove attribute modifiers etc.<br>
	 * Also called if the abilities are removed from the provider, so the affected targets should be left in a state as
	 * though they didn't have abilities.
	 * @param affected The object affected by the ability.
	 */
	public void reset(ICharacter affected);

	/**
	 * Called every time the effect ticks (as determined by {@link EffectInstance#shouldTick}).<br>
	 * Should be used for e.g. abilities that have an effect that does something every x seconds
	 * @param affected The object affected by the ability.
	 * @param ticksSinceStateChange the number of ticks this effect has been active
	 */
	public void tick(ICharacter affected, int ticksSinceStateChange);
}
