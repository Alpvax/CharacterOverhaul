package alpvax.characteroverhaul.api.perk.requirement;

import alpvax.characteroverhaul.character.ICharacter;

public class PerkRequirementGroup extends PerkRequirement
{
	public enum Type
	{
		ALL, ANY, NONE;
	}

	public final Type operation;
	public PerkRequirement[] requirements;

	public PerkRequirementGroup(Type t, PerkRequirement... perkRequirements)
	{
		operation = t;
		requirements = perkRequirements;
	}

	@Override
	public boolean checkRequirement(ICharacter character)
	{
		for(PerkRequirement r : requirements)
		{
			if(r.checkRequirement(character))
			{
				if(operation == Type.ANY)//valid and ANY
				{
					return true;
				}
				if(operation == Type.NONE)//valid and NONE
				{
					return false;
				}
			}
			else if(operation == Type.ALL)//not valid and ALL
			{
				return false;
			}
		}
		return operation != Type.ANY;//Return (true if (all pass and type is ALL) or (all fail and type is NONE), (false if all fail and type is ANY)
	}
}
