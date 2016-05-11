package alpvax.characteroverhaul.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public class SerializeableCapabilityProvider<T> extends SimpleCapabilityProvider<T> implements INBTSerializable<NBTBase>
{
	public SerializeableCapabilityProvider(Capability<T> capability)
	{
		super(capability);
	}

	public SerializeableCapabilityProvider(T capabilityHandler, Capability<T> capability)
	{
		super(capabilityHandler, capability);
	}

	public SerializeableCapabilityProvider(T capabilityHandler, Capability<T> capability, EnumFacing... sides)
	{
		super(capabilityHandler, capability, sides);
	}

	@Override
	public NBTBase serializeNBT()
	{
		return getCapability().writeNBT(getHandler(), getSide());
	}

	@Override
	public void deserializeNBT(NBTBase nbt)
	{
		getCapability().readNBT(getHandler(), getSide(), nbt);
	}

}
