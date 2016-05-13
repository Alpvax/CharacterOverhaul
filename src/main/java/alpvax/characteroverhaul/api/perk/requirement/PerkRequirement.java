package alpvax.characteroverhaul.api.perk.requirement;

import alpvax.characteroverhaul.api.character.ICharacter;

public abstract class PerkRequirement
{
	public abstract boolean checkRequirement(ICharacter character);

	/**
	 * Useful for {@linkplain PerkRequirementGroup PerkRequirementGroups}.
	 * @return the inverse of this IPerkRequirement, i.e. !checkRequirement.
	 */
	public PerkRequirement invert()
	{
		return new PerkRequirement()
		{
			@Override
			public boolean checkRequirement(ICharacter character)
			{
				return !PerkRequirement.this.checkRequirement(character);
			}
		};
	}

	public static final PerkRequirement VALID = new PerkRequirement()
	{
		@Override
		public boolean checkRequirement(ICharacter character)
		{
			return true;
		}
	};
}
