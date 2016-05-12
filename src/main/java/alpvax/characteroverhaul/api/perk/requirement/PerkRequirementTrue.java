package alpvax.characteroverhaul.api.perk.requirement;

import alpvax.characteroverhaul.api.character.ICharacter;

public class PerkRequirementTrue extends PerkRequirement
{
	@Override
	public boolean checkRequirement(ICharacter character)
	{
		return true;
	}

}
