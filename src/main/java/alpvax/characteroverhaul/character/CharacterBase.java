package alpvax.characteroverhaul.character;

import java.util.HashMap;
import java.util.Map;

import alpvax.characteroverhaul.api.perk.Perk;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public /*abstract/**/ class CharacterBase implements ICharacter
{
	private Map<ResourceLocation, Integer> perks = new HashMap<>();

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
	public <T extends ICapabilityProvider> T getAttachedObject()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractAttributeMap getAttributeMap()
	{
		// TODO Auto-generated method stub
		return null;
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
}