package alpvax.characteroverhaul.api.perk.requirement;

import alpvax.characteroverhaul.api.character.ICharacter;

public abstract class PerkRequirement
{
	public abstract boolean checkRequirement(ICharacter character);

	/**
	 * Get the requirement text to display.
	 * @param achieved whether or not the character passes the requirement.
	 * @return a string to be displayed in the GUI.
	 */
	public abstract String getDisplayText(boolean achieved);

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

			@Override
			public String getDisplayText(boolean achieved)
			{
				// TODO Auto-generated method stub
				return null;
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

		@Override
		public String getDisplayText(boolean achieved)
		{
			return null;
		}
	};
}
