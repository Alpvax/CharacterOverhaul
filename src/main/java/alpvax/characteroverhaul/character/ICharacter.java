package alpvax.characteroverhaul.character;

import alpvax.characteroverhaul.api.perk.Perk;

public interface ICharacter
{
	/**
	 * @param perk the perk to retrieve the level of.
	 * @return the level of the perk, or 0 if it isn't acquired.
	 */
	public int getPerkLevel(Perk perk);

	public void setPerkLevel(Perk perk, int level);
}
