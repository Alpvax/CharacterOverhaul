package alpvax.characteroverhaul.api.perk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import alpvax.characteroverhaul.api.Reference;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.perk.requirement.PerkRequirement;
import alpvax.characteroverhaul.api.perk.requirement.PerkRequirementPerk;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * All perks must be registered with {@link GameRegistry#register(perk)}.
 */
public abstract class Perk extends IForgeRegistryEntry.Impl<Perk>
{
	private final Perk parent;
	private Set<Perk> children = new HashSet<Perk>();

	public Perk(String id, Perk parent)
	{
		Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "Attempted instantiation of perk \"%s\" with no id", toString());
		setRegistryName(id);
		Preconditions.checkArgument(parent != null || this instanceof RootPerk, "Attempted instantiation of perk \"%s\" with no parent. If you want to create a new tree, you need to extend %s", getRegistryName(), RootPerk.class.getName());
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
		if(canUnlock(levelToUnlock, character) && applyUnlockCost(levelToUnlock, character))
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
	 * @param oldLevel the previous perk level of the character.
	 * @param newLevel the new level (Could be lower than the old level).
	 * @param character the character affected.
	 */
	public abstract void onLevelChange(int oldLevel, int newLevel, ICharacter character);

	protected PerkRequirement getRequirements(int level, ICharacter character)
	{
		return this instanceof RootPerk ? new PerkRequirement()
		{
			@Override
			public boolean checkRequirement(ICharacter character)
			{
				return true;
			}
		} : new PerkRequirementPerk(parent, 1);
	}

	/**
	 * Change this value in order to allow for more/fewer perks.
	 */
	private static final int MAX_PERK_ID = 0xff;

	public static final FMLControlledNamespacedRegistry<Perk> registry = PersistentRegistryManager.createRegistry(new ResourceLocation(Reference.MOD_ID, "perks"), Perk.class, null, 0, MAX_PERK_ID, true, null, null, null);

	/**
	 * @return a list of {@linkplain RootPerk RootPerks} (i.e. a list of perk trees).
	 */
	public static List<RootPerk> getRootPerks()
	{
		List<RootPerk> list = new ArrayList<>();
		for(Perk p : registry.getValues())
		{
			if(p instanceof RootPerk)
			{
				list.add((RootPerk)p);
			}
		}
		return list;
	}
}