package alpvax.characteroverhaul.api.character;

import java.util.List;
import java.util.UUID;

import alpvax.characteroverhaul.api.ability.IAbility;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifierHandler;
import alpvax.characteroverhaul.api.perk.Perk;
import alpvax.characteroverhaul.api.skill.Skill;
import alpvax.characteroverhaul.api.skill.SkillInstance;
import net.minecraft.util.ResourceLocation;

public interface ICharacter extends IAffected
{
	/**
	 * @param perk the perk to retrieve the level of.
	 * @return the level of the perk, or 0 if it isn't acquired.
	 */
	public int getPerkLevel(Perk perk);

	public void setPerkLevel(Perk perk, int level);

	/**
	 * @param skill the skill to retrieve the instance of.
	 * @return
	 */
	public SkillInstance getSkillInstance(Skill skill);

	/**
	 * @param skill the skill to retrieve the instance of.
	 * @return the level of the skill (ignoring experience at the current level)
	 */
	public int getSkillLevel(Skill skill);

	public void addSkillExperience(Skill skill, float amount);

	public <T extends ICharacterModifierHandler<?>> T getModifierHandler(ResourceLocation registryName);

	//public List<ICharacterModifierHandler<?>> getModifierHandlers();

	public List<IAbility> getAbilities();

	public List<IAbility> getCurrentAbilities();

	public void addAbility(IAbility ability);

	public void removeAbility(UUID id);

	/*public AbilityInstance getAbilityInstance(Ability ability);

	public List<AbilityInstance> getAbilities();

	public boolean hasAbility(Ability ability);

	public void addAbility(Ability ability);

	public void removeAbility(Ability ability);*/


	/**
	 * Used to copy data from this to a new Character upon player respawn.
	 * @param newCharacter
	 */
	public void cloneTo(ICharacter newCharacter);
}
