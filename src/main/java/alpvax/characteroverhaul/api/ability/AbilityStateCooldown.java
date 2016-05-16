package alpvax.characteroverhaul.api.ability;

import alpvax.characteroverhaul.api.character.ICharacter;

public abstract class AbilityStateCooldown implements IAbilityState
{
	private int maxCooldown;

	public AbilityStateCooldown(int cooldown)
	{
		maxCooldown = cooldown;
	}

	@Override
	public boolean canActivate(int ticksStateActive, IAbilityState oldState, ICharacter character)
	{
		return ticksStateActive >= maxCooldown;
	}
}
