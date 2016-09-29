package alpvax.characteroverhaul.api.character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import alpvax.characteroverhaul.api.ability.IAbility;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifierHandler;
import alpvax.characteroverhaul.api.config.Config;
import alpvax.characteroverhaul.api.effect.ICharacterEffect;
import alpvax.characteroverhaul.api.event.CharacterCreationEvent;
import alpvax.characteroverhaul.api.perk.Perk;
import alpvax.characteroverhaul.api.skill.Skill;
import alpvax.characteroverhaul.api.skill.SkillInstance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public /*abstract/**/ class CharacterBase implements ICharacter
{
	private final Map<ResourceLocation, Integer> perks = new HashMap<>();
	private final Map<ResourceLocation, SkillInstance> skills = new HashMap<>();
	private final ImmutableMap<ResourceLocation, ICharacterModifierHandler<?>> modifiers;
	private Map<UUID, IAbility> abilities = new HashMap<>();
	private UUID[] abilityHotbar = new UUID[Config.numAbilities];

	private final IAffected affected;

	public CharacterBase(AttachCapabilitiesEvent event)
	{
		this(getAffected(event));
	}

	private static IAffected getAffected(AttachCapabilitiesEvent event)
	{
		ICapabilityProvider obj = (ICapabilityProvider)event.getObject();
		if(obj.hasCapability(IAffected.CAPABILITY, null))
		{
			return obj.getCapability(IAffected.CAPABILITY, null);
		}
		for(ICapabilityProvider provider : event.getCapabilities().values())
		{
			if(provider.hasCapability(IAffected.CAPABILITY, null))
			{
				return provider.getCapability(IAffected.CAPABILITY, null);
			}
		}
		return new AffectedBase(obj);
	}

	public CharacterBase(IAffected affected)
	{
		this.affected = affected;
		ImmutableMap.Builder<ResourceLocation, ICharacterModifierHandler<?>> b = ImmutableMap.builder();
		CharacterCreationEvent event = new CharacterCreationEvent(this);
		MinecraftForge.EVENT_BUS.post(event);
		b.putAll(event.getModifiers());
		/*for(Map.Entry<ResourceLocation, ICharacterModifierHandler<?>> e : event.getModifiers().entrySet())
		{
			b.put(e);
			ICharacterModifierHandler<?> handler = e.getValue();
		}*/
		modifiers = b.build();
	}

	@Override
	public <T extends ICapabilityProvider> T getAttachedObject()
	{
		return affected.getAttachedObject();
	}

	@Override
	public Vec3d getPosition()
	{
		return affected.getPosition();
	}

	@Override
	public Vec3d getDirection()
	{
		return affected.getDirection();
	}

	@Override
	public List<ICharacterEffect> getEffects()
	{
		return affected.getEffects();
	}

	@Override
	public void addEffect(ICharacterEffect effect)
	{
		affected.addEffect(effect);
	}

	@Override
	public void removeEffect(UUID id)
	{
		affected.removeEffect(id);
	}

	@Override
	public void cloneFrom(ICharacter newCharacter)
	{
		for(Perk perk : Perk.REGISTRY.getValues())
		{
			int l = newCharacter.getPerkLevel(perk);
			if(l != 0)
			{
				setPerkLevel(perk, l);
			}
		}
		for(Skill skill : Skill.getAllSkills())
		{
			SkillInstance inst = newCharacter.getSkillInstance(skill);
			if(inst != null)
			{
				inst.cloneTo(getSkillInstance(skill));
			}
		}
		/*for(AbilityInstance inst : abilities.values())
		{
			//TODO:inst.cloneTo(newCharacter);
		}*/
		/*for(ICharacterModifier m : modifiers.values())
		{
			if(m.persistAcrossDeath())
			{
				newCharacter.applyModifier(m);
			}
		}*/
		//TODO:Complete cloning
	}

	@Override
	public int getPerkLevel(Perk perk)
	{
		Integer i = perks.get(perk.getRegistryName());
		return i != null ? i.intValue() : 0;
	}

	@Override
	public void aquirePerk(Perk perk)
	{
		setPerkLevel(perk, getPerkLevel(perk) + 1);
	}

	@Override
	public void aquirePerk(Perk perk, int level)
	{
		if(level > getPerkLevel(perk))
		{
			setPerkLevel(perk, level);
		}
	}

	private void setPerkLevel(Perk perk, int level)
	{
		int oldLevel = getPerkLevel(perk);
		perks.put(perk.getRegistryName(), Integer.valueOf(level));
		perk.onLevelChange(oldLevel, level, this);
	}

	@Override
	public SkillInstance getSkillInstance(Skill skill)
	{
		ResourceLocation name = skill.getRegistryName();
		SkillInstance skillInst = skills.get(name);
		if(skillInst == null)// && TODO:skill is not disabled
		{
			skillInst = new SkillInstance(this, skill);
			skills.put(name, skillInst);
		}
		return skillInst;
	}

	@Override
	public int getSkillLevel(Skill skill)
	{
		SkillInstance inst = getSkillInstance(skill);
		if(inst != null)
		{
			return inst.getLevel();
		}
		//TODO:Log
		return Integer.MIN_VALUE;
	}

	@Override
	public void addSkillExperience(Skill skill, float amount)
	{
		SkillInstance inst = getSkillInstance(skill);
		if(inst != null)
		{
			inst.addExp(amount);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ICharacterModifierHandler<?>> T getModifierHandler(ResourceLocation registryName)
	{
		return (T)modifiers.get(registryName);
	}

	@Override
	public List<IAbility> getAbilities()
	{
		return new ArrayList<>(abilities.values());
	}

	@Override
	public List<IAbility> getCurrentAbilities()
	{
		List<IAbility> list = new ArrayList<>();
		for(int i = 0; i < abilityHotbar.length; i++)
		{
			list.add(abilities.get(abilityHotbar[i]));
		}
		return Collections.unmodifiableList(list);
	}

	@Override
	public void addAbility(IAbility ability)
	{
		UUID id = ability.getId();
		Preconditions.checkArgument(!abilities.containsKey(id), "Already an ability with that id: %s", id);
		ability.onAttach();
		abilities.put(id, ability);
	}

	@Override
	public void removeAbility(UUID id)
	{
		if(abilities.containsKey(id))
		{
			abilities.remove(id).onRemove();
		}
	}

	private static final class NBTKeys
	{
		private static final String PERKS = "Perks";
		private static final String SKILLS = "Skills";
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		//Save Effects
		NBTTagCompound nbt = (NBTTagCompound)IAffected.CAPABILITY.writeNBT(this, null);
		//Save Perks
		NBTTagCompound perknbt = new NBTTagCompound();
		for(Perk perk : Perk.REGISTRY.getValues())
		{
			int l = getPerkLevel(perk);
			if(l != 0)
			{
				perknbt.setInteger(perk.getRegistryName().toString(), l);
			}
		}
		if(!perknbt.hasNoTags())
		{
			nbt.setTag(NBTKeys.PERKS, perknbt);
		}
		//Save Skills
		NBTTagCompound skillnbt = new NBTTagCompound();
		for(SkillInstance inst : skills.values())
		{
			skillnbt.setTag(inst.getSkill().getRegistryName().toString(), inst.serializeNBT());
		}
		if(!skillnbt.hasNoTags())
		{
			nbt.setTag(NBTKeys.SKILLS, skillnbt);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		//Load Effects
		IAffected.CAPABILITY.readNBT(this, null, nbt);
		//Load Perks
		if(nbt.hasKey(NBTKeys.PERKS, NBT.TAG_COMPOUND))
		{
			NBTTagCompound perknbt = nbt.getCompoundTag(NBTKeys.PERKS);
			for(String s : perknbt.getKeySet())
			{
				Perk perk = Perk.REGISTRY.getValue(new ResourceLocation(s));
				if(perk != null)//Incorrect NBT check
				{
					setPerkLevel(perk, perknbt.getInteger(s));
				}
			}
		}
		//Load Skills
		if(nbt.hasKey(NBTKeys.SKILLS, NBT.TAG_COMPOUND))
		{
			NBTTagCompound skillnbt = nbt.getCompoundTag(NBTKeys.SKILLS);
			for(String s : skillnbt.getKeySet())
			{
				Skill skill = Skill.REGISTRY.getValue(new ResourceLocation(s));
				if(skill != null)//Incorrect NBT check
				{
					SkillInstance inst = getSkillInstance(skill);
					if(inst != null)
					{
						inst.deserializeNBT(skillnbt.getCompoundTag(s));
					}
				}
			}
		}
	}
}