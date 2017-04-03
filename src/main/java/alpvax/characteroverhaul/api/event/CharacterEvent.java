package alpvax.characteroverhaul.api.event;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import alpvax.characteroverhaul.api.character.CharacterBase;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifier;
import alpvax.characteroverhaul.api.perk.Perk;
import alpvax.characteroverhaul.api.skill.Skill;
import alpvax.characteroverhaul.api.skill.SkillInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * CharacterEvent is fired whenever an event involving characters occurs. <br>
 * If a method utilizes this {@link Event} as its parameter, the method will receive every child event of this class.
 * <br>
 * <br>
 * All children of this event are fired on the {@link MinecraftForge#EVENT_BUS}.
 **/
public class CharacterEvent extends Event
{
	private final ICharacter character;

	public CharacterEvent(ICharacter character)
	{
		this.character = character;
	}

	public ICharacter getCharacter()
	{
		return character;
	}

	/**
	 * CharacterCreate is fired when a character is created.<br>
	 * Use it to register skills to the character and add modifiers.<br>
	 * <br>
	 * This event is fired from new
	 * {@link CharacterBase#CharacterBase(net.minecraftforge.common.capabilities.ICapabilityProvider) CharacterBase}.
	 * <br>
	 * <br>
	 * {@link #skills} contains the Skills that the character can level up in.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
	 **/
	public static class CharacterCreate extends CharacterEvent
	{
		private Set<Skill> skills = Sets.newHashSet();
		private Set<Skill> skillsView = Collections.unmodifiableSet(skills);
		private Map<ResourceLocation, ICharacterModifier> modifiers = Maps.newHashMap();
		private Map<ResourceLocation, ICharacterModifier> modifiersView = Collections.unmodifiableMap(modifiers);
	
		public CharacterCreate(ICharacter character)
		{
			super(character);
		}
	
		public void registerSkill(Skill skill)
		{
			skills.add(skill);
		}
	
		public void removeSkill(Skill skill)
		{
			skills.remove(skill);
		}
		
		/**
		 * @return an unmodifiable view of the skills registered to this character.
		 */
		public Set<Skill> getSkills()
		{
			return skillsView;
		}

		public void addModifier(ResourceLocation key, ICharacterModifier modifier)
		{
			Preconditions.checkNotNull(modifier, "Attempting to register a null modifier to character %s with key %s", getCharacter(), key);
			Preconditions.checkArgument(!modifiers.containsKey(key), "Attempting to register multiple modifiers to character %s with the same key: %s. Old modifier: %s, New modifier: %s", getCharacter(), key, modifiers.get(key), modifier);
			modifiers.put(key, modifier);
		}

		public void removeModifier(ResourceLocation key)
		{
			modifiers.remove(key);
		}

		/**
		 * @return an unmodifiable view of the modifiers that will be attached to this object.
		 */
		public Map<ResourceLocation, ICharacterModifier> getModifiers()
		{
			return modifiersView;
		}
	}

	/**
	 * AquirePerk is fired when a character aquires a perk.<br>
	 * <br>
	 * This event is fired from the private method {@link CharacterBase#setPerkLevel(Perk, int)}.<br>
	 * <br>
	 * {@link #perk} contains the Perk that is being changed.<br>
	 * {@link #currentLevel} contains the the current level of the perk.<br>
	 * {@link #newLevel} contains the new level of the perk. Could be lower than the current level.<br>
	 * <br>
	 * This event is {@link Cancelable}.<br>
	 * If it is canceled or if the new level matches the old level, the perk is not updated.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
	 **/
	@Cancelable
	public static class AquirePerk extends CharacterEvent
	{
		private final Perk perk;
		private final int currentLevel;
		private int newLevel;

		public AquirePerk(ICharacter character, Perk perk, int oldLevel, int newLevel)
		{
			super(character);
			this.perk = perk;
			currentLevel = oldLevel;
			this.newLevel = newLevel;
		}

		public Perk getPerk()
		{
			return perk;
		}

		public int getCurrentLevel()
		{
			return currentLevel;
		}

		public int getNewLevel()
		{
			return newLevel;
		}

		public void setNewLevel(int newLevel)
		{
			this.newLevel = newLevel;
		}
	}

	/**
	 * SkillLevelChange is fired when a character levels up in a skill.<br>
	 * <br>
	 * This event is fired from {@link SkillInstance#setExperience}.<br>
	 * <br>
	 * {@link #skill} contains the Skill that is being changed.<br>
	 * {@link #oldLevel} contains the the old level of the skill.<br>
	 * {@link #newLevel} contains the new level of the skill. Could be lower than the current level.<br>
	 * <br>
	 * This event is not {@link Cancelable}.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
	 **/
	public static class SkillLevelChange extends CharacterEvent
	{
		private final Skill skill;
		private final int oldLevel;
		private final int newLevel;

		public SkillLevelChange(ICharacter character, Skill skill, int oldLevel, int newLevel)
		{
			super(character);
			this.skill = skill;
			this.oldLevel = oldLevel;
			this.newLevel = newLevel;
		}

		public Skill getSkill()
		{
			return skill;
		}

		public int getOldLevel()
		{
			return oldLevel;
		}

		public int getNewLevel()
		{
			return newLevel;
		}
	}
}
