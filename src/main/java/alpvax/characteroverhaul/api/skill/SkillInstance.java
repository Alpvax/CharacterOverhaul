package alpvax.characteroverhaul.api.skill;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Preconditions;

import alpvax.characteroverhaul.api.character.ICharacter;
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
		private static final String MOD_ID_MOST = "IDMost";
		private static final String MOD_ID_LEAST = "IDLeast";
		private static final String BASE_ADD = "AdditionalBaseExp";
		private static final String BASE_MULT = "BaseExpMultiplier";
		private static final String EXP_ADD = "CalculatedExpMultiplier";
		private static final String EXP_MULT = "FlatExpBonus";
	}

	private final ICharacter character;
	private final Skill skill;
	private float experience;
	private int level;
	private final Map<UUID, SkillExpModifier> modifiers = new HashMap<>();

	private float baseAdd = 0F;
	private float baseMult = 1F;
	private float expMult = 1F;
	private float expAdd = 0F;

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
		amount += expAdd;
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
	 * @param baseAdd the amount of additional experience is awarded before multiplication.
	 * @param baseMult the multiplier for the base experience (i.e. 1.2 will reward a total of 1.2 times base experience
	 *            before further modification).
	 * @param expMult the multiplier for the calculated experience (i.e. multiplier will also effect baseMults).
	 * @param expAdd a flat exp bonus added after all other calculation (i.e. if base is reduced to 0 xp, the only
	 *            experience recieved will be this).
	 */
	public void addExpModifier(UUID key, float baseAdd, float baseMult, float expMult, float expAdd)
	{
		addExpModifier(key, new SkillExpModifier(baseAdd, baseMult, expMult, expAdd));
	}
	public void addExpModifier(UUID key, SkillExpModifier modifier)
	{
		Preconditions.checkArgument(!modifiers.containsKey(key), "Cannot add modifier with UUID: \"%s\" try removing the old one first", key);
		modifiers.put(key, modifier);
		baseAdd += modifier.baseAdd;
		baseMult += modifier.baseMult - 1F;
		expMult *= modifier.expMult;
		expAdd += modifier.expAdd;
	}

	public void removeExpModifier(UUID key)
	{
		SkillExpModifier modifier = modifiers.remove(key);
		if(modifier != null)
		{
			baseAdd -= modifier.baseAdd;
			baseMult -= (modifier.baseMult - 1F);
			expMult /= modifier.expMult;
			expAdd -= modifier.expAdd;
		}
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat(NBTKeys.EXPERIENCE, experience);
		NBTTagList list = new NBTTagList();
		for(Map.Entry<UUID, SkillExpModifier> e : modifiers.entrySet())
		{
			NBTTagCompound tag = new NBTTagCompound();
			UUID key = e.getKey();
			tag.setLong(NBTKeys.MOD_ID_MOST, key.getMostSignificantBits());
			tag.setLong(NBTKeys.MOD_ID_LEAST, key.getLeastSignificantBits());
			tag.setFloat(NBTKeys.BASE_ADD, e.getValue().baseAdd);
			tag.setFloat(NBTKeys.BASE_MULT, e.getValue().baseMult);
			tag.setFloat(NBTKeys.EXP_ADD, e.getValue().expMult);
			tag.setFloat(NBTKeys.EXP_MULT, e.getValue().expAdd);
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
				float ba = tag.hasKey(NBTKeys.BASE_ADD) ? tag.getFloat(NBTKeys.BASE_ADD) : 0F;
				float bm = tag.hasKey(NBTKeys.BASE_MULT) ? tag.getFloat(NBTKeys.BASE_MULT) : 1F;
				float em = tag.hasKey(NBTKeys.EXP_MULT) ? tag.getFloat(NBTKeys.EXP_MULT) : 1F;
				float ea = tag.hasKey(NBTKeys.EXP_ADD) ? tag.getFloat(NBTKeys.EXP_ADD) : 0F;
				addExpModifier(new UUID(tag.getLong(NBTKeys.MOD_ID_MOST), tag.getLong(NBTKeys.MOD_ID_LEAST)), new SkillExpModifier(ba, bm, em, ea));
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
		newInstance.expAdd = expAdd;
		newInstance.modifiers.putAll(modifiers);
	}


	public static final class SkillExpModifier
	{
		private float baseAdd = 0F;
		private float baseMult = 1F;
		private float expMult = 1F;
		private float expAdd = 0F;

		/**
		 * @param baseAdd the amount of additional experience is awarded before multiplication.
		 * @param baseMult the multiplier for the base experience (i.e. 1.2 will reward a total of 1.2 times base
		 *            experience before further modification).
		 * @param expMult the multiplier for the calculated experience (i.e. multiplier will also effect baseMults).
		 * @param expAdd a flat exp bonus added after all other calculation (i.e. if base is reduced to 0 xp, the only
		 *            experience recieved will be this).
		 */
		public SkillExpModifier(float baseAdd, float baseMult, float expMult, float expAdd)
		{
			this.baseAdd = baseAdd;
			this.baseMult = baseMult;
			this.expMult = expMult;
			this.expAdd = expAdd;
		}
	}
}
