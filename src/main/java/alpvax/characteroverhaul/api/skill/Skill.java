package alpvax.characteroverhaul.api.skill;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import alpvax.characteroverhaul.api.Reference;
import alpvax.characteroverhaul.api.character.ICharacter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;

/**
 * All perks must be registered with {@link GameRegistry#register(IForgeRegistryEntry) GameRegistry.register(perk)}.
 */
public abstract class Skill extends IForgeRegistryEntry.Impl<Skill>
{
	public Skill(String id)
	{
		Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "Attempted instantiation of Skill \"%s\" with no id", toString());
		setRegistryName(id);
	}

	/**
	 * Level up the character when they gain experience in this skill.<br>
	 * Should not call {@link #onLevelChange(int, int, ICharacter)}, it is called automatically.
	 * @param experience the new experience the character has in this skill.
	 * @return the new level of the character.
	 */
	public abstract int getNewLevel(float experience);

	/**
	 * Apply any changes to the character when they level up in this skill.
	 * @param oldLevel the previous skill level of the character.
	 * @param newLevel the new level (Could be lower than the old level).
	 * @param character the character affected.
	 */
	public abstract void onLevelChange(int oldLevel, int newLevel, ICharacter character);

	/**
	 * Change this value in order to allow for more/fewer perks.
	 */
	private static final int MAX_SKILL_ID = 0xff;

	public static final FMLControlledNamespacedRegistry<Skill> REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation(Reference.MOD_ID, "skills"), Skill.class, null, 0, MAX_SKILL_ID, true, null, null, null);

	/**
	 * @return a list of {@linkplain RootPerk RootPerks} (i.e. a list of perk trees).
	 */
	public static List<Skill> getAllSkills()
	{
		return REGISTRY.getValues();
	}
}
