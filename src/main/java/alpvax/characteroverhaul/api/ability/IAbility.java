package alpvax.characteroverhaul.api.ability;

import java.util.UUID;

public abstract class IAbility
{
	protected abstract void onActiveTick(AbilityInstance abilityInstance);

	protected abstract void onInactiveTick(AbilityInstance abilityInstance);

	public abstract void trigger(AbilityInstance abilityInstance);

	public boolean canTrigger(int ticks, boolean isActive)
	{
		return ticks < getCooldown(isActive);
	}

	public abstract int getCooldown(boolean isActive);

	public abstract UUID getID();
}
