package alpvax.characteroverhaul.api.ability;

import alpvax.characteroverhaul.api.character.ICharacter;

public interface IAbilityState
{
	public void onActivate(ICharacter character);

	public void tick(ICharacter character);

	public void onDectivate(ICharacter character);

	public default boolean canActivate(int ticksStateActive, IAbilityState oldState, ICharacter character)
	{
		return true;
	}

	public default boolean canDeactivate(int ticksStateActive, IAbilityState newState, ICharacter character)
	{
		return true;
	}
}
