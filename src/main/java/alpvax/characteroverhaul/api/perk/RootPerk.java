package alpvax.characteroverhaul.api.perk;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.perk.requirement.PerkRequirement;
import alpvax.characteroverhaul.api.perk.requirement.PerkRequirementTrue;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class RootPerk extends Perk
{
	public RootPerk(String id)
	{
		super(id, null);
	}

	@Override
	protected final Perk getParent()
	{
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public final boolean shouldDisplay(boolean parentDisplayed)
	{
		return shouldDisplay();
	}

	@SideOnly(Side.CLIENT)
	public abstract boolean shouldDisplay();

	@Override
	protected PerkRequirement getRequirements(int level, ICharacter character)
	{
		return new PerkRequirementTrue();
	}
}
