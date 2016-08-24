package alpvax.characteroverhaul.api.character.modifier;

import alpvax.characteroverhaul.api.perk.Perk;

public class PerkModifier
{
	public final Perk perk;
	public final int levelIncrease;

	public PerkModifier(Perk perk, int maxLevelIncrease)
	{
		this.perk = perk;
		levelIncrease = maxLevelIncrease;
	}
}
