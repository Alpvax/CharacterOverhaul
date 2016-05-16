package alpvax.characteroverhaul.api.perk;

import alpvax.characteroverhaul.api.ability.IAbility;
import alpvax.characteroverhaul.api.character.ICharacter;

public abstract class PerkAbility extends Perk
{
	private final IAbility ability;

	public PerkAbility(IAbility ability, String id, Perk parent)
	{
		super(id, parent);
		this.ability = ability;
	}

	@Override
	public void onLevelChange(int oldLevel, int newLevel, ICharacter character)
	{
		if(newLevel == 0)
		{
			character.removeAbility(ability);
		}
		else if(!character.hasAbility(ability))
		{
			character.addAbility(ability);
		}
	}

}
