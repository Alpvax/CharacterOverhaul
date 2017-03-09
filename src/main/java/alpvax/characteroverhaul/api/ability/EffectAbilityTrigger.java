package alpvax.characteroverhaul.api.ability;

import java.util.UUID;

import alpvax.characteroverhaul.api.effect.Effect;
import alpvax.characteroverhaul.api.effect.EffectType;

public abstract class EffectAbilityTrigger extends Effect
{
	private Ability ability;

	public EffectAbilityTrigger(Ability toTrigger)
	{
		super(toTrigger);
		// TODO Auto-generated constructor stub
	}

	@Override
	public UUID getId()
	{
		return ability.getID();
	}

	@Override
	public EffectType getEffectType()
	{
		return EffectType.ABILITY;
	}

	@Override
	public boolean canBePurged()
	{
		return false;
	}

	@Override
	public void tickEffect()
	{
	}

	@Override
	protected void trigger()
	{
		// TODO Auto-generated method stub
		if(ability.canTrigger(false))
		{
			ability.trigger();
		}
	}

	@Override
	protected void reset()
	{
		// TODO Auto-generated method stub
		ability.reset();
	}

}
