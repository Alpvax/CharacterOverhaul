package alpvax.characteroverhaul.api.ability;

import alpvax.characteroverhaul.api.character.ICharacter;

public abstract class Ability
{
	private IAbilityState activeState = getDefaultState();
	private int ticksSinceStateChanged = 0;

	protected abstract IAbilityState[] getStates();

	protected IAbilityState getDefaultState()
	{
		return getStates()[0];
	}

	public IAbilityState getState()
	{
		return activeState;
	}

	public boolean setState(IAbilityState state, ICharacter attached)
	{
		if(state.canDeactivate(ticksSinceStateChanged, state, attached) && state.canActivate(ticksSinceStateChanged, activeState, attached))
		{
			activeState.onDectivate(attached);
			activeState = state;
			activeState.onActivate(attached);
			ticksSinceStateChanged = 0;
			return true;
		}
		return false;
	}

	public void tick(ICharacter attached)
	{
		ticksSinceStateChanged++;
	}
}
