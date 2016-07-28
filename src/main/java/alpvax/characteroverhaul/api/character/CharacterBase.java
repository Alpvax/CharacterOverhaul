package alpvax.characteroverhaul.api.character;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Preconditions;

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
	private Map<ResourceLocation, Integer> perks = new HashMap<>();
	private Map<ResourceLocation, SkillInstance> skills = new HashMap<>();
	private Map<UUID, ICharacterEffect> effects = new HashMap<>();
	private final ICapabilityProvider attached;

	public CharacterBase(ICapabilityProvider object)
	{
		attached = object;
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

	@Override
	public List<ICharacterEffect> getEffects()
	{
		// TODO Auto-generated method stub
		return null;
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
}