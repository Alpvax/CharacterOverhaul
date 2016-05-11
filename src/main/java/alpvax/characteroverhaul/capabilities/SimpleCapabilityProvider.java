package alpvax.characteroverhaul.capabilities;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class SimpleCapabilityProvider<T> implements ICapabilityProvider
{
	private static final EnumFacing[] ALL_SIDES_AND_NULL = ArrayUtils.add(EnumFacing.VALUES, 0, null);

	private T handler;
	private EnumFacing[] sides;
	private Capability<T> cap;

	public SimpleCapabilityProvider(Capability<T> capability)
	{
		this(capability.getDefaultInstance(), capability);
	}

	public SimpleCapabilityProvider(T capabilityHandler, Capability<T> capability)
	{
		this(capabilityHandler, capability, ALL_SIDES_AND_NULL);
	}

	public SimpleCapabilityProvider(T capabilityHandler, Capability<T> capability, EnumFacing... sides)
	{
		handler = capabilityHandler != null ? capabilityHandler : capability.getDefaultInstance();
		cap = capability;
		this.sides = sides == null ? new EnumFacing[]{null} : (sides.length > 0 ? sides : ALL_SIDES_AND_NULL);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == cap)
		{
			for(EnumFacing f : sides)
			{
				if(f == facing)
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public <S> S getCapability(Capability<S> capability, EnumFacing facing)
	{
		return cap.cast(hasCapability(capability, facing) ? getHandler() : null);
	}

	protected Capability<T> getCapability()
	{
		return cap;
	}

	protected T getHandler()
	{
		return handler;
	}

	protected EnumFacing getSide()
	{
		return sides[sides.length - 1];
	}
}
