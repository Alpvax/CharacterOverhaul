package alpvax.characteroverhaul.api.perk.requirement;

import java.util.Iterator;

import alpvax.characteroverhaul.api.character.ICharacter;

public abstract class PerkRequirement implements Iterable<PerkRequirement>
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

	@Override
	public Iterator<PerkRequirement> iterator()
	{
		return new Iterator<PerkRequirement>()
		{
			int i = 0;

			@Override
			public boolean hasNext()
			{
				return i == 0;
			}

			@Override
			public PerkRequirement next()
			{
				return PerkRequirement.this;
			}
		};
	}
}
