package alpvax.characteroverhaul.api.skill;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import alpvax.characteroverhaul.api.character.ICharacter;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

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
	 * Get how much experience is required to get from the passed in level to the next one up.<br>
	 * Should return the cumulative exp for the next level - the cumulative exp for this level.
	 * @param currentLevel the current level of the character.
	 * @return the amount of experience required to reach the next level.
	 */
	public abstract float getExperienceForLevelUp(int currentLevel);
	
	/**
	 * @param character can be used to limit max level based on character.
	 * @return the max level of the skill for the given character, or -1 if there is no limit.
	 */
	public abstract int getMaxLevel(ICharacter character);

	/**
	 * Level up the character when they gain experience in this skill.<br>
	 * Should not call {@link #onLevelChange(int, int, ICharacter)}, it is called automatically.
	 * @param experience the new experience the character has in this skill.
	 * @return the new level of the character.
	 */
	public abstract int getLevelfromExperience(float experience);

	/**
	 * Apply any changes to the character when they level up in this skill.
	 * @param oldLevel the previous skill level of the character.
	 * @param newLevel the new level (Could be lower than the old level).
	 * @param character the character affected.
	 */
	public abstract void onLevelChange(int oldLevel, int newLevel, ICharacter character);

	public static IForgeRegistry<Skill> REGISTRY = GameRegistry.findRegistry(Skill.class);

	public static List<Skill> getAllSkills()
	{
		return REGISTRY.getValues();
	}
}
