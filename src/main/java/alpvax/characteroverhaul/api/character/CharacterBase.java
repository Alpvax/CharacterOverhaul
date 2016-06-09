package alpvax.characteroverhaul.api.character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alpvax.characteroverhaul.api.ability.Ability;
import alpvax.characteroverhaul.api.ability.AbilityInstance;
import alpvax.characteroverhaul.api.perk.Perk;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public /*abstract/**/ class CharacterBase implements ICharacter
{
	private Map<ResourceLocation, Integer> perks = new HashMap<>();
	private Map<ResourceLocation, AbilityInstance> abilities = new HashMap<>();
	private final ICapabilityProvider attached;

	public CharacterBase(ICapabilityProvider object)
	{
		attached = object;
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

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ICapabilityProvider> T getAttachedObject()
	{
		return (T)attached;
	}

	/**
	 * Utility method to provide a shortcut to the AttributeMap.<br>
	 * May return null if Attributes aren't supported on this object;
	 * @return
	 */
	protected AbstractAttributeMap getAttributeMap()
	{
		ICapabilityProvider o = getAttachedObject();
		return o instanceof EntityLivingBase ? ((EntityLivingBase)o).getAttributeMap() : null;
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
	public List<AbilityInstance> getAbilities()
	{
		return new ArrayList<>(abilities.values());
	}

	@Override
	public AbilityInstance getAbilityInstance(Ability ability)
	{
		return abilities.get(ability.getRegistryName());
	}

	@Override
	public boolean hasAbility(Ability ability)
	{
		return abilities.containsKey(ability.getRegistryName());
	}

	@Override
	public void addAbility(Ability ability)
	{
		abilities.put(ability.getRegistryName(), ability.createNewAbilityInstance(this));
		//TODO:markdirty
	}

	@Override
	public void removeAbility(Ability ability)
	{
		abilities.remove(ability.getRegistryName());
		//TODO:markdirty
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
		for(AbilityInstance inst : abilities.values())
		{
			//TODO:inst.cloneTo(newCharacter);
		}
		/*for(ICharacterModifier m : modifiers.values())
		{
			if(m.persistAcrossDeath())
			{
				newCharacter.applyModifier(m);
			}
		}*/
		//TODO:Complete cloning
	}
}