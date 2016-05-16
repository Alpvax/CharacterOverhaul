package alpvax.characteroverhaul.api.ability;

import alpvax.characteroverhaul.api.character.ICharacter;

public class AbilityInstance
{
	private final ICharacter character;
	private final IAbility ability;
	private int ticks;
	private boolean active = false;

	public AbilityInstance(ICharacter character, IAbility ability)
	{
		this.character = character;
		this.ability = ability;
	}

	protected ICharacter getcharacter()
	{
		return character;
	}

	protected int getTicksSinceStateChange()
	{
		return ticks;
	}

	public void tick()
	{
		ticks++;
		if(active)
		{
			ability.onActiveTick(this);
		}
		else
		{
			ability.onInactiveTick(this);
		}
	}

	private boolean canTrigger()
	{
		return ability.canTrigger(ticks, active);
	}

	public boolean trigger()
	{
		if(canTrigger())
		{
			ability.trigger(this);
			return true;
		}
		return false;
	}
}
