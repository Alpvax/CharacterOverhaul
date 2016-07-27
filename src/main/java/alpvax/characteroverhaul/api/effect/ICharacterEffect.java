package alpvax.characteroverhaul.api.effect;

import alpvax.characteroverhaul.api.character.ICharacter;

public interface ICharacterEffect
{
	/**
	 * If this returns true, this effect will have to be equipped in an ability slot for it to have an effect.<br>
	 * Otherwise it will appear on the effects screen instead and will have an effect without being equipped.
	 */
	public boolean requiresAbilitySlot(ICharacter character);

	/**
	 * Called every tick, use it to affect the character or other objects around the character.
	 */
	public void tick(ICharacter character);

	public IEffectProvider getProvider();
}
