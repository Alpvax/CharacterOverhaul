package alpvax.characteroverhaul.api.ability;

import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import alpvax.characteroverhaul.api.ability.IAbility.AbilityState;
import alpvax.characteroverhaul.api.ability.IAbility.EffectEntry;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.effect.ICharacterEffect;

public class AbilityInstance
{
	private final ICharacter character;
	//private final UUID abilityID;
	private int ticksSinceStateChange = 0;

	private Multimap<AbilityState, ICharacterEffect> effects = HashMultimap.create();

	private AbilityState currentState = null;

	public AbilityInstance(ICharacter character, IAbility ability)
	{
		this.character = character;
		//abilityID = ability.getID();
		for(EffectEntry e : ability.getAllEffects())
		{
			if(e.activeState)
			{
				effects.put(AbilityState.ACTIVE, e.effect);
			}
			if(e.inactiveState)
			{
				effects.put(AbilityState.INACTIVE, e.effect);
			}
		}
		changeState(ability.getDefaultState());
	}

	public void changeState(AbilityState state)
	{
		if(state == currentState)
		{
			return;
		}
		for(ICharacterEffect e : effects.get(currentState))
		{
			e.reset(character);
		}
		ticksSinceStateChange = 0;
		for(ICharacterEffect e : effects.get(state))
		{
			e.trigger(character);
		}
	}

	public void tick()
	{
		ticksSinceStateChange++;
		for(ICharacterEffect e : effects.get(currentState))
		{
			e.tick(character, ticksSinceStateChange);
		}
	}

	public List<ICharacterEffect> getActiveEffects()
	{

	}

	public ICharacter getCharacter()
	{
		return character;
	}
}
