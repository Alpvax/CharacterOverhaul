package alpvax.characteroverhaul.api.skill;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.google.common.base.Preconditions;

import alpvax.characteroverhaul.api.CharacterOverhaul;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.skill.ISkillModifier.SkillExpModifier;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.INBTSerializable;

public class SkillInstance implements INBTSerializable<NBTTagCompound>
{
	private static final class NBTKeys
	{
		private static final String EXPERIENCE = "Experience";
		private static final String MODIFIERS = "ExpModifiers";
		private static final String UUID = "UUID";
		private static final String OPERATION = "Operation";
		private static final String AMOUNT = "Amount";
	}

	private final ICharacter character;
	private final Skill skill;
	/** The total amount of experience */
	private float cumulativeExp;
	/** The amount of experience this level */
	private float experience;
	/** The current level */
	private int level;
	private final Map<UUID, ISkillModifier> modifiers = new HashMap<>();

	private float baseAdd = 0F;
	private float baseMult = 1F;
	private float expMult = 1F;

	public SkillInstance(ICharacter character, Skill skill)
	{
		this.character = character;
		this.skill = skill;
		addExperience(0);
		//TODO:setExperience(0F);
	}

	public void addExp(float amount)
	{
		amount += baseAdd;
		amount *= baseMult;
		amount *= expMult;
		addExperience(amount);
		//TODO:setExperience(cumulativeExp + amount);
	}

	public int getLevel()
	{
		return level;
	}


	public Skill getSkill()
	{
		return skill;
	}

	/**
	 * @param key the unique identifier for this modifier
	 * @param modifier the experience modifier:<br>
	 *            an operation of 0 will add a flat amount of experience before applying other modifiers.<br>
	 *            an operation of 1 will multiply the modified base experience by this amount and add it to the
	 *            experience.<br>
	 *            an operation of 2 will multiply the total of the previous operation by this amount (i.e.
	 *            multiplicative stacking).
	 */
	public void addExpModifier(UUID key, ISkillModifier modifier)
	{
		Preconditions.checkArgument(!modifiers.containsKey(key), "Cannot add modifier with UUID: \"%s\" try removing the old one first", key);
		modifiers.put(key, modifier);
		float amount = modifier.getExperienceModifier();
		switch(modifier.getOperatorType())
		{
		case 0:
			baseAdd += amount;
			break;
		case 1:
			baseMult += amount;
			break;
		case 2:
			expMult *= amount;
			break;
		}
	}

	public void removeExpModifier(UUID key)
	{
		ISkillModifier modifier = modifiers.remove(key);
		if(modifier != null)
		{
			float amount = modifier.getExperienceModifier();
			switch(modifier.getOperatorType())
			{
			case 0:
				baseAdd -= amount;
				break;
			case 1:
				baseMult -= amount;
				break;
			case 2:
				expMult /= amount;
				break;
			}
		}
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat(NBTKeys.EXPERIENCE, cumulativeExp);
		NBTTagList list = new NBTTagList();
		for(Map.Entry<UUID, ISkillModifier> e : modifiers.entrySet())
		{
			ISkillModifier m = e.getValue();
			NBTTagCompound tag = new NBTTagCompound();
			tag.setUniqueId(NBTKeys.UUID, e.getKey());
			tag.setInteger(NBTKeys.OPERATION, m.getOperatorType());
			tag.setFloat(NBTKeys.AMOUNT, m.getExperienceModifier());
			list.appendTag(tag);
		}
		if(list.tagCount() > 0)
		{
			nbt.setTag(NBTKeys.MODIFIERS, list);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		addExperience(nbt.getFloat(NBTKeys.EXPERIENCE));
		//TODO:setExperience(nbt.getFloat(NBTKeys.EXPERIENCE));
		modifiers.clear();
		if(nbt.hasKey(NBTKeys.MODIFIERS, NBT.TAG_LIST))
		{
			NBTTagList list = nbt.getTagList(NBTKeys.MODIFIERS, NBT.TAG_COMPOUND);
			for(int i = 0; i < list.tagCount(); i++)
			{
				NBTTagCompound tag = list.getCompoundTagAt(i);
				addExpModifier(tag.getUniqueId(NBTKeys.UUID), new SkillExpModifier(tag.getInteger(NBTKeys.OPERATION), tag.getFloat(NBTKeys.AMOUNT)));
			}
		}
	}

	/*private boolean levelUp()
	{
		if(level < skill.getMaxLevel(character))
		{
			experience -= skill.getExperienceForLevelUp(level++);//getExp then increase level
			return true;
		}
		return false;
	}
	
	private boolean levelDown()
	{
		if(level > 0)
		{
			experience += skill.getExperienceForLevelUp(--level);//Decrease level then getExp
			return true;
		}
		return false;
	}*/

	/*private void setExperience(float amount)
	{
		experience = cumulativeExp = amount;
		while(experience >= skill.getExperienceForLevelUp(level))
		int newLevel = skill.getLevelfromExperience(cumulativeExp = amount);
		if(newLevel != level)
		{
			skill.onLevelChange(level, newLevel, character);
			MinecraftForge.EVENT_BUS.post(new CharacterEvent.SkillLevelChange(character, skill, level, newLevel));
			level = newLevel;
		}
	}*/

	private void addExperience(float amount)
	{
		cumulativeExp += amount;
		experience += amount;
		float exp;
		while((exp = levelUpdate(experience)) != experience)
		{
			experience = exp;
			CharacterOverhaul.log(Level.INFO, "Level: %i; Experience: %f; Required: %f", level, experience, skill.getExperienceForLevelUp(level));//XXX
		}
	}

	private float levelUpdate(float levelExp)
	{
		if(levelExp < 0)
		{
			return levelExp + skill.getExperienceForLevelUp(--level);//Decrease level then getExp
		}
		float next = skill.getExperienceForLevelUp(level);
		if(levelExp < 0)
		{
			return levelExp - next;
		}
		return levelExp;
	}

	/**
	 * @param levelExp the current experience at this level
	 * @return +1, 0 or -1
	 */
	/*private int getLevelChange(float levelExp)
	{
		return levelExp < 0 ? -1 : levelExp > skill.getExperienceForLevelUp(level) ? 1 : 0;
	}
	
	private void calculateExperience()
	{
		level = 0;
		experience = cumulativeExp;
		float f = skill.getExperienceForLevelUp(level);
		while(experience < f)
		{
			experience -= f;
			level++;
			f = skill.getExperienceForLevelUp(level);
		}
	}*/

	public void cloneTo(SkillInstance newInstance)
	{
		newInstance.addExperience(cumulativeExp);
		//TODO:newInstance.setExperience(cumulativeExp);
		newInstance.baseAdd = baseAdd;
		newInstance.baseMult = baseMult;
		newInstance.expMult = expMult;
		newInstance.modifiers.putAll(modifiers);
	}
}
