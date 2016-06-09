package alpvax.characteroverhaul.api.ability;

import alpvax.characteroverhaul.api.character.ICharacter;

public class AbilityInstance
{
	private final ICharacter character;
	private final Ability ability;
	private int ticks;
	private boolean active = false;

	public AbilityInstance(ICharacter character, Ability ability)
	{
		this.character = character;
		this.ability = ability;
		ticks = 0;
	}

	protected final ICharacter getCharacter()
	{
		return character;
	}

	public final Ability getAbility()
	{
		return ability;
	}

	public int getTicksSinceStateChange()
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

	public boolean trigger()
	{
		if(ability.canToggle(this))
		{
			ability.onTrigger(this);
			active = true;
			return true;
		}
		return false;
	}

	public boolean isActive()
	{
		return active;
	}

	public boolean reset()
	{
		if(active && ability.canToggle(this))
		{
			ability.onReset(this);
			active = false;
			return true;
		}
		return false;
	}

	public final void load(boolean active, int ticks)
	{
		this.active = active;
		this.ticks = ticks;
		if(active)
		{
			ability.onTrigger(this);
		}
		else
		{
			ability.onReset(this);
		}
	}

	/*public void cloneTo(ICharacter newCharacter)
	{
		AbilityInstance i = ability.createNewAbilityInstance(newCharacter);


	}*/
}
