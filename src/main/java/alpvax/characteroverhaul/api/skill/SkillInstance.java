package alpvax.characteroverhaul.api.skill;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Preconditions;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifier.ISkillModifier;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifier.SkillExpModifier;
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
	private float experience;
	private int level;
	private final Map<UUID, ISkillModifier> modifiers = new HashMap<>();

	private float baseAdd = 0F;
	private float baseMult = 1F;
	private float expMult = 1F;

	public SkillInstance(ICharacter character, Skill skill)
	{
		this.character = character;
		this.skill = skill;
		setExperience(0F);
	}

	public void addExp(float amount)
	{
		amount += baseAdd;
		amount *= baseMult;
		amount *= expMult;
		setExperience(experience + amount);
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
		nbt.setFloat(NBTKeys.EXPERIENCE, experience);
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
		setExperience(nbt.getFloat(NBTKeys.EXPERIENCE));
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

	private void setExperience(float amount)
	{
		int newLevel = skill.getNewLevel(experience = amount);
		if(newLevel != level)
		{
			skill.onLevelChange(level, newLevel, character);
		}
	}

	public void cloneTo(SkillInstance newInstance)
	{
		newInstance.setExperience(experience);
		newInstance.baseAdd = baseAdd;
		newInstance.baseMult = baseMult;
		newInstance.expMult = expMult;
		newInstance.modifiers.putAll(modifiers);
	}
}
