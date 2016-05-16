package alpvax.characteroverhaul.api.effect;

import alpvax.characteroverhaul.api.ability.AbilityInstance;

public class EffectInstance
{
	private ICharacterEffect effect;
	private AbilityInstance abilityInst;
	private int ticks = 0;

	public void abilityActivated()
	{
		ticks = 0;
		effect.trigger(abilityInst.getCharacter());
	}

	public void abilityDeactivated()
	{
		effect.reset(abilityInst.getCharacter());
	}

	public void tick()
	{
		effect.tick(abilityInst.getCharacter(), ticks);
	}

	public void remove()
	{
		effect.reset(abilityInst.getCharacter());
	}
}
