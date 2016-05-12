package alpvax.characteroverhaul.api.perk.requirement;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.perk.Perk;

public class PerkRequirementPerk extends PerkRequirement
{
	private Perk perk;
	private int level;

	public PerkRequirementPerk(Perk required, int requiredLevel)
	{
		perk = required;
		level = requiredLevel;
	}

	@Override
	public boolean checkRequirement(ICharacter character)
	{
		return character.getPerkLevel(perk) >= level;
	}

}
