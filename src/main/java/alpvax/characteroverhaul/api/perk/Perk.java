package alpvax.characteroverhaul.api.perk;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.effect.IEffectProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * All perks must be registered with {@link GameRegistry#register(IForgeRegistryEntry) GameRegistry.register(perk)}.
 */
public abstract class Perk extends IForgeRegistryEntry.Impl<Perk> implements IEffectProvider
{
	private final PerkTree tree;
	private Set<Perk> children = new HashSet<Perk>();

	public Perk(String id, PerkTree perkTree)
	{
		Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "Attempted instantiation of perk \"%s\" with no id", toString());
		setRegistryName(id);
		tree = perkTree;
	}

	/**
	 * Get the skill tree this perk is part of.<br>
	 * Can return null if the perk is not associated with a skill!
	 */
	protected PerkTree getPerkTree()
	{
		return tree;
	}

	public String getPermissionKey()
	{
		return getPerkTree().getPermissionKey() + getRegistryName().getResourcePath();
	}

	@SideOnly(Side.CLIENT)
	public abstract boolean shouldDisplay(ICharacter character);

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

	public String getLocalisedName()
	{
		ResourceLocation r = getRegistryName();
		return I18n.format(String.format("%s:perk.%s.name", r.getResourceDomain(), r.getResourcePath()));
	}

	public final boolean unlock(ICharacter character)
	{
		int levelToUnlock = character.getPerkLevel(this) + 1;
		if(levelToUnlock <= getMaxLevel(character) && canUnlock(levelToUnlock, character) && applyUnlockCost(levelToUnlock, character))
		{
			character.aquirePerk(this, levelToUnlock);
			return true;
		}
		return false;
	}

	public abstract boolean canUnlock(int level, ICharacter character);

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


	public static IForgeRegistry<Perk> REGISTRY = GameRegistry.findRegistry(Perk.class);

	public static List<Perk> getAllPerks()
	{
		return REGISTRY.getValues();
	}
}