package alpvax.characteroverhaul.perk;

import java.util.HashSet;
import java.util.Set;

import alpvax.characteroverhaul.character.ICharacter;
import alpvax.characteroverhaul.perk.requirement.PerkRequirement;
import alpvax.characteroverhaul.perk.requirement.PerkRequirementPerk;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Perk extends IForgeRegistryEntry.Impl<Perk>
{
	private final Perk parent;
	private Set<Perk> children = new HashSet<Perk>();

	public Perk(String id, Perk parent)
	{
		setRegistryName(id);
		this.parent = parent;
	}

	protected Perk getParent()
	{
		return parent;
	}

	private int getLevel(ICharacter character)
	{
		return character.getPerkLevel(this);
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldDisplay()
	{
		return getParent().shouldDisplay();
	}

	@SideOnly(Side.CLIENT)
	public ResourceLocation getIcon()
	{
		ResourceLocation r = getRegistryName();
		return new ResourceLocation(r.getResourceDomain(), String.format("textures/perk/%s.png", r.getResourcePath()));
	}

	protected void addChild(Perk child)
	{
		children.add(child);
	}

	public Set<Perk> getChildren()
	{
		return new HashSet<Perk>(children);
	}

	public String localisedName()
	{
		ResourceLocation r = getRegistryName();
		return I18n.format(String.format("%s:perk.%s.name", r.getResourceDomain(), r.getResourcePath()));
	}

	/**
	 * Simplified version of {@link #getUnlockCostForDisplay(int, ICharacter)}
	 */
	@SideOnly(Side.CLIENT)
	public String getUnlockCostForDisplay(ICharacter character)
	{
		return getUnlockCostForDisplay(getLevel(character), character);
	}

	@SideOnly(Side.CLIENT)
	public String getUnlockCostForDisplay(int level, ICharacter character)
	{
		return "";//TODO: unlockcostForDisplay
	}

	/**
	 * Simplified version of {@link #canUnlock(int, ICharacter)}
	 */
	public final boolean canUnlock(ICharacter character)
	{
		return canUnlock(getLevel(character), character);
	}

	public boolean canUnlock(int level, ICharacter character)
	{
		if(level >= getMaxLevel(character))
		{
			return false;
		}
		return getRequirements(level, character).checkRequirement(character);
	}

	public final boolean unlock(ICharacter character)
	{
		int levelToUnlock = character.getPerkLevel(this) + 1;
		if(applyUnlockCost(levelToUnlock, character))
		{
			character.setPerkLevel(this, levelToUnlock);
			return true;
		}
		return false;
	}

	/**
	 * @param levelToUnlock the level to apply the costs of.
	 * @param character the character to apply the costs to.
	 * @return true if the cost was expended.
	 */
	protected abstract boolean applyUnlockCost(int levelToUnlock, ICharacter character);

	public abstract int getMaxLevel(ICharacter character);

	/**
	 * Apply any changes to the character when they achieve the perk.
	 * @param levelUnlocked the new level (Could be lower than previously).
	 * @param character the character affected.
	 */
	public abstract void onLevelChange(int levelUnlocked, ICharacter character);

	protected PerkRequirement getRequirements(int level, ICharacter character)
	{
		return new PerkRequirementPerk(parent, 1);
	}
}
