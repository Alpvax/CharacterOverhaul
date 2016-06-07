package alpvax.characteroverhaul.api.skill;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.perk.Perk;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

/**
 * All perks must be registered with {@link GameRegistry#register(IForgeRegistryEntry) GameRegistry.register(perk)}.
 */
public abstract class Skill extends IForgeRegistryEntry.Impl<Perk>
{
	public Skill(String id)
	{
		Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "Attempted instantiation of Skill \"%s\" with no id", toString());
		setRegistryName(id);
	}

	/**
	 * Apply any changes to the character when they level up in this skill.
	 * @param oldLevel the previous skill level of the character.
	 * @param newLevel the new level (Could be lower than the old level).
	 * @param character the character affected.
	 */
	public abstract void onLevelChange(int oldLevel, int newLevel, ICharacter character);
}
