package alpvax.characteroverhaul.api.character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Preconditions;

import alpvax.characteroverhaul.api.effect.ICharacterEffect;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class AffectedBase implements IAffected
{
	private Map<UUID, ICharacterEffect> effects = new HashMap<>();

	private final ICapabilityProvider attached;

	public AffectedBase(ICapabilityProvider object)
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

}
