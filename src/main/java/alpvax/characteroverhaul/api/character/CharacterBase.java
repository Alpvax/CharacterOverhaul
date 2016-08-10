package alpvax.characteroverhaul.api.character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import alpvax.characteroverhaul.api.ability.IAbility;
import alpvax.characteroverhaul.api.character.modifier.CharacterModifierFactory;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifierHandler;
import alpvax.characteroverhaul.api.effect.ICharacterEffect;
import alpvax.characteroverhaul.api.perk.Perk;
import alpvax.characteroverhaul.api.skill.Skill;
import alpvax.characteroverhaul.api.skill.SkillInstance;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public /*abstract/**/ class CharacterBase implements ICharacter
{
	private final Map<ResourceLocation, Integer> perks = new HashMap<>();
	private final Map<ResourceLocation, SkillInstance> skills = new HashMap<>();
	private final ImmutableMap<ResourceLocation, ICharacterModifierHandler<?>> modifiers;
	private Map<UUID, ICharacterEffect> effects = new HashMap<>();
	private Map<UUID, IAbility> abilities = new HashMap<>();
	private final ICapabilityProvider attached;

	public CharacterBase(ICapabilityProvider object)
	{
		attached = object;
		ImmutableMap.Builder<ResourceLocation, ICharacterModifierHandler<?>> b = ImmutableMap.builder();
		for(CharacterModifierFactory<?> factory : CharacterModifierFactory.REGISTRY.getValues())
		{
			if(factory.isValidForCharacter(this))
			{
				b.put(factory.getRegistryName(), factory.newHandler(this));
			}
		}
		modifiers = b.build();

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ICapabilityProvider> T getAttachedObject()
	{
		return (T)attached;
	}

	@Override
	public Vec3d getPosition()
	{
		ICapabilityProvider o = getAttachedObject();
		if(o instanceof Entity)
		{
			return ((Entity)o).getPositionVector();
		}
		if(o instanceof TileEntity)
		{
			BlockPos pos = ((TileEntity)o).getPos();
			return new Vec3d((pos.getX()) + 0.5D, (pos.getY()) + 0.5D, (pos.getZ()) + 0.5D);
		}
		return null;
	}

	@Override
	public Vec3d getDirection()
	{
		ICapabilityProvider o = getAttachedObject();
		if(o instanceof Entity)
		{
			return ((Entity)o).getLookVec();
		}
		if(o instanceof TileEntity)
		{
			TileEntity t = ((TileEntity)o);
			IBlockState state = t.getWorld().getBlockState(t.getPos());
			if(state.getPropertyNames().contains(BlockDirectional.FACING))
			{
				return new Vec3d(state.getValue(BlockDirectional.FACING).getDirectionVec());
			}
		}
		return Vec3d.ZERO;
	}

	@Override
	public void cloneTo(ICharacter newCharacter)
	{
		for(Perk perk : Perk.REGISTRY.getValues())
		{
			int l = getPerkLevel(perk);
			if(l != 0)
			{
				newCharacter.setPerkLevel(perk, l);
			}
		}
		for(SkillInstance skill : skills.values())
		{
			skill.cloneTo(newCharacter.getSkillInstance(skill.getSkill()));
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
	public void setPerkLevel(Perk perk, int level)
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
		if(skillInst == null)
		{
			skillInst = new SkillInstance(this, skill);
			skills.put(name, skillInst);
		}
		return skillInst;
	}

	@Override
	public int getSkillLevel(Skill skill)
	{
		return getSkillInstance(skill).getLevel();
	}

	@Override
	public void addSkillExperience(Skill skill, float amount)
	{
		getSkillInstance(skill).addExp(amount);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ICharacterModifierHandler<?>> T getModifierHandler(ResourceLocation registryName)
	{
		return (T)modifiers.get(registryName);
	}

	@Override
	public List<ICharacterEffect> getEffects()
	{
		return new ArrayList<>(effects.values());
	}

	@Override
	public void addEffect(ICharacterEffect effect)
	{
		UUID id = effect.getId();
		Preconditions.checkArgument(!effects.containsKey(id), "Already an effect with that id: %s", id);
		effect.onAttach();
		effects.put(id, effect);
	}

	@Override
	public void removeEffect(UUID id)
	{
		if(effects.containsKey(id))
		{
			effects.remove(id).onRemove();
		}
	}

	@Override
	public List<IAbility> getAbilities()
	{
		return new ArrayList<>(abilities.values());
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
}